package com.morethanheroic.swords.item.service.response;

import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UseItemResponseBuilder {

    private final ResponseFactory responseFactory;

    @Autowired
    public UseItemResponseBuilder(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    public CharacterRefreshResponse build(UserEntity userEntity, boolean success) {
        CharacterRefreshResponse response = responseFactory.newResponse(userEntity);

        response.setData("success", success);

        return response;
    }
}
