package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.entity.Menu;
import org.example.blog.domain.vo.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-05
 */
public interface IMenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}
