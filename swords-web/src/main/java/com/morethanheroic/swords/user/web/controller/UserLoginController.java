package com.morethanheroic.swords.user.web.controller;

import com.morethanheroic.login.configuration.EnableLogin;
import com.morethanheroic.login.domain.LoginRequest;
import com.morethanheroic.login.service.LoginFacade;
import com.morethanheroic.response.domain.Response;
import com.morethanheroic.session.domain.SessionEntity;
import com.morethanheroic.swords.attribute.domain.BasicAttribute;
import com.morethanheroic.swords.attribute.domain.CombatAttribute;
import com.morethanheroic.swords.attribute.service.AttributeFacade;
import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.user.domain.UserEntity;
import com.morethanheroic.swords.user.service.UserFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Handles all login and user information related requests.
 */
@RestController
@EnableLogin
@SuppressWarnings("checkstyle:multiplestringliterals")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserLoginController {

    private static final UserEntity LOGGED_OUT_USER = null;

    @NonNull
    private final UserFacade userFacade;

    @NonNull
    private final AttributeFacade attributeFacade;

    @NonNull
    private final ResponseFactory responseFactory;

    @NonNull
    private final LoginFacade loginFacade;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Response login(SessionEntity sessionEntity, @RequestBody LoginRequest loginRequest) {
        return loginFacade.handleLoginRequest(sessionEntity, loginRequest);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public CharacterRefreshResponse info(UserEntity user) {
        final CharacterRefreshResponse response = responseFactory.newResponse(user);

        if (user != null) {
            response.setData("loggedIn", true);
            //TODO: Do we really need to set this data? It's automatically set afaik.
            response.setData("life", attributeFacade.calculateAttributeValue(user, CombatAttribute.LIFE).getValue());
            response.setData("max_life", attributeFacade.calculateAttributeMaximumValue(user, CombatAttribute.LIFE).getValue());
            response.setData("mana", attributeFacade.calculateAttributeValue(user, CombatAttribute.MANA).getValue());
            response.setData("max_mana", attributeFacade.calculateAttributeMaximumValue(user, CombatAttribute.MANA).getValue());
            response.setData("movement", attributeFacade.calculateAttributeValue(user, BasicAttribute.MOVEMENT).getValue());
            response.setData("max_movement", attributeFacade.calculateAttributeMaximumValue(user, BasicAttribute.MOVEMENT).getValue());
        } else {
            response.setData("loggedIn", false);
        }

        return response;
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public CharacterRefreshResponse logout(HttpSession session) {
        session.invalidate();

        final CharacterRefreshResponse response = responseFactory.newResponse(LOGGED_OUT_USER);

        response.setData("loggedIn", "false");

        return response;
    }
}
