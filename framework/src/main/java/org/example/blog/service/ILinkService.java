package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Link;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
public interface ILinkService extends IService<Link> {

    ResponseResult getAllLink();
}
