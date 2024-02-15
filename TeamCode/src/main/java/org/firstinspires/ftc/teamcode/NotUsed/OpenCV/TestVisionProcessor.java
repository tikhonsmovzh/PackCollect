package org.firstinspires.ftc.teamcode.NotUsed.OpenCV;

import static org.opencv.core.Core.inRange;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV;
import static org.opencv.imgproc.Imgproc.MORPH_ERODE;
import static org.opencv.imgproc.Imgproc.blur;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.resize;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

public class TestVisionProcessor implements VisionProcessor {

    double x = 640;
    double y = 480;
    double r1 = 5;
    double g1 = 69;
    double b1 = 206;
    double r2 = 60;
    double g2 = 129;
    double b2 = 296;
    Mat img_range_red = new Mat();
    Mat img_range_blue = new Mat();
    public double hRedDown = 4;
    public double cRedDown = 127.7;
    public double vRedDowm = 154.4;
    public double hRedUp = 30;
    public double cRedUp = 255;
    public double vRedUp = 255;

    public double hBlueDown = 85;
    public double cBlueDown = 53.8;
    public double vBlueDowm = 148.8;
    public double hBlueUp = 100;
    public double cBlueUp = 255;
    public double vBlueUp = 255;
    public boolean alyans = false;
    double x1Finish = x * 0.3;
    double x1Start = x * 0;
    double x2Finish = x * 0.6;
    double x2Start = x * 0.3;
    double x3Finish = x;
    double x3Start = x * 0.6;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public int pos = 0;

    public int ksize = 13;
    public boolean team = true;

    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        //

        cvtColor(frame, frame, COLOR_RGB2HSV);//конвертация в хсв
        resize(frame, frame, new Size(x, y));// установка разрешения

        blur(frame, frame, new Size(10, 10));//размытие для компенсации шумов с камеры
        // можно иф для установки цвета команды и только 1 инрейндж
        inRange(frame, new Scalar(hRedDown, cRedDown, vRedDowm), new Scalar(hRedUp, cRedUp, vRedUp), img_range_red);

        //inRange(картинка вход, мин знач хсв, макс знач хсв, выход картинка(трешхолды))
        inRange(frame, new Scalar(hBlueDown, cBlueDown, vBlueDowm), new Scalar(hBlueUp, cBlueUp, vBlueUp), img_range_blue);



        Core.bitwise_or(img_range_red, img_range_blue, frame);//объединяем два инрейнджа

        erode(frame, frame, getStructuringElement(MORPH_ERODE, new Size(ksize, ksize))); // Сжать
        dilate(frame, frame, getStructuringElement(MORPH_ERODE, new Size(ksize, ksize))); // Раздуть

        Rect boundingRect = boundingRect(frame);//boudingRect рисуем прямоугольник

        centerOfRectX = boundingRect.x + boundingRect.width / 2.0;//координаты центра вычисляем
        centerOfRectY = boundingRect.y + boundingRect.height / 2.0;

        if (centerOfRectX < x1Finish && centerOfRectX > x1Start) {
            pos = 1;
        }
        if (centerOfRectX < x2Finish && centerOfRectX > x2Start) {
            pos = 2;
        }
        if (centerOfRectX < x3Finish && centerOfRectX > x3Start) {
            pos = 3;
        }

        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
