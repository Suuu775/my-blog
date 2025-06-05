package org.example.blog.service.impl;

import org.example.blog.dao.RoleMapper;
import org.example.blog.domain.entity.Role;
import org.example.blog.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (id == 1L){
            ArrayList<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        List<String> roles = roleMapper.selectRoleKeyByUserId(id);
        return roles;
    }
}
