package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
public interface IUserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(String phonenumber);

    boolean checkEmailUnique(String email);

    ResponseResult addUser(User user);
}
