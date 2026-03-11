package com.example.nhom6.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom6.R;
import com.example.nhom6.model.Room;

public class EditRoom extends AppCompatActivity {

    private EditText etRoomId, etRoomName, etRoomPrice, etTenantName, etPhoneNumber;
    private CheckBox cbIsRented;
    private LinearLayout layoutTenantInfo;
    private Button btnSave;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_room);

        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etRoomPrice = findViewById(R.id.etRoomPrice);
        cbIsRented = findViewById(R.id.cbIsRented);
        etTenantName = findViewById(R.id.etTenantName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        layoutTenantInfo = findViewById(R.id.layoutTenantInfo);
        btnSave = findViewById(R.id.btnSave);

        Room room = (Room) getIntent().getSerializableExtra("room");
        position = getIntent().getIntExtra("position", -1);

        if (room != null) {
            etRoomId.setText(room.getId());
            etRoomName.setText(room.getName());
            etRoomPrice.setText(String.valueOf(room.getPrice()));
            cbIsRented.setChecked(room.isRented());
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());

            layoutTenantInfo.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
        }

        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutTenantInfo.setVisibility(View.VISIBLE);
            } else {
                layoutTenantInfo.setVisibility(View.GONE);
                etTenantName.setText("");
                etPhoneNumber.setText("");
            }
        });

        btnSave.setOnClickListener(v -> {
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            boolean isRented = cbIsRented.isChecked();
            String tenantName = isRented ? etTenantName.getText().toString().trim() : "";
            String phoneNumber = isRented ? etPhoneNumber.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isRented && (TextUtils.isEmpty(tenantName) || TextUtils.isEmpty(phoneNumber))) {
                Toast.makeText(this, "Vui lòng nhập thông tin người thuê", Toast.LENGTH_SHORT).show();
                return;
            }

            Room updatedRoom = new Room(etRoomId.getText().toString(), name, Double.parseDouble(priceStr), isRented, tenantName, phoneNumber);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("room", updatedRoom);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}