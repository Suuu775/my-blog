package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-05
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listAllRole();

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}
