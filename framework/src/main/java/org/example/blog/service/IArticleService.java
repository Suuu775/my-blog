package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Article;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
public interface IArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);
}
