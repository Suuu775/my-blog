package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.dao.UserMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.User;
import org.example.blog.domain.entity.UserRole;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.domain.vo.UserInfoVo;
import org.example.blog.domain.vo.UserVo;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.IUserService;
import org.example.blog.service.UserRoleService;
import org.example.blog.utils.BeanCopyUtils;
import org.example.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName)
                .eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber)
                .eq(status != null,User::getStatus,status);

        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<User> records = page.getRecords();
        List<UserVo> userVos = records.stream().map(item -> BeanCopyUtils.copyBean(item, UserVo.class)).collect(Collectors.toList());
        PageVo pageVo = new PageVo(userVos,page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public boolean checkUserNameUnique(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return userMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkPhoneUnique(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phonenumber);
        return userMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkEmailUnique(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return userMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public ResponseResult addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
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
