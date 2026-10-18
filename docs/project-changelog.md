# Nhật ký thay đổi

## 2026-10-18

- Khởi tạo dashboard phân tích mạng xã hội bằng Spring Boot 3.5.16 và Java 21.
- Thêm PostgreSQL/Flyway, pgAdmin, Artemis và Docker health checks.
- Thêm REST CRUD, Excel import/export, dashboard Chart.js và STOMP/WebSocket.
- Thêm scheduler metric, provider mock, JMS import event và SOAP exchange-rate endpoint.
- Thêm CSRF cookie flow cho thao tác ghi từ giao diện, OpenAPI và health endpoint.
- Bổ sung kiểm thử unit cho post service và Excel reflection mapper.
- Giữ Flyway ở trạng thái khởi tạo một file duy nhất: `V1__create_core_schema.sql`.
- Refactor package Java về Layered Architecture và thêm Lombok cho constructor injection/logging.
- Chuyển toàn bộ JPA entity sang Lombok `@Getter` và `@NoArgsConstructor(PROTECTED)`, chỉ giữ setter cần thiết cho cập nhật entity.
- Thêm migration V2 và aggregate service dựa trên metric mới nhất của từng post.
- Thêm kiểm tra file `.xlsx`, kích thước và số dòng trước khi import.
- Bổ sung cấu hình OAuth2 tùy chọn, SOAP exchange-rate client và dashboard refresh button.
- Thêm các sơ đồ deployment, ERD và import flow ở dạng editable Draw.io.
