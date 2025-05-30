package org.example.blog.service;

import org.example.blog.domain.ResponseResult;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult upload(MultipartFile img);
}
