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
import static org.opencv.core.Core.normalize;
import static org.opencv.core.Core.sumElems;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class PuckDetections implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
    public AtomicReference<Integer> RedCount = new AtomicReference<>(0), BlueCount = new AtomicReference<>(0);
    public AtomicReference<Vector2> BlueConcentrationPos = new AtomicReference<>(new Vector2()), RedConcentrationPos = new AtomicReference<>(new Vector2());

    private static AtomicReference<Mat> _bluePuckMat = new AtomicReference<>(), _redPuckMat = new AtomicReference<>();

    private static ExecutorService _executorService;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        if(_executorService == null)
            _executorService = Executors.newWorkStealingPool(Configs.Camera.ThreadCount);

        if(_redPuckMat.get() == null || _bluePuckMat.get() == null) {
            _bluePuckMat.set(imread(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/blue_puck.bmp"));
            cvtColor(_bluePuckMat.get(), _bluePuckMat.get(), COLOR_BGR2RGB);
            resize(_bluePuckMat.get(), _bluePuckMat.get(), new Size(_bluePuckMat.get().width() * Configs.Camera.CompressionCoef, _bluePuckMat.get().height() * Configs.Camera.CompressionCoef));

            _redPuckMat.set(imread(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/red_puck.bmp"));
            cvtColor(_redPuckMat.get(), _redPuckMat.get(), COLOR_BGR2RGB);
            resize(_redPuckMat.get(), _redPuckMat.get(), new Size(_redPuckMat.get().width() * Configs.Camera.CompressionCoef, _redPuckMat.get().height() * Configs.Camera.CompressionCoef));
        }
    }

    @Override
    public Object processFrame(Mat frm, long captureTimeNanos) {
        Mat frame = frm.clone();

        normalize(frame, frame, 100d);

        //resize(frame, frame, new Size(frm.width() * 0.8d, frm.height() * 0.8d));
        frame = frm.submat(new Rect(0, frame.height() - Configs.Camera.ImgSizeY, frame.width(), Configs.Camera.ImgSizeY));


        Mat drawFrame = frame.clone();

        Mat blueBinaryFrame = frame.clone();

        blur(blueBinaryFrame, blueBinaryFrame, new Size(10, 10));
        cvtColor(blueBinaryFrame, blueBinaryFrame, COLOR_RGB2HSV);

        Mat redBinaryFrame = blueBinaryFrame.clone();

        inRange(blueBinaryFrame, new Scalar(Configs.Camera.hBlueDown, Configs.Camera.cBlueDown, Configs.Camera.vBlueDowm), new Scalar(Configs.Camera.hBlueUp, Configs.Camera.cBlueUp, Configs.Camera.vBlueUp), blueBinaryFrame);

        erode(blueBinaryFrame, blueBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));
        dilate(blueBinaryFrame, blueBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));

        ArrayList<Callable<Rect>> tasks = new ArrayList<>();

        BlueConcentrationPos.set(new Vector2());
        BlueCount.set(0);

        List<MatOfPoint> contours = new ArrayList();

        if (sumElems(blueBinaryFrame).val[0] != 0) {
            findContours(blueBinaryFrame, contours, new Mat(), RETR_TREE, CHAIN_APPROX_SIMPLE);

            for (MatOfPoint i : contours){
                Mat finalFrame = frame;
                tasks.add(()->matchContour(finalFrame, i, _bluePuckMat, Configs.Camera.MinBlueSimilarity, Configs.Camera.MaxBlueSimilarity));
            }

            try {
                List<Future<Rect>> result = _executorService.invokeAll(tasks);

                int blueCount = 0;

                double posSum = 0;

                for (Future<Rect> i : result) {
                    Rect boundRect = i.get();

                    if (boundRect != null) {
                        blueCount++;

                        posSum += boundRect.x;

                        rectangle(drawFrame, boundRect, new Scalar(0, 0, 255), 5);
                    }
                }

                BlueCount.set(blueCount);
                BlueConcentrationPos.set(new Vector2(blueCount != 0 ? posSum / blueCount : 0, 0));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        tasks.clear();

        inRange(redBinaryFrame, new Scalar(Configs.Camera.hRedDown, Configs.Camera.cRedDown, Configs.Camera.vRedDowm), new Scalar(Configs.Camera.hRedUp, Configs.Camera.cRedUp, Configs.Camera.vRedUp), redBinaryFrame);

        erode(redBinaryFrame, redBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));
        dilate(redBinaryFrame, redBinaryFrame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize)));

        RedConcentrationPos.set(new Vector2());
        RedCount.set(0);

        contours.clear();

        if (sumElems(redBinaryFrame).val[0] != 0) {
            findContours(redBinaryFrame, contours, new Mat(), RETR_TREE, CHAIN_APPROX_SIMPLE);

            for (MatOfPoint i : contours){
                Mat finalFrame = frame;
                tasks.add(()->matchContour(finalFrame, i, _redPuckMat, Configs.Camera.MinRedSimilarity, Configs.Camera.MaxRedSimilarity));
            }

            try {
                List<Future<Rect>> result = _executorService.invokeAll(tasks);

                int redCount = 0;

                double posSum = 0;

                for (Future<Rect> i : result){
                    Rect boundRect = i.get();

                    if(boundRect != null) {
                        redCount++;

                        posSum += boundRect.x;

                        rectangle(drawFrame, boundRect, new Scalar(255, 0, 0), 5);
                    }
                }


                RedCount.set(redCount);
                BlueConcentrationPos.set(new Vector2(redCount != 0 ? posSum / redCount : 0, 0));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        Bitmap b = Bitmap.createBitmap(drawFrame.width(), drawFrame.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(drawFrame, b);
        LastFrame.set(b);

        return frm;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    private Rect matchContour(Mat frame, MatOfPoint contour, AtomicReference<Mat> template, double minSimilarity, double maxSimilarity){
        Rect boundRect = boundingRect(contour.t());
        boundRect.x -= 3;
        boundRect.y -= 3;

        boundRect.width += 6;
        boundRect.height += 6;

        if (boundRect.empty() ||
                Math.abs(boundRect.width * Configs.Camera.CompressionCoef) < 1.1d ||
                Math.abs(boundRect.height * Configs.Camera.CompressionCoef) < 1.1d ||
                boundRect.x <= 0 || boundRect.y <= 0 || boundRect.x + boundRect.width >= frame.width() || boundRect.y + boundRect.height >= frame.height() ||
                boundRect.area() <= 180d)
            return null;

        Mat maybeBluePuck = frame.submat(boundRect);

        cvtColor(maybeBluePuck, maybeBluePuck, COLOR_BGR2RGB);

        resize(maybeBluePuck, maybeBluePuck, new Size(maybeBluePuck.width() * Configs.Camera.CompressionCoef, maybeBluePuck.height() * Configs.Camera.CompressionCoef));

        if(maybeBluePuck.size().width >= template.get().size().width || maybeBluePuck.size().height >= template.get().size().height)
            return null;

        Mat res = new Mat();

        matchTemplate(maybeBluePuck, template.get(), res, COLOR_BGR2RGB);

        double per = res.get(0, 0)[0];

        if (per > minSimilarity && per < maxSimilarity)
            return boundRect;

        return null;
    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(LastFrame.get()));
    }
}
