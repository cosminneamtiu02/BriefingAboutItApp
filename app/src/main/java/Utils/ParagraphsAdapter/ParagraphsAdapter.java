package Utils.ParagraphsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.R;

import java.util.List;

import Entities.Paragraph;
import Utils.ImagesAdapter.RecyclerViewHolder;

public class ParagraphsAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<Paragraph> paragraphs;

    private Utils.ImagesAdapter.ImagesAdapter.OnClickListener onClickListener;

    public ParagraphsAdapter(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
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
        holder.getTextView().setText("  " + paragraphs.get(position).getParagraphDescription());

        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position);
            }
        });

    }

    public interface OnClickListener {
        void onClick(int position);
    }
    public void setOnClickListener(Utils.ImagesAdapter.ImagesAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return this.paragraphs.size();
    }
}

