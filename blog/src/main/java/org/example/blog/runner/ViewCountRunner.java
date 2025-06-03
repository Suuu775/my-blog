package org.example.blog.runner;

import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.ArticleMapper;
import org.example.blog.domain.entity.Article;
import org.example.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
//        viewCount
        List<Article> articles = articleMapper.selectList(null);
        HashMap<String, Object> hashMap = new HashMap<>();
        articles.stream().map(article -> hashMap.put(article.getId().toString(),article.getViewCount().intValue()))
                .collect(Collectors.toList());

//        塞到redis
        redisCache.setCacheMap(SystemConstants.VIEW_CONT,hashMap);

    }
}
