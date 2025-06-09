package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.ArticleMapper;
import org.example.blog.dao.CategoryMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.dto.AddArticleDto;
import org.example.blog.domain.entity.Article;
import org.example.blog.domain.entity.ArticleTag;
import org.example.blog.domain.entity.Category;
import org.example.blog.domain.vo.ArticleDetailVo;
import org.example.blog.domain.vo.ArticleListVo;
import org.example.blog.domain.vo.HotArticleVo;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.service.IArticleService;
import org.example.blog.service.IArticleTagService;
import org.example.blog.utils.BeanCopyUtils;
import org.example.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private IArticleTagService articleTagService;

    @Autowired
    private  ArticleMapper articleMapper;

    @Autowired
    private  CategoryMapper categoryMapper;

    @Autowired
    private RedisCache redisCache;

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

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
//        ①只能查询正式发布的文章 ②置顶的文章要显示在最前面

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop)
                .eq(categoryId!= 0,Article::getCategoryId,categoryId);
// 分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(articlePage,queryWrapper);
        List<Article> articles = articlePage.getRecords();

// 封装vo
        ArrayList<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article:articles){
            ArticleListVo  articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article,articleListVo);

            Category category = categoryMapper.selectById(article.getCategoryId());
            String name = category.getName();
            articleListVo.setCategoryName(name);

            articleListVos.add(articleListVo);
        }

//  封装pagevo
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article,articleDetailVo);

//        根据分类id查询分类名,
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleDetailVo.setCategoryName(category.getName());

//        从redis中取viewCount
        Integer value = redisCache.getCacheMapValue(SystemConstants.VIEW_CONT, id.toString());
        articleDetailVo.setViewCount(value.longValue());

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.addCacheMapValue(SystemConstants.VIEW_CONT,id.toString(),1);
        return null;
    }

    @Override
    public ResponseResult getAdminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(title != null,Article::getTitle,title)
                .like(summary!=null,Article::getSummary,summary)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = new Page<>(pageNum, pageSize);

        articleMapper.selectPage(articlePage,queryWrapper);
        List<Article> articles = articlePage.getRecords();
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto article) {
        //添加博⽂
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        save(article1);
        // 将List<Long> tags转为List<ArticleTag> articleTags对象
        List<ArticleTag> articleTags = article.getTags().stream()
                .map(tagId -> new ArticleTag(article1.getId(), tagId)).collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
        //添加博⽂和标签的关联关系
    }

}
