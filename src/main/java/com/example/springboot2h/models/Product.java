package com.example.springboot2h.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tblProduct")
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO) // tương ứng đoạn code @SequenceGenerator, @GeneratedValue
    //You can also use "sequence": --> tạo ra 1 cái role để tạo ra một số quy tắc khi: thêm, sửa ...
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    //validate = constraint: ràng buộc:
    @Column(nullable = false, unique = true, length = 300)
    private String productName;
    private Integer year;
    private Double price;
    private String url;

    //caculated field = transient --> trường được tính từ các trường khác, không được lưu vào csdl
    @Transient
    private int age; //age is caculated from year
    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }

    //function
    public Product(String productName, Integer year, Double price, String url) {
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }
}
