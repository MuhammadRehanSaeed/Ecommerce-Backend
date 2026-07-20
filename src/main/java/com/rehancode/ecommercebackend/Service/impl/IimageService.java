package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.Service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class IimageService implements ImageService {

    private final String uploadDirectory = "uploads/products/";
    @Override
    public String uploadImage(MultipartFile file) {


        try {

            File folder = new File(uploadDirectory);

            if(!folder.exists()){
                folder.mkdirs();
            }


            String fileName =
                    System.currentTimeMillis()
                            +"_"
                            +file.getOriginalFilename();


            Path path = Paths.get(uploadDirectory + fileName);


            Files.write(path, file.getBytes());


            return "http://localhost:8080/images/products/" + fileName;


        } catch (IOException e) {

            throw new RuntimeException("Image upload failed");

        }

    }

}

