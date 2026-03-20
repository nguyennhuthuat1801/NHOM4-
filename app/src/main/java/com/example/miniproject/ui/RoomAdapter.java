package com.example.miniproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.R;
import com.example.miniproject.model.Room;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    public interface OnRoomActionListener {
        void onEditRoom(@NonNull Room room);

        void onRequestDelete(@NonNull Room room);
    }

    private final Context context;
    private final OnRoomActionListener listener;
    private List<Room> rooms = new ArrayList<>();
    private final DecimalFormat priceFormat = new DecimalFormat("#.##");

    public RoomAdapter(@NonNull Context context, @NonNull List<Room> rooms, @NonNull OnRoomActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.rooms = rooms;
    }

    public void setRooms(@NonNull List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.tvRoomName.setText(room.getRoomName());
        holder.tvPrice.setText(priceFormat.format(room.getRentPrice()));

        if (room.getStatus() == Room.Status.RENTED) {
            holder.tvStatus.setText(context.getString(R.string.status_rented));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rented);
        } else {
            holder.tvStatus.setText(context.getString(R.string.status_empty));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_empty);
        }

        holder.itemView.setOnClickListener(v -> listener.onEditRoom(room));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onRequestDelete(room);
            return true;
        });
        holder.btnDelete.setOnClickListener(v -> listener.onRequestDelete(room));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        final TextView tvRoomName;
        final TextView tvPrice;
        final TextView tvStatus;
        final ImageButton btnDelete;

        RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

