package org.example.blog.service;

import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;

public interface IAdminLoginService {
    ResponseResult login(User user);
}
