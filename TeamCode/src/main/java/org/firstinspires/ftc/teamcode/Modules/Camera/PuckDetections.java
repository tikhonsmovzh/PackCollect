package org.firstinspires.ftc.teamcode.Modules.Camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Environment;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.sumElems;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PuckDetections implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
    public AtomicReference<Integer> RedCount = new AtomicReference<>(0), BlueCount = new AtomicReference<>(0);
    public AtomicReference<Double> BluePercent = new AtomicReference<>();
    public AtomicReference<Vector2> BlueConcentrationPos = new AtomicReference<>(new Vector2()), RedConcentrationPos = new AtomicReference<>(new Vector2());

    private Mat _bluePuckMat, _redPuckMat;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        _bluePuckMat = imread(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/blue_puck.bmp");
        cvtColor(_bluePuckMat, _bluePuckMat, COLOR_BGR2RGB);
        resize(_bluePuckMat, _bluePuckMat, new Size(_bluePuckMat.width() * Configs.Camera.CompressionCoef, _bluePuckMat.height() * Configs.Camera.CompressionCoef));

        _redPuckMat = imread(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/red_puck.bmp");
        cvtColor(_redPuckMat, _redPuckMat, COLOR_BGR2RGB);
        resize(_redPuckMat, _redPuckMat, new Size(_redPuckMat.width() * Configs.Camera.CompressionCoef, _redPuckMat.height() * Configs.Camera.CompressionCoef));

        BluePercent.set(0d);
    }

    private AtomicReference<Mat> _drawFrame = new AtomicReference<>();

    @Override
    public Object processFrame(Mat frm, long captureTimeNanos) {
        Mat frame = frm.clone();

        resize(frame, frame, new Size(frm.width() * 0.8d, frm.height() * 0.8d));
        frame = frm.submat(frame.height() - Configs.Camera.ImgSizeY, frame.height(), 0, frame.width());

        _drawFrame.set(frame.clone());

        Mat blueBinaryFrame = frame.clone();

        blur(blueBinaryFrame, blueBinaryFrame, new Size(10, 10));
        cvtColor(blueBinaryFrame, blueBinaryFrame, COLOR_RGB2HSV);

        inRange(blueBinaryFrame, new Scalar(Configs.Camera.hBlueDown, Configs.Camera.cBlueDown, Configs.Camera.vBlueDowm), new Scalar(Configs.Camera.hBlueUp, Configs.Camera.cBlueUp, Configs.Camera.vBlueUp), blueBinaryFrame);

        erode(blueBinaryFrame, blueBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));
        dilate(blueBinaryFrame, blueBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));

        if (sumElems(blueBinaryFrame).val[0] != 0) {
            List<MatOfPoint> contours = new ArrayList();

            findContours(blueBinaryFrame, contours, new Mat(), RETR_TREE, CHAIN_APPROX_SIMPLE);

            int contoursForOne = contours.size() > Configs.Camera.ThreadCount ? contours.size() / Configs.Camera.ThreadCount : 1;

            for (int t = 0; t < Math.min(contours.size(), Configs.Camera.ThreadCount); t++) {
                List<MatOfPoint> contour = contours.subList(t * contoursForOne, (t + 1) * contoursForOne);

                Mat myFrm = frame.clone();

                _threads[t] = new Thread(() -> {
                    Mat res = new Mat();

                    for (MatOfPoint i : contour) {
                        if(i == null)
                            continue;

                        Rect boundRect = boundingRect(i.t());

                        if (boundRect.empty() ||
                                Math.abs(boundRect.width * Configs.Camera.CompressionCoef) < 0.9d ||
                                Math.abs(boundRect.height * Configs.Camera.CompressionCoef) < 0.9d ||
                                boundRect.area() < 4d)
                            continue;

                        Mat maybeBluePuck = myFrm.submat(boundRect);

                        cvtColor(maybeBluePuck, maybeBluePuck, COLOR_BGR2RGB);

                        resize(maybeBluePuck, maybeBluePuck, new Size(maybeBluePuck.width() * Configs.Camera.CompressionCoef, maybeBluePuck.height() * Configs.Camera.CompressionCoef));

                        matchTemplate(maybeBluePuck, _bluePuckMat, res, COLOR_BGR2RGB);

                        double per = res.get(0, 0)[0];

                        if (per > Configs.Camera.MinSimilarity && per < Configs.Camera.MaxSimilarity)
                            rectangle(_drawFrame.get(), boundRect, new Scalar(0, 0, 255), 5);
                    }
                });

                _threads[t].start();
            }

            for (Thread i : _threads)
                if(i != null)
                    while (i.getState() != Thread.State.TERMINATED) ;
        }

        Bitmap b = Bitmap.createBitmap(_drawFrame.get().width(), _drawFrame.get().height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(_drawFrame.get(), b);
        LastFrame.set(b);

        return frm;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    private Thread[] _threads = new Thread[Configs.Camera.ThreadCount];

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(LastFrame.get()));
    }
}
