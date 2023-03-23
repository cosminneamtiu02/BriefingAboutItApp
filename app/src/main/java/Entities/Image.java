package Entities;

import android.net.Uri;

import java.io.Serializable;

public class Image implements Serializable{
    private final String id;
    private final Uri photo;
    private final String imageName;
    private final boolean blurred;
    private final boolean toBlur;

    public Image(String id, String imageName, Uri photo, boolean blurred, boolean toBlur) {
        this.id = id;
        this.imageName = imageName;
        this.photo = photo;
        this.blurred = blurred;
        this.toBlur = toBlur;
    }

    public boolean isBlurred() {
        return blurred;
    }

    public boolean isToBlur() {
        return toBlur;
    }

    public Uri getPhoto() {
        return photo;
    }

    public String getImageName() {
        return imageName;
    }

    public String getId() {
        return id;
    }
}
