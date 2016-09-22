package com.morethanheroic.swords.forum.service;

import com.morethanheroic.swords.forum.domain.ForumCategoryEntity;
import com.morethanheroic.swords.forum.repository.dao.ForumCategoriesDatabaseEntity;
import com.morethanheroic.swords.forum.repository.dao.NewComment;
import com.morethanheroic.swords.forum.repository.dao.NewTopic;
import com.morethanheroic.swords.forum.repository.domain.ForumCategoryRepository;
import com.morethanheroic.swords.user.domain.UserEntity;
import com.morethanheroic.swords.user.service.UserEntityFactory;
import com.morethanheroic.swords.user.service.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2016. 08. 01..
 */


@Service
public class ForumService {


    @Autowired
    private ForumCategoryRepository forumCategoryRepository;

    @Autowired
    private UserEntityFactory userEntityFactory;

    public List<ForumCategoryEntity> getTopics(UserEntity userEntity) {
        List<ForumCategoriesDatabaseEntity> forumCategoriesDatabaseEntities = forumCategoryRepository.getCategories();

        List<ForumCategoryEntity> result = new ArrayList<>();
        for (ForumCategoriesDatabaseEntity forumCategoriesDatabaseEntity : forumCategoriesDatabaseEntities) {
            result.add(
                    ForumCategoryEntity.builder()
                            .name(forumCategoriesDatabaseEntity.getName())
                            .postCount(forumCategoriesDatabaseEntity.getPostCount())
                            .icon(forumCategoriesDatabaseEntity.getIcon())
                            .lastPostDate(forumCategoriesDatabaseEntity.getLastPostDate())
                            .lastPostUser(userEntityFactory.getEntity(forumCategoriesDatabaseEntity.getLastPostUser()))
                            .build()
            );
        }

        return result;
    }

    public void createNewTopic(UserEntity userEntity, NewTopic newTopic) {
        forumCategoryRepository.newTopic(newTopic);
    }

    public void createNewComment(UserEntity userEntity, NewComment newComment) {
        forumCategoryRepository.newComment(
                newComment.getTopicId(),
                newComment.getContent(),
                userEntity.getId(),
                newComment.isAnswer() ? 1 : 0,
                newComment.getAnswerToCommentId());
    }
}
