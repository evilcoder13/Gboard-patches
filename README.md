<h1 align="center">Gboard Patches</h1>

<p align="center">
  Morphe patches for Gboard focused on core productivity, English &amp; Vietnamese input, and BlackBerry-style flick-up word completion.
</p>

<p align="center">
  <a href="https://github.com/evilcoder13/Gboard-patches/releases/latest"><img alt="Latest release" src="https://img.shields.io/github/v/release/evilcoder13/Gboard-patches?display_name=tag&label=Release&style=for-the-badge"></a>
  <a href="https://github.com/evilcoder13/Gboard-patches"><img alt="Total downloads" src="https://img.shields.io/github/downloads/evilcoder13/Gboard-patches/total?label=Downloads&style=for-the-badge"></a>
  <a href="https://morphe.software/add-source?github=evilcoder13/Gboard-patches"><img alt="Add to Morphe" src="https://img.shields.io/badge/Morphe-Add%20Source-00A8FF?style=for-the-badge"></a>
  <a href="https://github.com/evilcoder13/Gboard-patches"><img alt="GitHub stars" src="https://img.shields.io/github/stars/evilcoder13/Gboard-patches?style=social"></a>
</p>

## Overview

**Gboard Patches** là bộ patch Morphe tinh giản dành riêng cho người dùng gõ tiếng Việt và tiếng Anh:
- **Tập trung cốt lõi**: Đã loại bỏ các layout/bộ gõ không cần thiết (Zhuyin, Bopomofo, OCR tiếng Trung/Nhật/Hàn,...), giữ ứng dụng nhẹ và mượt mà.
- **Tính năng độc quyền Flick-Up Word Completion (phong cách BlackBerry)**: Vuốt phím lên để chọn từ gợi ý ngay lập tức mà không cần với tay lên thanh gợi ý (Suggestion Bar).
- **Hỗ trợ đầy đủ**: Bàn phím tách (Split Keyboard), Clipboards nâng cao, thanh công cụ biên tập văn bản (Select All, Cut, Copy, Paste), Advanced Voice Typing...

> **Lưu ý về cử chỉ (Gesture)**: 
> Tính năng *Flick-Up Word Completion* nhận diện cử chỉ vuốt nhanh hướng lên trên thân bàn phím (chia theo 3 vùng Trái - Giữa - Phải tương ứng với 3 từ gợi ý trên Candidates Bar). Nếu bạn hay bị lẫn với thao tác lướt nối chữ (*Glide Typing* của Gboard), bạn có thể chủ động tắt tính năng **Lướt phím để nhập (Glide Typing)** trong phần cài đặt chính của Gboard: `Cài đặt Gboard > Lướt phím để nhập (Glide typing) > Tắt`.

---

## Included Patches (21 Patches)

### Tính năng được phát triển thêm (Project-Built Features)

<details>
  <summary><code>Flick-Up Word Completion</code></summary>

  Vuốt lên trên vùng Trái / Giữa / Phải của thân bàn phím để chọn nhanh từ gợi ý tương ứng ở thanh ứng cử viên (tương tự trải nghiệm bàn phím BlackBerry 10 / KEYone).
</details>

<details>
  <summary><code>Clipboard Enhancements</code></summary>

  Tùy chỉnh thời gian lưu trữ clipboard, số lượng mục tối đa, số dòng xem trước, nhãn đếm ngược thời gian tạo, chỉ mục sắp xếp, số cột dạng lưới và giới hạn số ký tự hiển thị.
</details>

<details>
  <summary><code>Long-Press Editing Shortcuts</code></summary>

  Thêm các phím tắt nhấn giữ: Chọn tất cả (Select all), Hoàn tác (Undo), Sao chép (Copy), Cắt (Cut), Dán (Paste), Làm lại (Redo) trên bàn phím chữ cái (tiếng Việt & tiếng Anh).
</details>

<details>
  <summary><code>Toolbar Editing Buttons</code></summary>

  Thêm các nút Chọn tất cả, Sao chép, Cắt và Dán vào menu thanh công cụ. Có thể kéo thả lên toolbar trên cùng để thao tác văn bản nhanh.
</details>

