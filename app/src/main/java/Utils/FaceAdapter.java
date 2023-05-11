package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.R;

import java.util.ArrayList;
import java.util.List;

import Entities.Face;

public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.ViewHolder> {

    private final List<Face> faceList;
    private final List<Face> checkedFaceList;
    private final Context context;

    public FaceAdapter(List<Face> faceList, Context context) {
        this.faceList = faceList;
        this.context = context;
        checkedFaceList = new ArrayList<>();
    }

    public List<Face> getCheckedFaceList() {
        return checkedFaceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_face, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Face face = faceList.get(position);

        holder.faceCroppedImage.setImageBitmap(convertStringToBitmap(face.getFaceCrop()));

        // Set checkbox status
        holder.faceCheckbox.setChecked(checkedFaceList.contains(face));

        holder.faceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedFaceList.add(face);
            } else {
                checkedFaceList.remove(face);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView faceCroppedImage;
        CheckBox faceCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faceCroppedImage = itemView.findViewById(R.id.face_cropped_image);
            faceCheckbox = itemView.findViewById(R.id.face_checkbox);
        }
    }

    private static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
    }
}