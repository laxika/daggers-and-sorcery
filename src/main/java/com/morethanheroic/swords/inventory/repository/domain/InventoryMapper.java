package com.morethanheroic.swords.inventory.repository.domain;

import com.morethanheroic.swords.inventory.repository.dao.ItemDatabaseEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

public interface InventoryMapper {

    //UNSAFE! If we ever going to change to master-slave based mysql setup we should fix this!
    @Insert("INSERT INTO inventory SET user_id = #{user_id}, item_id = #{item_id}, amount = #{amount} ON DUPLICATE KEY UPDATE amount = amount + #{amount}")
    void addItem(@Param("user_id") int userId, @Param("item_id") int itemId, @Param("amount") int amount);

    @Select("SELECT * FROM inventory WHERE user_id = #{user_id}")
    List<ItemDatabaseEntity> getItems(@Param("user_id") int userId);

    @Select("SELECT * FROM inventory WHERE user_id = #{user_id} AND item_id = #{item_id}")
    ItemDatabaseEntity getItem(@Param("user_id") int userId, @Param("item_id") int itemId);

    @Update("UPDATE inventory SET amount = #{amount} WHERE user_id = #{user_id} AND item_id = #{item_id}")
    void removeItem(@Param("user_id")int userId, @Param("item_id") int itemId, @Param("amount") int amount);

    @Delete("DELETE FROM inventory WHERE user_id = #{user_id} AND item_id = #{item_id}")
    void deleteItem(@Param("user_id")int userId, @Param("item_id") int itemId);
}
