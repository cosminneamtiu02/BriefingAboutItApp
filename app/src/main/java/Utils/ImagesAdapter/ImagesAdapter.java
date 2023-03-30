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

    private OnClickListener onClickListener;

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
        return new RecyclerViewHolder(view) ;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getTextView().setText("  " + images.get(position).getImageName());

        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position);
            }
        });

    }

    public interface OnClickListener {
        void onClick(int position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }
}
