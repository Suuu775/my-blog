package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Comment;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
public interface ICommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
