package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.ArticleMapper;
import org.example.blog.dao.CategoryMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Article;
import org.example.blog.domain.entity.Category;
import org.example.blog.domain.vo.CategoryVo;
import org.example.blog.service.ICategoryService;
import org.example.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
//        只展示有发布正式文章的分类
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleMapper.selectList(queryWrapper);

//        获取已发布文章的分类id
        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
//根据分类id查询分类表
        List<Category> categories = categoryMapper.selectBatchIds(categoryIds);

        ArrayList<CategoryVo> categoryVos = new ArrayList<>();

        for (Category category : categories){
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            categoryVos.add(categoryVo);
        }

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }
}
