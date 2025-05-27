package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.dao.CommentMapper;
import org.example.blog.dao.UserMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Comment;
import org.example.blog.domain.vo.CommentVo;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.ICommentService;
import org.example.blog.service.IUserService;
import org.example.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements ICommentService {
    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer
            pageSize) {
//查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getRootId, -1)
                .eq(Comment::getType, 0)
                .orderByAsc(Comment::getCreateTime);
//分页查询
        Page page = new Page(pageNum, pageSize);
        page(page, queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
//查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
//查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new
                PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     *
     * @param id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id)
                .orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        return toCommentVoList(comments);
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list,
                CommentVo.class);
        for (CommentVo commentVo : commentVoList) {
//通过creatyBy查询用户的昵称并赋值
            String nickName =
                    userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
//通过toCommentUserId查询用户的昵称并赋值
//如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {

                commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId
                        ()).getNickName());
            }
        }
        return commentVoList;
    }
}