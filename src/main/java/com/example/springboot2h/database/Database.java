package com.example.springboot2h.database;

import com.example.springboot2h.models.Product;
import com.example.springboot2h.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Now connect with mysql using JPA
/*
docker run -d --rm --name mysql-spring-boot-tutorial \
docker run: bao gồm cả pull image và create container tên là mysql-spring-boot-tutorial
-d = deamon = run container as a background process
--rm: chạy xong nếu tắt container này th xóa luôn, lần sau lai tạo lại nhưng k lo mất data
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=hoangnd \
-e MYSQL_PASSWORD=123456 \
-e MYSQL_DATABASE=test_db \
-p 3309:3306 \ánh xạ giữa cổng trên host port trên PC (3309) và cổng dưới container (3306)
I
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql\ mysql:latest
-> thư mục trên máy tính sẽ map với container để container bị xóa mà volume, data CSDL vẫn còn
mysql -h localhost -P 3309 --protocol=tcp -u hoangnd -p
--> connect với CSDL
 */
@Configuration
public class Database {

    //logger --> thay vì in ra command line mặc định ta c thể in ra log các error, warnning
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    //Tạo database H2
    @Bean //insert 1 số bản ghi xuống database, nếu chưa c bảng thì sẽ tọa bảng
    CommandLineRunner initDatabase(ProductRepository productRepository) {

        return new CommandLineRunner() { //Tạo ra 1 đối tượng để thục thi inter
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Macbook pro 16", 2020, 250.0, "");
                Product productB = new Product("Macbook Air Green", 2021, 200.0, "");
                logger.info("insert data: " + productRepository.save(productA));
                logger.info("insert data: " + productRepository.save(productB));
            }
        };
    }
}
