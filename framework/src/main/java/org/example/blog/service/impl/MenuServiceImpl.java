package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.MenuMapper;
import org.example.blog.domain.entity.Menu;
import org.example.blog.domain.vo.MenuVo;
import org.example.blog.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.utils.BeanCopyUtils;
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

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        // 判断是否是管理员
        if (userId == 1L) {
            // 如果是，获取所有符合要求的Menu
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,'C','M')
                    .eq(Menu::getStatus,SystemConstants.STATUS_NORMAL)
                    .orderByAsc(Menu::getParentId,Menu::getOrderNum);
            menus = menuMapper.selectList(queryWrapper);

        } else {
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }

        // 将为null的perm值改为空字符串
        for (Menu menu:menus){
            if (menu.getPerms() == null){
                menu.setPerms("");
            }
        }

        // 封装数据
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        // 构建tree：先找出一级菜单，然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = builderMenuTree(menuVos);
        return menuTree;
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menuVos) {
        //先找出一级菜单
        List<MenuVo> menuTree = menuVos.stream().filter(menuVo -> menuVo.getParentId().equals(0L))
                //然后去找他们的子菜单设置到children属性中
                .map(menuVo -> setChildrenList(menuVo, menuVos))
                .collect(Collectors.toList());
        return menuTree;
    }

    private MenuVo setChildrenList(MenuVo parent, List<MenuVo> menuVos) {
//        从menuVos里面找到所有parentId等于menuVo id 的菜单,就是它的子菜单
        List<MenuVo> childrenList = menuVos.stream().filter(menuVo -> menuVo.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());

        parent.setChildren(childrenList);
        return parent;
    }
}
