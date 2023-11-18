/*
Inject cái service vào Controller
 */
package com.example.springboot2h.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAll(); //load all file inside a folder --> load all các file trong thư mục chứa ảnh
    public byte[] readFileContent(String fileName); //request gửi lên server, server trả về mảng cac byte ta mới xem được ảnh
    public void deleteAllFiles();
}
