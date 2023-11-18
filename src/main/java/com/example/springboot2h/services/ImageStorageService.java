package com.example.springboot2h.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService{
    //tham chiếu đến thư mục để upload ảnh nằm trong cùng thư muc chua project, neu chua co se duoc tao ra
    private final Path storageFolder = Paths.get("uploads");

    //constructor
    public ImageStorageService(){
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }

    //kiem tra co phai file image
    private boolean isImageFile(MultipartFile file) {
        //let install FileNameUtils: lay ra file Extension -> de lay ra cau hinh file do la gi
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        //chi cho phep 1 so duoi file sau la file image
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase()); //kiem tra duoi file do co nam trong nhung duoi file kia khong
    }

    //nhan file tu request, vaf kiem tra xem file do co trong khong
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("haha");
            if(file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            //check file is image?
            if(!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file.");
            }
            //file must be <= 5MB
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if(fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be < 5Mb.");
            }
            //file must be rename, why? de tranh ghi de len nhau khi upload file trung ten
            //Copy code
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve( Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals (this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) { //copy file vao destinationFilePath, REPLACE_EXISTING: neu da ton tai thi thay the
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return generatedFileName;

        } catch (IOException exception) {
            throw new RuntimeException("Failed to store file.", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //list all file instorageFolder: chi duyet cac file image been trong, con cac thu muc con khac khong duyet
            return Files.walk (this.storageFolder, 1) //maxDepth = 1 --> chỉ duyệt thằng con gần nhất
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        //Code copy
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
else {
                throw new RuntimeException( "Could not read file: " + fileName);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
