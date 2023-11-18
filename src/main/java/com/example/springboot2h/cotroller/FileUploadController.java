package com.example.springboot2h.cotroller;

import com.example.springboot2h.models.ResponseObject;
import com.example.springboot2h.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/FileUpload")
public class FileUploadController {
    //This controller receive file/image from client

    //Inject storage Service here
    @Autowired
    private ImageStorageService storageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            //save a file toầ folder --> use a service
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload file successfully", generatedFileName)
                    //7c05a56eaf734e9c900cdc6c057701ce.jpg --> must have path to open this file in the web Browser
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("ok", e.getMessage(), "")
            );
        }
    }

    //get image's url
    @GetMapping("/files/{fileName:.+}") //.+ la phan duoi file
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        // /files/7c05a56eaf734e9c900cdc6c057701ce.jpg
        try{
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    //load all uploaded file
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> urls = storageService.loadAll()
                    //convert KQ load dc ve dang url request
                    .map(path -> {
                        //goi den request readDetailFile
                        //convert fileName to url (by send requset "readDetailFile")
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("ok", "List files successfully.", urls));
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ResponseObject("failed", "List files failed.", new String[] {})
            );
        }
    }
}
