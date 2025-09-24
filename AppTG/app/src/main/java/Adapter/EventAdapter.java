package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptg.R;

import java.util.List;

import item.EventItem;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {
    private List<EventItem> items;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(EventItem item);
    }

    public EventAdapter(Context ctx, List<EventItem> items, OnItemClickListener l) {
        this.context = ctx;
        this.items = items;
        this.listener = l;
    }

    public void setItems(List<EventItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_event_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final EventItem e = items.get(position);
        holder.tvTitle.setText(e.getTitle());
        holder.tvDate.setText(e.getDateIso());
        try {
            holder.viewColor.setBackgroundColor(android.graphics.Color.parseColor(e.getColorHex()));
        } catch (Exception ex) {
            holder.viewColor.setBackgroundColor(android.graphics.Color.GRAY);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(e);
        });
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        View viewColor;
        TextView tvTitle, tvDate;
        VH(@NonNull View itemView) {
            super(itemView);
            viewColor = itemView.findViewById(R.id.view_color);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
