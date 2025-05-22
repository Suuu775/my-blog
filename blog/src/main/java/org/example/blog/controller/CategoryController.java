package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.service.IArticleService;
import org.example.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IArticleService articleService;

    @GetMapping("/getCategoryList")
    @ResponseBody
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }

}
