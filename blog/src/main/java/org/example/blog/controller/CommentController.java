package org.example.blog.controller;

import org.example.blog.constant.SystemConstants;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Comment;
import org.example.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import org.example.blog.constant.SystemConstants;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping()
    @ResponseBody
    public ResponseResult comment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }


    @GetMapping("/commentList")
    @ResponseBody
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @GetMapping("/linkCommentList")
    @ResponseBody
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return
                commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
