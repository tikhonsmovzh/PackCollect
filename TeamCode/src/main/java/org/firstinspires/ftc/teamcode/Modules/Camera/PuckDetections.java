package org.firstinspires.ftc.teamcode.Modules.Camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PuckDetections implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frm, long captureTimeNanos) {
        try {
            Mat frame = frm.clone();

            blur(frame, frame, new Size(10, 10));

            MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
            MatOfPoint2f approxCurve = new MatOfPoint2f();

            List<MatOfPoint> contours = new ArrayList<>();

            findContours(frame, contours, new Mat(), 100, 300);

            for (MatOfPoint i : contours) {
                matOfPoint2f.fromList(i.toList());
                approxPolyDP(matOfPoint2f, approxCurve, arcLength(matOfPoint2f, true) * 0.02, true);
                long total = approxCurve.total();
                if (total >= 4 && total <= 6) {
                    boolean isRect = total == 4;

                    if (isRect) {
                        Imgproc.rectangle(frame, new Rect(new Point(0, 0), new Point(50, 50)), new Scalar(100, 100, 100));
                    }
                }
            }

            Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);//выводим картинку в дашборд
            Utils.matToBitmap(frame, b);
            LastFrame.set(b);
        }
        catch (Exception e){

        }

        return frm;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(LastFrame.get()));
    }
}
