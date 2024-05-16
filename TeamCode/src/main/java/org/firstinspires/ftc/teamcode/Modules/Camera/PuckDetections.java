package org.firstinspires.ftc.teamcode.Modules.Camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.sumElems;
import static org.opencv.imgproc.Imgproc.*;

import java.util.concurrent.atomic.AtomicReference;

public class PuckDetections implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
    public AtomicReference<Double> RedCount = new AtomicReference<>(0d), BlueCount = new AtomicReference<>(0d);

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frm, long captureTimeNanos) {
        Mat frame = frm.clone();

        blur(frame, frame, new Size(10, 10));

        Mat blueFrame = frame.clone();

        inRange(blueFrame, new Scalar(Configs.Camera.hBlueDown, Configs.Camera.cBlueDown, Configs.Camera.vBlueDowm), new Scalar(Configs.Camera.hBlueUp, Configs.Camera.cBlueUp, Configs.Camera.vBlueUp), blueFrame);

        BlueCount.set(sumElems(blueFrame).val[0]);

        Mat redFrame = frame.clone();

        inRange(redFrame, new Scalar(Configs.Camera.hRedDown, Configs.Camera.cRedDown, Configs.Camera.vRedDowm), new Scalar(Configs.Camera.hRedUp, Configs.Camera.cRedUp, Configs.Camera.vRedUp), redFrame);

        RedCount.set(sumElems(redFrame).val[0]);

        Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);//выводим картинку в дашборд
        Utils.matToBitmap(frame, b);
        LastFrame.set(b);

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
