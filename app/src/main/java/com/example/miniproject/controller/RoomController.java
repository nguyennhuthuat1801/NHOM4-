package com.example.miniproject.controller;

import com.example.miniproject.model.Room;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller quản lý CRUD phòng trọ.
 * Dữ liệu lưu tạm thời trong bộ nhớ (List) theo yêu cầu bài.
 */
public class RoomController {

    private static RoomController instance;

    private final List<Room> rooms = new ArrayList<>();

    private RoomController() {
        seedSampleDataIfNeeded();
    }

    public static synchronized RoomController getInstance() {
        if (instance == null) {
            instance = new RoomController();
        }
        return instance;
    }

    private void seedSampleDataIfNeeded() {
        if (!rooms.isEmpty()) return;
        rooms.add(new Room(
                "P101",
                "Phòng 101",
                1500000d,
                Room.Status.EMPTY,
                "",
                ""
        ));
        rooms.add(new Room(
                "P102",
                "Phòng 102",
                2000000d,
                Room.Status.RENTED,
                "Nguyễn Văn A",
                "0901234567"
        ));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(String roomId) {
        if (roomId == null) return null;
        for (Room room : rooms) {
            if (roomId.equals(room.getRoomId())) return room;
        }
        return null;
    }

    public boolean createRoom(Room room) {
        if (room == null || room.getRoomId() == null) return false;
        if (getRoomById(room.getRoomId()) != null) {
            return false;
        }
        rooms.add(room);
        return true;
    }

    public boolean updateRoom(String roomId, Room updated) {
        Room existing = getRoomById(roomId);
        if (existing == null || updated == null) return false;

        // Mã phòng là key nên giữ nguyên.
        existing.setRoomName(updated.getRoomName());
        existing.setRentPrice(updated.getRentPrice());
        existing.setStatus(updated.getStatus());
        existing.setTenantName(updated.getTenantName());
        existing.setPhone(updated.getPhone());
        return true;
    }

    public boolean deleteRoom(String roomId) {
        if (roomId == null) return false;
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (roomId.equals(r.getRoomId())) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}

