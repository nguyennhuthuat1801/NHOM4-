package com.example.miniproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject.controller.RoomController;
import com.example.miniproject.model.Room;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RoomFormActivity extends AppCompatActivity {

    public static final String EXTRA_MODE_IS_CREATE = "extra_mode_is_create";
    public static final String EXTRA_ROOM_ID = "extra_room_id";

    private TextInputLayout tilRoomCode;
    private TextInputEditText etRoomCode;
    private TextInputLayout tilRoomName;
    private TextInputEditText etRoomName;
    private TextInputLayout tilRentPrice;
    private TextInputEditText etRentPrice;
    private TextInputLayout tilStatus;
    private Spinner spStatus;
    private TextInputLayout tilTenantName;
    private TextInputEditText etTenantName;
    private TextInputLayout tilPhone;
    private TextInputEditText etPhone;

    private Button btnCancel;
    private Button btnSave;

    private RoomController controller;

    private boolean isCreate;
    private String originalRoomId; // key giữ nguyên khi update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_form);

        controller = RoomController.getInstance();

        initViews();
        initStatusSpinner();
        readIntentAndPrefill();

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnSave.setOnClickListener(v -> onSave());

        // Add listener to show/hide tenant fields based on status
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0: EMPTY, position 1: RENTED (based on status_array)
                boolean isRented = (position == 1);
                toggleTenantFields(isRented);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initViews() {
        tilRoomCode = findViewById(R.id.tilRoomCode);
        etRoomCode = findViewById(R.id.etRoomCode);

        tilRoomName = findViewById(R.id.tilRoomName);
        etRoomName = findViewById(R.id.etRoomName);

        tilRentPrice = findViewById(R.id.tilRentPrice);
        etRentPrice = findViewById(R.id.etRentPrice);

        tilStatus = findViewById(R.id.tilStatus);
        spStatus = findViewById(R.id.spStatus);

        tilTenantName = findViewById(R.id.tilTenantName);
        etTenantName = findViewById(R.id.etTenantName);

        tilPhone = findViewById(R.id.tilPhone);
        etPhone = findViewById(R.id.etPhone);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
    }

    private void toggleTenantFields(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        tilTenantName.setVisibility(visibility);
        tilPhone.setVisibility(visibility);
        
        if (!show) {
            etTenantName.setText("");
            etPhone.setText("");
            tilTenantName.setError(null);
            tilPhone.setError(null);
        }
    }

    private void readIntentAndPrefill() {
        isCreate = getIntent().getBooleanExtra(EXTRA_MODE_IS_CREATE, true);
        originalRoomId = getIntent().getStringExtra(EXTRA_ROOM_ID);

        if (isCreate) {
            setTitle(getString(R.string.add_room));
            // Default is EMPTY (0), hide fields
            toggleTenantFields(false);
            return;
        }

        setTitle(getString(R.string.edit_room));

        if (TextUtils.isEmpty(originalRoomId)) {
            Toast.makeText(this, R.string.toast_room_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Room room = controller.getRoomById(originalRoomId);
        if (room == null) {
            Toast.makeText(this, R.string.toast_room_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etRoomCode.setText(room.getRoomId());
        etRoomCode.setEnabled(false);
        tilRoomCode.setEnabled(false);

        etRoomName.setText(room.getRoomName());
        etRentPrice.setText(String.valueOf(room.getRentPrice()));
        etTenantName.setText(room.getTenantName());
        etPhone.setText(room.getPhone());

        boolean isRented = room.getStatus() == Room.Status.RENTED;
        spStatus.setSelection(isRented ? 1 : 0);
        toggleTenantFields(isRented);
    }

    private void onSave() {
        // Clear previous errors.
        tilRoomCode.setError(null);
        tilRoomName.setError(null);
        tilRentPrice.setError(null);
        tilStatus.setError(null);
        tilTenantName.setError(null);
        tilPhone.setError(null);

        String roomId = etRoomCode.getText() != null ? etRoomCode.getText().toString().trim() : "";
        String roomName = etRoomName.getText() != null ? etRoomName.getText().toString().trim() : "";
        String priceRaw = etRentPrice.getText() != null ? etRentPrice.getText().toString().trim() : "";
        String tenantName = etTenantName.getText() != null ? etTenantName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

        if (TextUtils.isEmpty(roomId)) {
            tilRoomCode.setError(getString(R.string.toast_validation_failed));
            Toast.makeText(this, R.string.toast_validation_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(roomName)) {
            tilRoomName.setError(getString(R.string.toast_validation_failed));
            Toast.makeText(this, R.string.toast_validation_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            // Accept comma as decimal separator if user types it.
            price = Double.parseDouble(priceRaw.replace(',', '.'));
        } catch (Exception e) {
            tilRentPrice.setError(getString(R.string.toast_validation_failed));
            Toast.makeText(this, R.string.toast_validation_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if (price <= 0) {
            tilRentPrice.setError(getString(R.string.toast_validation_failed));
            Toast.makeText(this, R.string.toast_validation_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        Room.Status status = spStatus.getSelectedItemPosition() == 1 ? Room.Status.RENTED : Room.Status.EMPTY;

        if (status == Room.Status.RENTED) {
            if (TextUtils.isEmpty(tenantName)) {
                tilTenantName.setError(getString(R.string.toast_validation_failed));
                Toast.makeText(this, R.string.toast_validation_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            // Validate phone number: 10 digits starting with 0.
            if (!phone.matches("^0[0-9]{9}$")) {
                tilPhone.setError(getString(R.string.error_invalid_phone));
                Toast.makeText(this, R.string.error_invalid_phone, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Clear tenant fields if room is empty.
            tenantName = "";
            phone = "";
        }

        Room newRoom = new Room(roomId, roomName, price, status, tenantName, phone);

        if (isCreate) {
            boolean ok = controller.createRoom(newRoom);
            if (!ok) {
                Toast.makeText(this, R.string.toast_duplicate_room_id, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            boolean ok = controller.updateRoom(originalRoomId, newRoom);
            if (!ok) {
                Toast.makeText(this, R.string.toast_room_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        setResult(RESULT_OK);
        finish();
    }
}
