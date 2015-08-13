package com.morethanheroic.swords.user.domain;

import com.morethanheroic.swords.race.model.Race;
import com.morethanheroic.swords.skill.service.Skills;
import com.morethanheroic.swords.user.repository.dao.UserDatabaseEntity;
import com.morethanheroic.swords.user.repository.domain.UserMapper;

import java.util.Date;

public class UserEntity {

    private final UserDatabaseEntity userDatabaseEntity;
    private final UserMapper userMapper;

    public UserEntity(UserDatabaseEntity userDatabaseEntity) {
        this(userDatabaseEntity, null);
    }

    public UserEntity(UserDatabaseEntity userDatabaseEntity, UserMapper userMapper) {
        this.userDatabaseEntity = userDatabaseEntity;
        this.userMapper = userMapper;
    }

    public int getId() {
        return userDatabaseEntity.getId();
    }

    public String getUsername() {
        return userDatabaseEntity.getUsername();
    }

    public Race getRace() {
        return userDatabaseEntity.getRace();
    }

    public Date getRegistrationDate() {
        return userDatabaseEntity.getRegistrationDate();
    }

    public Date getLastLoginDate() {
        return userDatabaseEntity.getLastLoginDate();
    }

    public Skills getSkills() {
        return userDatabaseEntity.getSkills();
    }

    public int getMapId() {
        return userDatabaseEntity.getMap();
    }

    public void setPosition(int x, int y) {
        userDatabaseEntity.setX(x);
        userDatabaseEntity.setY(y);

        userMapper.updatePosition(userDatabaseEntity.getId(), x, y);
    }

    public void setMap(int mapId) {
        userDatabaseEntity.setMap(mapId);
    }

    public int getX() {
        return userDatabaseEntity.getX();
    }

    public int getY() {
        return userDatabaseEntity.getY();
    }

    public int getHealth() {
        return userDatabaseEntity.getHealth();
    }

    public int getMana() {
        return userDatabaseEntity.getMana();
    }

    public int getMovement() {
        return userDatabaseEntity.getMovement();
    }

    public Date getLastRegenerationDate() {
        return userDatabaseEntity.getLastRegenerationDate();
    }

    public void regenerate(int health, int mana, int movement, Date date) {
        userDatabaseEntity.setLastRegenerationDate(date);
        userDatabaseEntity.setMana(mana);
        userDatabaseEntity.setHealth(health);
        userDatabaseEntity.setMovement(movement);

        System.out.println("UODATIONG!");
        System.out.println(userDatabaseEntity.getId()+" "+health+" "+mana+" "+movement+" "+date);
        userMapper.updateRegeneration(userDatabaseEntity.getId(), health, mana, movement, date);
    }
}
