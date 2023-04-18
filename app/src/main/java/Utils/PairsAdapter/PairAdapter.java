package Utils.PairsAdapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.R;

import java.util.List;

import Utils.ImagesAdapter.RecyclerViewHolder;

public class PairAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<Pair<String,String>> pairs;

    private Utils.ImagesAdapter.ImagesAdapter.OnClickListener onClickListener;

    public PairAdapter(List<Pair<String,String>> pairs) {
        this.pairs = pairs;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.pair_list_view;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view) ;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getTextView().setText("  " + pairs.get(position).second);

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
        return this.pairs.size();
    }
}

