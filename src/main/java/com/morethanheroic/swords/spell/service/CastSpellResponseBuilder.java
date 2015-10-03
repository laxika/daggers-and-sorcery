package com.morethanheroic.swords.spell.service;

import com.morethanheroic.swords.common.response.Response;
import com.morethanheroic.swords.common.response.ResponseFactory;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CastSpellResponseBuilder {

    private final ResponseFactory responseFactory;

    @Autowired
    public CastSpellResponseBuilder(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    public Response build(UserEntity userEntity, boolean success) {
        Response response = responseFactory.newResponse(userEntity);

        response.setData("success", success);

        return response;
    }
}
