package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private IArticleService articleService;


    @GetMapping("/hotArticleList")
    @ResponseBody
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    @ResponseBody
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long
            categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @ResponseBody
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
