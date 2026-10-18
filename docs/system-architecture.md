# Kiến trúc hệ thống

## Tổng quan

Ứng dụng là một Spring Boot 3.5.16 chạy Java 21 theo Layered Architecture. Thymeleaf phục vụ dashboard, REST phục vụ CRUD và dữ liệu biểu đồ, PostgreSQL lưu dữ liệu chính, Flyway quản lý schema. Docker Compose đóng gói app, PostgreSQL, pgAdmin và ActiveMQ Artemis.

## Cây package

```text
src/main/java/com/learning/social_analytics_dashboard/
├── controller/       # HTTP and MVC entry points
├── service/          # application use cases and transactions
├── repository/       # Spring Data persistence ports
├── entity/           # JPA persistence model
├── dto/
│   ├── request/      # inbound API and import models
│   └── response/     # outbound API models
├── exception/        # API error mapping and domain exceptions
├── config/           # Spring configuration and security
├── integration/      # external SOAP/social adapters
├── messaging/        # JMS and WebSocket adapters
├── scheduler/        # scheduled application triggers
└── common/           # shared enums and value constants
```

## Các lớp

- Presentation: controller REST/Thymeleaf, DTO và xử lý HTTP/CSRF.
- Application: service, transaction, validation, scheduler và orchestration use case.
- Domain: entity, enum và các hợp đồng nghiệp vụ.
- Infrastructure: Spring Data repository, Flyway, Docker, JMS, WebSocket, SOAP và social provider adapter.

Các package hiện được gom theo miền để dễ tìm, nhưng vẫn giữ ranh giới lớp: controller không truy cập repository trực tiếp; service không phụ thuộc vào giao diện; adapter bên ngoài không chứa luật nghiệp vụ.

## Luồng dữ liệu

1. Người dùng mở dashboard; trình duyệt gọi summary, chart-data và danh sách posts.
2. Tạo/cập nhật metric ghi vào `social_metric`, sau đó phát sự kiện STOMP ở `/topic/metrics`.
3. Job định kỳ lấy dữ liệu từ `SocialMetricsProvider` và chạy bất đồng bộ trên executor giới hạn.
4. File Excel đi qua mapper phản chiếu có giới hạn, ghi posts và tạo `import_batch`; sự kiện hoàn tất được gửi qua JMS.
5. OAuth2 là điểm mở rộng tùy cấu hình; local admin dùng mật khẩu băm trong bộ nhớ.

## Thành phần vận hành

- PostgreSQL 16: dữ liệu ứng dụng.
- pgAdmin 8: quản trị PostgreSQL trong môi trường phát triển.
- Artemis 2.37: hàng đợi `import.completed`.
- Actuator: health/readiness ở `/actuator/health`.

Schema khởi tạo mới cố ý chỉ có một file `V1__create_core_schema.sql`; mọi thay đổi schema tiếp theo phải được bàn giao như một migration mới, không sửa V1 sau khi đã phát hành.

Migration `V2__create_metric_aggregate.sql` thêm bảng aggregate theo ngày và nguồn. `MetricAggregateService` dựng lại aggregate từ metric mới nhất của từng post sau khi metric thay đổi hoặc import hoàn tất. Dashboard summary dùng metric mới nhất của từng post để tránh cộng lặp các snapshot lịch sử.

Các adapter mạng xã hội thật chưa gọi API bên ngoài; adapter mock hiện tại là nguồn dữ liệu an toàn cho local và test.

Sơ đồ editable và ảnh render nằm trong [docs/diagrams](diagrams/).
