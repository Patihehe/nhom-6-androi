package com.example.nhom6.controller;

import `android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6.R;
import com.example.nhom6.model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room, int position);
        void onRoomLongClick(Room room, int position);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    public void updateList(List<Room> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName() + " (" + room.getId() + ")");
        holder.tvRoomPrice.setText("Giá: " + String.format("%,.0f VNĐ", room.getPrice()));
        
        if (room.isRented()) {
            holder.tvRoomStatus.setText("Trạng thái: Đã thuê");
            holder.tvRoomStatus.setTextColor(Color.RED);
        } else {
            holder.tvRoomStatus.setText("Trạng thái: Còn trống");
            holder.tvRoomStatus.setTextColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> listener.onRoomClick(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onRoomLongClick(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
        }
    }
}
