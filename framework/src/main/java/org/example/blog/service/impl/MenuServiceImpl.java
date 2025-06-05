package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.MenuMapper;
import org.example.blog.domain.entity.Menu;
import org.example.blog.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 如果是管理员,返回所有权限标识
        if (id == 1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,"C","F","M")
                    .eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(queryWrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        } else {
//            否则返回用户所具有的权限标识
            List<String> perms =  menuMapper.selectPermsByUserId(id);
            return  perms;
        }
    }
}
