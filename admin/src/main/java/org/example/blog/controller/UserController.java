package org.example.blog.controller;


import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Role;
import org.example.blog.domain.entity.User;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.IRoleService;
import org.example.blog.service.IUserService;
import org.example.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {


    @Autowired
    private IUserService userService;

//    用户列表
    @GetMapping("/list")
    public ResponseResult listUser(Integer pageNum,Integer pageSize,String userName,String phonenumber,Integer status){
        return userService.listUser(pageNum,pageSize,userName,phonenumber,status);
    }

//     新增用户
    @PostMapping
    public ResponseResult add(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw  new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    @Autowired
    private IRoleService roleService;

    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

//    删除用户
@DeleteMapping("/{userIds}")
public ResponseResult remove(@PathVariable List<Long> userIds) {
    if(userIds.contains(SecurityUtils.getUserId())){
        return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
    }
    userService.removeByIds(userIds);
    return ResponseResult.okResult();
}
}
