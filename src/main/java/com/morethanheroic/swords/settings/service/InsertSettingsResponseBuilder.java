package com.morethanheroic.swords.settings.service;

import com.morethanheroic.swords.response.domain.Response;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertSettingsResponseBuilder {

    private final ResponseFactory responseFactory;

    @Autowired
    public InsertSettingsResponseBuilder(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    public Response build(UserEntity userEntity, String result) {
        Response response = responseFactory.newResponse(userEntity);

        response.setData("result", result);

        return response;
    }
}
