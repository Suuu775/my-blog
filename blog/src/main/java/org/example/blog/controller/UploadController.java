package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult upload(@RequestParam("img")MultipartFile img){
         return uploadService.upload(img);
    }
}
