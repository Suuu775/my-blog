package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;
import org.example.blog.domain.vo.AdminUserInfoVo;
import org.example.blog.domain.vo.MenuVo;
import org.example.blog.domain.vo.UserInfoVo;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.IAdminLoginService;
import org.example.blog.service.IMenuService;
import org.example.blog.service.IRoleService;
import org.example.blog.utils.BeanCopyUtils;
import org.example.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class AdminLoginController {
    @Autowired
    private IAdminLoginService adminLoginService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
//提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }


    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
//获取当前登录的用户
        User user = SecurityUtils.getLoginUser().getUser();
//根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
//根据用户id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());
//获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
//封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();
//查询menu，返回的menus以tree形式表示父子菜单的层级关系
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
//封装数据返回
        HashMap<Object, Object> map = new HashMap<>();
        map.put("menus", menus);
        return ResponseResult.okResult(map);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
