package com.example.miniproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.Toast;

import com.example.miniproject.controller.RoomController;
import com.example.miniproject.model.Room;
import com.example.miniproject.ui.RoomAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private RoomController controller;

    private ActivityResultLauncher<Intent> roomFormLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = RoomController.getInstance();

        rvRooms = findViewById(R.id.rvRooms);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RoomAdapter(this, controller.getAllRooms(), new RoomAdapter.OnRoomActionListener() {
            @Override
            public void onEditRoom(Room room) {
                launchCreateOrEdit(false, room);
            }

            @Override
            public void onRequestDelete(Room room) {
                showDeleteConfirm(room);
            }
        });
        rvRooms.setAdapter(adapter);

        roomFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        refreshRooms();
                    }
                }
        );

        findViewById(R.id.fabAddRoom).setOnClickListener(v -> launchCreateOrEdit(true, null));
    }

    private void launchCreateOrEdit(boolean isCreate, Room room) {
        Intent intent = new Intent(this, RoomFormActivity.class);
        intent.putExtra(RoomFormActivity.EXTRA_MODE_IS_CREATE, isCreate);
        if (room != null) {
            intent.putExtra(RoomFormActivity.EXTRA_ROOM_ID, room.getRoomId());
        }
        roomFormLauncher.launch(intent);
    }

    private void refreshRooms() {
        List<Room> newRooms = controller.getAllRooms();
        adapter.setRooms(newRooms);
    }

    private void showDeleteConfirm(Room room) {
        if (room == null) return;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_title))
                .setMessage(getString(R.string.confirm_delete_message))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    boolean deleted = controller.deleteRoom(room.getRoomId());
                    if (!deleted) {
                        Toast.makeText(this, R.string.toast_room_not_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    refreshRooms();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}