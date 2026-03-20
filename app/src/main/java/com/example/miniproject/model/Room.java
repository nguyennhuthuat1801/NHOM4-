package com.example.miniproject.model;

/**
 * Model dữ liệu cho phòng trọ.
 * Dữ liệu được lưu tạm thời trong bộ nhớ (List) theo yêu cầu bài.
 */
public class Room {

    public enum Status {
        EMPTY,
        RENTED
    }

    private final String roomId; // Mã phòng (key)
    private String roomName;     // Tên phòng
    private double rentPrice;    // Giá thuê
    private Status status;       // Còn trống / Đã thuê
    private String tenantName;   // Tên người thuê
    private String phone;        // Số điện thoại

    public Room(String roomId, String roomName, double rentPrice, Status status, String tenantName, String phone) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.rentPrice = rentPrice;
        this.status = status;
        this.tenantName = tenantName;
        this.phone = phone;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

