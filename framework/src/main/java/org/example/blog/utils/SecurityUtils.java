package org.example.blog.utils;

import org.example.blog.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static LoginUser getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (LoginUser) authentication.getPrincipal();
    }

    public  static  long getUserId(){
        return getLoginUser().getUser().getId();
    }

    public static Boolean IsAdmin(){
        Long userId = getUserId();
        return userId != null && userId == 1;
    }
}