<details>
  <summary><code>Incognito Mode Toggle</code></summary>

  Thêm nút bật/tắt chế độ Ẩn danh (Incognito) ngay trên thanh công cụ Access Point.
</details>

<details>
  <summary><code>Swipe Down to Dismiss Keyboard</code></summary>

  Vuốt xuống từ thanh gợi ý hoặc thanh công cụ để đóng bàn phím nhanh chóng.
</details>

<details>
  <summary><code>Latin Globe Key Ignore Interval</code></summary>

  Tùy chỉnh khoảng thời gian bỏ qua phím địa cầu (Globe key) sau khi gõ phím để tránh chạm nhầm đổi ngôn ngữ.
</details>

<details>
  <summary><code>Backup &amp; Restore</code></summary>

  Sao lưu và khôi phục toàn bộ cài đặt patch ra file JSON hoặc các cờ Flag PB/XML của Gboard.
</details>

### Mở khóa tính năng ẩn của Gboard (Gboard Feature Unlocks)

<details>
  <summary><code>Advanced Voice Typing</code></summary>

  Mở khóa tính năng Nhập liệu bằng giọng nói nâng cao (Advanced Voice Typing) với tính năng tự động thêm dấu chấm câu.
</details>

<details>
  <summary><code>Enable OCR / Scan Text</code></summary>

  Mở khóa tính năng Quét văn bản (OCR / Scan Text) hỗ trợ ký tự Latinh (tiếng Việt và tiếng Anh).
</details>

<details>
  <summary><code>English QWERTY Up-Flick Uppercase</code></summary>

  Vuốt lên trên bàn phím QWERTY tiếng Anh để viết hoa ký tự.
</details>

<details>
  <summary><code>Enable Inline Autofill Suggestions</code></summary>

  Bật hiển thị gợi ý tự động điền (Autofill) nội dòng trong các trường nhập liệu tương thích.
</details>

<details>
  <summary><code>Grammar Checker</code></summary>

  Mở khóa tính năng Kiểm tra ngữ pháp (Grammar check) trong menu *Sửa văn bản*.
</details>

<details>
  <summary><code>Inline Suggestions (Smart Compose)</code></summary>

  Bật gợi ý thông minh Smart Compose nội dòng.
</details>

<details>
  <summary><code>Use Bluetooth Microphone</code></summary>

  Cho phép sử dụng micrô tai nghe Bluetooth khi nhập liệu bằng giọng nói.
</details>

<details>
  <summary><code>Enable cursor trackpad mode</code></summary>

  Mở khóa chế độ rê con trỏ (trackpad) bằng cách nhấn giữ và lướt trên phím cách (Spacebar).
</details>

<details>
  <summary><code>Enable split keyboard</code></summary>

  Kích hoạt giao diện bàn phím chia đôi (Split keyboard) cho máy tính bảng hoặc màn hình gập.
</details>

<details>
  <summary><code>Close Proactive Suggestions</code></summary>

  Hiển thị nút đóng thanh gợi ý chủ động.
</details>

<details>
  <summary><code>Clipboard Custom Character Limit</code></summary>

  Cho phép tùy chỉnh độ dài tối đa của văn bản lưu trong clipboard.
</details>

<details>
  <summary><code>Package Rename</code></summary>

  Đổi package name để cài đặt song song với bản Gboard gốc.
</details>

---

## Cài đặt vào Morphe Manager

Thêm nguồn repository này trực tiếp vào ứng dụng **Morphe Manager**:

- **Nhấn mở trực tiếp**: [Thêm nguồn vào Morphe](https://morphe.software/add-source?github=evilcoder13/Gboard-patches)
- **Hoặc thêm thủ công URL**: `https://github.com/evilcoder13/Gboard-patches`

---

## Tự Build cục bộ (Local Build)

Cần cấu hình GitHub Packages token (`read:packages`) vào `~/.gradle/gradle.properties`:
```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

Lệnh build bundle patch cho Android:

```powershell
.\gradlew.bat :patches:buildAndroid
```

Tạo lại metadata danh sách patch:

```powershell
.\gradlew.bat generatePatchesList
```

File bundle `.mpp` sẽ được tạo tại `patches/build/libs/patches-*.mpp`.

---

## License

Phát hành theo giấy phép [GNU General Public License v3.0](LICENSE).
