# Tiêu chuẩn mã nguồn

- Java 21, package theo miền nghiệp vụ, tên file kebab-case cho tài liệu và tên lớp theo chuẩn Java.
- Lombok dùng cho constructor injection và logging (`@RequiredArgsConstructor`, `@Slf4j`); không dùng `@Data` trên JPA entity để tránh sinh setter/`equals` ngoài ý muốn.
- DTO dùng cho biên API; entity không trả trực tiếp từ REST.
- Dữ liệu thay đổi phải qua service có `@Transactional`; controller chỉ điều phối.
- Endpoint ghi dữ liệu yêu cầu xác thực và CSRF. Không đưa mật khẩu, token hay thông tin kết nối thật vào repository.
- Origin WebSocket phải lấy từ cấu hình `APP_ALLOWED_ORIGINS`; không dùng wildcard trong môi trường triển khai.
- Excel chỉ ánh xạ các trường đã khai báo trong lớp đích; không cho phép nạp lớp tùy ý từ workbook.
- Tác vụ nền phải có executor giới hạn, log lỗi theo từng bản ghi và không làm hỏng toàn bộ lượt chạy.
- Giao diện phải có trạng thái tải, rỗng, lỗi; hỗ trợ bàn phím, focus rõ ràng, tương phản đủ và giảm chuyển động.
- Kiểm thử ưu tiên hợp đồng quan sát được ở lớp rẻ nhất: unit trước, context/integration sau.
