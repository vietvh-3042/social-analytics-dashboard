# Lộ trình phát triển

## Đã hoàn thành — nền tảng MVP

- Docker Compose cho app, PostgreSQL, pgAdmin và Artemis.
- Flyway schema cho user, social account, post, metric và import batch.
- Dashboard responsive với KPI, biểu đồ, trạng thái tải/lỗi/rỗng và cập nhật STOMP.
- CRUD posts/metrics, nhập và xuất Excel, xử lý dòng lỗi và chống trùng external ID.
- Job metric bất đồng bộ, provider port, JMS import event, SOAP WSDL và OpenAPI.
- Bảo mật local admin, CSRF, health endpoint và secret-safe environment template.
- Aggregate metrics theo ngày, latest-metric KPI, validate file Excel và SOAP client adapter.
- Sơ đồ deployment, data model và import flow dạng `.drawio` có thể chỉnh sửa.

## Giai đoạn tiếp theo

1. Kết nối OAuth2 thật cho Facebook/Twitter và lưu social account theo người dùng.
2. Thay mock provider bằng adapter API thật với giới hạn tốc độ, retry và circuit breaker.
3. Bổ sung Testcontainers cho PostgreSQL/Artemis và kiểm thử trình duyệt cho dashboard.
4. Mở rộng phân quyền theo tenant, audit log và chính sách lưu trữ dữ liệu.
5. Hoàn thiện contract/integration tests cho REST, dashboard, JMS, WebSocket, SOAP và Testcontainers.
