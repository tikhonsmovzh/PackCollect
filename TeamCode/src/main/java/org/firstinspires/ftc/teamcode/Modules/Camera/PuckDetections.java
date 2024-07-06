package org.firstinspires.ftc.teamcode.Modules.Camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;

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
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.sumElems;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.*;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class PuckDetections implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
    public AtomicReference<Integer> RedCount = new AtomicReference<>(0), BlueCount = new AtomicReference<>(0);
    public AtomicReference<Vector2> BlueConcentrationPos = new AtomicReference<>(new Vector2()), RedConcentrationPos = new AtomicReference<>(new Vector2());

    private Mat img;
    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        //StaticTelemetry.AddLine(getClass().getResourceAsStream("red_puck.bmp").toString());
        StaticTelemetry.AddLine(this.getClass().getResource("red_puck.bmp").toString());
        //img = imread(getClass().getResource("red_puck.bmp").toString());
    }

    @Override
    public Object processFrame(Mat frm, long captureTimeNanos) {
        Mat frame = frm.clone();

        Bitmap b = Bitmap.createBitmap(img.width(), img.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(img, b);
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
