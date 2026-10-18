# Social Analytics Dashboard

Dashboard Spring Boot cho dữ liệu bài đăng và chỉ số mạng xã hội. Bản hiện tại chạy với PostgreSQL, pgAdmin, ActiveMQ Artemis, Flyway, Chart.js, STOMP/WebSocket và Apache POI.

Mã Java sử dụng Layered Architecture: `controller` nhận request, `service` điều phối use case, `repository` truy cập dữ liệu, `entity` mô hình hóa persistence, `dto` định nghĩa hợp đồng API, `exception` chuẩn hóa lỗi và `config` chứa cấu hình Spring. Các adapter ngoài nằm trong `integration`, `messaging` và `scheduler`.

Lombok được dùng có kiểm soát cho constructor injection và logging, trong khi JPA entity vẫn giữ constructor nghiệp vụ và getter/setter cần thiết cho Hibernate.

## Chạy bằng Docker

```bash
cp .env.example .env
docker compose up -d --build
```

Các cổng mặc định:

- Dashboard/API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- pgAdmin: http://localhost:5050
- Artemis console: http://localhost:8161

Tài khoản mặc định chỉ dành cho môi trường local: `admin` / `change-me-admin`. Hãy đổi các giá trị trong `.env` trước khi dùng ngoài máy cá nhân.

## Kiểm thử

```bash
./gradlew test --no-daemon
```

PostgreSQL và Artemis phải đang chạy khi khởi động test tích hợp Spring context. Dữ liệu PostgreSQL được giữ trong volume Docker `postgres-data`.

## Các điểm tích hợp

- REST: `/api/posts`, `/api/posts/{postId}/metrics`, `/api/chart-data`, `/api/summary`.
- Nhập/xuất Excel: `POST /api/import-posts`, `GET /api/export-report`.
- WebSocket STOMP: `/ws`, topic `/topic/metrics`.
- SOAP WSDL: `/ws/exchange-rate.wsdl`.
- OAuth2 Facebook/Twitter được bật khi có client ID tương ứng; không có thông tin nhà cung cấp thì đăng nhập local vẫn hoạt động.

## Sơ đồ hệ thống

Các sơ đồ editable nằm trong [docs/diagrams](docs/diagrams/):

- [architecture.drawio](docs/diagrams/architecture.drawio) — các tầng ứng dụng và hướng dữ liệu.
- [deployment.drawio](docs/diagrams/deployment.drawio) — các service Docker và volume.
- [data-model.drawio](docs/diagrams/data-model.drawio) — ERD từ Flyway schema.
- [import-flow.drawio](docs/diagrams/import-flow.drawio) — luồng upload Excel, JMS và refresh dashboard.

PNG cùng tên dùng để xem nhanh trong trình duyệt hoặc review pull request.

OAuth2 tùy chọn dùng các biến `FACEBOOK_CLIENT_ID`, `FACEBOOK_CLIENT_SECRET`, `TWITTER_CLIENT_ID` và `TWITTER_CLIENT_SECRET`. Để trống các biến này thì app dùng local login.
