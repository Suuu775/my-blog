package org.example.blog.service.impl;

import org.example.blog.dao.ArticleTagMapper;
import org.example.blog.domain.entity.ArticleTag;
import org.example.blog.service.IArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-09
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

}
