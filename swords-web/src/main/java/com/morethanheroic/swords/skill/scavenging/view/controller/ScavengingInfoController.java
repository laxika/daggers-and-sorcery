package com.morethanheroic.swords.skill.scavenging.view.controller;

import com.morethanheroic.response.domain.Response;
import com.morethanheroic.swords.skill.scavenging.view.response.service.ScavengingInfoResponseBuilder;
import com.morethanheroic.swords.skill.scavenging.view.response.domain.ScavengingInfoResponseBuilderConfiguration;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScavengingInfoController {

    private final ScavengingInfoResponseBuilder scavengingInfoResponseBuilder;

    @RequestMapping(value = "/skill/scavenging/info", method = RequestMethod.GET)
    public Response scavengingInfo(UserEntity userEntity) {
        return scavengingInfoResponseBuilder.build(
                ScavengingInfoResponseBuilderConfiguration.builder()
                        .userEntity(userEntity)
                        .build()
        );
    }
}
