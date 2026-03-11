package com.example.nhom6;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6.controller.AddRoom;
import com.example.nhom6.controller.EditRoom;
import com.example.nhom6.controller.RoomAdapter;
import com.example.nhom6.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private List<Room> filteredList;
    private FloatingActionButton fabAddRoom;
    private EditText etSearch;
    private CheckBox cbFilterVacant;

    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room room = (Room) result.getData().getSerializableExtra("room");
                    roomList.add(room);
                    applyFilters();
                    Toast.makeText(this, "Đã thêm phòng", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private final ActivityResultLauncher<Intent> editRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room updatedRoom = (Room) result.getData().getSerializableExtra("room");
                    for (int i = 0; i < roomList.size(); i++) {
                        if (roomList.get(i).getId().equals(updatedRoom.getId())) {
                            roomList.set(i, updatedRoom);
                            break;
                        }
                    }
                    applyFilters();
                    Toast.makeText(this, "Đã cập nhật phòng", Toast.LENGTH_SHORT).show();
                }
            }
    );

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

        roomList = new ArrayList<>();
        filteredList = new ArrayList<>();

        roomList.add(new Room("101", "Phòng 101", 1500000, false, "", ""));
        roomList.add(new Room("102", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0123456789"));
        roomList.add(new Room("103", "Phòng 103", 1800000, false, "", ""));

        filteredList.addAll(roomList);

        etSearch = findViewById(R.id.etSearch);
        cbFilterVacant = findViewById(R.id.cbFilterVacant);
        recyclerView = findViewById(R.id.recyclerViewRooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cbFilterVacant.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());

        fabAddRoom = findViewById(R.id.fabAddRoom);
        fabAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRoom.class);
            addRoomLauncher.launch(intent);
        });
    }

    private void applyFilters() {
        String text = etSearch.getText().toString().toLowerCase();
        boolean filterVacant = cbFilterVacant.isChecked();

        filteredList.clear();
        for (Room room : roomList) {
            boolean matchesSearch = room.getName().toLowerCase().contains(text) ||
                    room.getId().toLowerCase().contains(text);

            boolean matchesStatus = !filterVacant || !room.isRented();

            if (matchesSearch && matchesStatus) {
                filteredList.add(room);
            }
        }
        adapter.updateList(filteredList);
    }

    @Override
    public void onRoomClick(Room room, int position) {
        Intent intent = new Intent(MainActivity.this, EditRoom.class);
        intent.putExtra("room", room);
        editRoomLauncher.launch(intent);
    }

    @Override
    public void onRoomLongClick(Room room, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa phòng")
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    roomList.remove(room);
                    applyFilters();
                    Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
