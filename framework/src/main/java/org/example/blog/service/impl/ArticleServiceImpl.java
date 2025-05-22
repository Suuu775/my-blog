package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.ArticleMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Article;
import org.example.blog.domain.vo.HotArticleVo;
import org.example.blog.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public ResponseResult hotArticleList() {
//查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
//最多只查询10条
        Page<Article> page = new Page(1, 10);
        articleMapper.selectPage(page, queryWrapper);
        List<Article> articles = page.getRecords();

        //bean拷贝
        List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article, vo);
            articleVos.add(vo);
        }



        return ResponseResult.okResult(articleVos);
    }
}
