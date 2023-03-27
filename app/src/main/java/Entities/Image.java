package Entities;

import android.net.Uri;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class Image{
    private final String id;
    private final String photo;
    private final String imageName;
    private final boolean blurred;
    private final boolean toBlur;

    public Image(String id, String imageName, String photo, boolean blurred, boolean toBlur) {
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
        return Uri.parse(photo);
    }

    public String getImageName() {
        return imageName;
    }

    public String getId() {
        return id;
    }


}
