package Utils.ImagesAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.R;

import java.util.List;

import Entities.Image;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final List<Image> images;

    public ImagesAdapter(List<Image> images) {
        this.images = images;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.image_list_view;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getTextView().setText(String.valueOf(images.get(position).getImageName()));
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }
}
