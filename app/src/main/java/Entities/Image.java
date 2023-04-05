package Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Image{
    private final String id;
    private final String photo;
    private final String imageName;
    private final String blurredImage;
    private final boolean toBlur;
    private ArrayList<Face> faces;

    public Image(String id, String imageName, String photo, String blurredImage, boolean toBlur, ArrayList<Face> faces) {
        this.id = id;
        this.imageName = imageName;
        this.photo = photo;
        this.blurredImage = blurredImage;
        this.toBlur = toBlur;
        this.faces = faces;
    }

    public Image(String id, String imageName, Bitmap photo, String blurredImage, boolean toBlur, ArrayList<Face> faces) {
        this.id = id;
        this.imageName = imageName;
        this.photo = convertBitmapToString(photo);
        this.blurredImage = blurredImage;
        this.toBlur = toBlur;
        this.faces = faces;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public String isBlurredImage() {
        return blurredImage;
    }

    public boolean isToBlur() {
        return toBlur;
    }

    public Bitmap getPhoto() {
        return convertStringToBitmap(photo);
    }

    public String getImageName() {
        return imageName;
    }

    public String getId() {
        return id;
    }

    private static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
    }
}
