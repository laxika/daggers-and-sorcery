package com.morethanheroic.swords.news.service;

import com.morethanheroic.swords.news.domain.NewsEntity;
import com.morethanheroic.swords.news.service.cache.NewsEntityCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Add an easy api for the news module.
 */
@Service
public class NewsFacade {

    @Autowired
    private NewsEntityCache newsEntityCache;

    public NewsEntity getNewsEntity(int newsId) {
        return newsEntityCache.getEntity(newsId);
    }

    public List<NewsEntity> getLastNewsEntity(int amount) {
        return newsEntityCache.getLastNewsEntity(amount);
    }
}
