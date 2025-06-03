package org.example.blog.job;

import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.ArticleMapper;
import org.example.blog.domain.entity.Article;
import org.example.blog.service.IArticleService;
import org.example.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Scheduled(cron = "0 */5 * * * *")
    public void updateViewCount(){
        Map<String, Object> map = redisCache.getCacheMap(SystemConstants.VIEW_CONT);
        List<Article> collect = map.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey().toString()), Long.valueOf(entry.getValue().toString())))
                .collect(Collectors.toList());

        articleService.updateBatchById(collect);
    }
}
