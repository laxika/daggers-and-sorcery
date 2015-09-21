package com.morethanheroic.swords.combatsettings.repository.domain;

import com.morethanheroic.swords.combatsettings.repository.dao.CombatSettingsDatabaseEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CombatSettingsMapper {

    @Select("SELECT * FROM combat_settings WHERE user_id = #{user_id}")
    List<CombatSettingsDatabaseEntity> getAll(@Param("user_id")int userId);

    @Select("SELECT * FROM combat_settings WHERE id = #{id} ORDER BY id")
    CombatSettingsDatabaseEntity get(@Param("id") int id);

    @Delete("DELETE FROM combat_settings WHERE id = #{id}")
    void remove(@Param("id") int id);

    @Insert("INSERT INTO combat_settings SET user_id = #{user_id}, type = #{type}, settings_id = #{settingsId}, trigger_type = #{trigger}, target = #{target}")
    void save(CombatSettingsDatabaseEntity combatSettingsDatabaseEntity);
}
