package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.dao.UserMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;
import org.example.blog.domain.vo.UserInfoVo;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.IUserService;
import org.example.blog.utils.BeanCopyUtils;
import org.example.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(user, UserInfoVo.class));
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        userMapper.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //合法型校验
        if (user.getUserName().isEmpty()){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }

        if (user.getEmail().isEmpty()){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        //重复性校验
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // 密文
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //保存
        userMapper.insert(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail,email)) > 0;
    }

    private boolean userNameExist(String userName) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUserName,userName)) > 0;
    }
}
