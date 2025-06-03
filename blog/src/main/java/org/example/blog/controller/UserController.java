package org.example.blog.controller;

import org.example.blog.annotation.SystemLog;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;
import org.example.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/userInfo")
    @ResponseBody
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @ResponseBody
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
