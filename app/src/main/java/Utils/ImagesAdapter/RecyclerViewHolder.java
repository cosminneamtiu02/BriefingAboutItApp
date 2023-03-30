package Utils.ImagesAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.randomText);
    }

    public TextView getTextView(){
        return textView;
    }

}