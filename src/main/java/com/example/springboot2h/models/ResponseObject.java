/*
Định nghĩa một Object để c tẻể show cho người dùng Lỗi hoặc ngoại lệ
Lớp này có thể bọc được các Object khác
 */
package com.example.springboot2h.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseObject {
    private String stattus;
    private String message;
    private Object data; //Các Object khác, cụ thể ở đây là Product


}
