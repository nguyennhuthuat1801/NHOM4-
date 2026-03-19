ĐỀ TÀI: XÂY DỰNG ỨNG DỤNG QUẢN LÝ NHÀ TRỌ (ROOM RENTAL MANAGEMENT APP)
Mô tả bài toán
Xây dựng ứng dụng Android giúp chủ nhà trọ quản lý danh sách phòng trọ, thông tin người thuê và tình trạng phòng. (có team work lưu dự án trên github hoặc gitlab)
Ứng dụng:
Áp dụng mô hình MVC (hoặc 1 mô hình khác)
Hiển thị danh sách bằng RecyclerView
Thực hiện đầy đủ CRUD
Dữ liệu chỉ lưu tạm thời bằng List (không dùng SQLite, Room Database …)
Chức năng yêu cầu
a. Quản lý phòng trọ
Gợi ý mỗi phòng trọ có thể gồm:
Mã phòng (String)
Tên phòng (String)
Giá thuê (double)
Tình trạng (Còn trống / Đã thuê)
Tên người thuê (String)
Số điện thoại (String)
….
b. Các chức năng CRUD
Create – Thêm phòng
Nhập thông tin phòng
Validate dữ liệu
Thêm vào List
Cập nhật RecyclerView
Read – Hiển thị danh sách phòng
Dùng RecyclerView
Hiển thị:
Tên phòng
Giá thuê
Tình trạng
Có thể tô màu:
Xanh → Còn trống
Đỏ → Đã thuê
Update – Sửa thông tin phòng
Click vào item → mở màn hình sửa
Cập nhật lại List
Delete – Xóa phòng
Nhấn giữ hoặc nút xóa
Hiển thị AlertDialog xác nhận
Xóa khỏi List
Cập nhật lại RecyclerView







