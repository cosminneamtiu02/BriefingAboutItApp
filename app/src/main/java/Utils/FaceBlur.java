package Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import Entities.Face;

public class FaceBlur {

    public FaceBlur() {
    }

    public static Bitmap blurFaceKMax(String image, List<Face> faces) {

        Bitmap img = convertStringToBitmap(image);

        Bitmap imgCopy = img.copy(Bitmap.Config.ARGB_8888, true);
        if (!faces.isEmpty()) {
            int kMax = 1;
            int h = imgCopy.getHeight();
            int w = imgCopy.getWidth();
            Mat cMask = new Mat(h, w, CvType.CV_8UC1, Scalar.all(0));

            for (Face face : faces) {
                int x1 = (int) face.getXMin();
                int y1 = (int) face.getYMin();
                int x2 = (int) face.getXMax();
                int y2 = (int) face.getYMax();

                int meanXY = Math.round(((x2 - x1) + (y2 - y1)) / 2.0f);
                int kSize = Math.round(meanXY * 0.3f);
                if (kSize > kMax) {
                    kMax = kSize;
                }

                Point center = new Point((x1 + x2) / 2.0, (y1 + y2) / 2.0);
                Size axesLength = new Size((x2 - x1) / 2.0, (y2 - y1) / 2.0);

                Imgproc.ellipse(cMask, center, axesLength, 0, 0, 360, new Scalar(255), -1);
            }

            Mat imgMat = new Mat();
            Utils.bitmapToMat(imgCopy, imgMat);
            Mat mask = new Mat();
            Core.bitwise_and(imgMat, imgMat, mask, cMask);
            Mat imgMask = new Mat();
            Core.subtract(imgMat, mask, imgMask);
            Mat blur = new Mat();
            Imgproc.blur(imgMat, blur, new Size(kMax, kMax));
            Mat mask2 = new Mat();
            Core.bitwise_and(blur, blur, mask2, cMask);
            Core.add(imgMask, mask2, imgMat);

            Utils.matToBitmap(imgMat, imgCopy);
        }

        return imgCopy;
    }


    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
    }

}
