package org.example.blog.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.example.blog.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        Long userId = null;

        try {
            userId = SecurityUtils.getUserId();
        } catch (RuntimeException e) {
            e.printStackTrace();
            userId = -1L;
        }

        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("createBy", userId,metaObject);
        this.setFieldValByName("updateBy", userId,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = null;

        try {
            userId = SecurityUtils.getUserId();
        } catch (RuntimeException e) {
            e.printStackTrace();
            userId = -1L;
        }
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateBy", userId,metaObject);
    }
}
