package com.example.atyourfingertips;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> {
    private List<String> cafes;
    private Context context;

    private OnItemClickListener listener;

    public CafeAdapter(Context context, List<String> cafes) {
        this.context = context;
        this.cafes = cafes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cafe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cafeName = cafes.get(position);
        holder.cafeNameTextView.setText(cafeName);
    }

    @Override
    public int getItemCount() {
        return cafes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cafeNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeNameTextView = itemView.findViewById(R.id.text_cafe_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String cafeName);
    }
}
