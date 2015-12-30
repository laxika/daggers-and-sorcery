package com.morethanheroic.swords.response.service;

import com.morethanheroic.swords.attribute.domain.BasicAttribute;
import com.morethanheroic.swords.attribute.domain.CombatAttribute;
import com.morethanheroic.swords.attribute.service.AttributeFacade;
import com.morethanheroic.swords.response.domain.CharacterData;
import com.morethanheroic.swords.response.domain.Response;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create new {@link Response} objects based on the data provided in the {@link UserEntity}.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResponseFactory {

    private static final boolean SUCCESSFUL_REQUEST = true;
    private static final boolean FAILED_REQUEST = false;

    @NonNull
    private final AttributeFacade attributeFacade;

    public Response newResponse(UserEntity userEntity) {
        return new Response(buildCharacterData(userEntity));
    }

    public Response newSuccessfulResponse(UserEntity userEntity) {
        return buildStatusResponse(userEntity, SUCCESSFUL_REQUEST);
    }

    public Response newFailedResponse(UserEntity userEntity) {
        return buildStatusResponse(userEntity, FAILED_REQUEST);
    }

    private Response buildStatusResponse(UserEntity userEntity, boolean status) {
        final Response response = newResponse(userEntity);

        response.setData("success", status);

        return response;
    }

    private CharacterData buildCharacterData(UserEntity user) {
        if (user == null) {
            return null;
        }

        return new CharacterData(
                attributeFacade.calculateAttributeValue(user, BasicAttribute.MOVEMENT).getValue(),
                attributeFacade.calculateAttributeValue(user, CombatAttribute.LIFE).getValue(),
                attributeFacade.calculateAttributeValue(user, CombatAttribute.MANA).getValue()
        );
    }
}
