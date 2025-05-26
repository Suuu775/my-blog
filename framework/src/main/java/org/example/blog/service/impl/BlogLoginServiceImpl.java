package org.example.blog.service.impl;

import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.LoginUser;
import org.example.blog.domain.entity.User;
import org.example.blog.domain.vo.BlogUserLoginVo;
import org.example.blog.domain.vo.UserInfoVo;
import org.example.blog.service.IBlogLoginService;
import org.example.blog.utils.JwtUtil;
import org.example.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements IBlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;


    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }

//        生成token,返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //将用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);

//        返回token
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(loginUser.getUser(), userInfoVo);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//获取userid
        Long userId = loginUser.getUser().getId();
//删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
