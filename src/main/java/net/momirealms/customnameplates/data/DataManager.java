package net.momirealms.customnameplates.data;

import net.momirealms.customnameplates.AdventureManager;
import net.momirealms.customnameplates.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    public static Map<UUID, PlayerData> cache;
    public DataManager() {
        cache = new HashMap<>();
    }

    public PlayerData getOrCreate(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }
        PlayerData playerData = SqlHandler.getPlayerData(uuid);
        if (playerData == null) {
            playerData = new PlayerData(ConfigManager.MainConfig.default_nameplate, 0);
        }
        cache.put(uuid, playerData);
        return playerData;
    }

    public void unloadPlayer(UUID uuid) {
        if (!cache.containsKey(uuid)) {
            return;
        }
        SqlHandler.save(cache.get(uuid), uuid);
        cache.remove(uuid);
    }

    public void savePlayer(UUID uuid) {
        SqlHandler.save(cache.get(uuid), uuid);
    }

    public static boolean create() {
        if(ConfigManager.DatabaseConfig.use_mysql){
            AdventureManager.consoleMessage("<gradient:#2E8B57:#48D1CC>[CustomNameplates]</gradient> <color:#22e281>存储模式 - MYSQL");
        }else {
            AdventureManager.consoleMessage("<gradient:#2E8B57:#48D1CC>[CustomNameplates]</gradient> <color:#22e281>存储模式 - SQLite");
        }
        if (SqlHandler.connect()) {
            if (ConfigManager.DatabaseConfig.use_mysql) {
                SqlHandler.getWaitTimeOut();
            }
            SqlHandler.createTable();
        } else {
            AdventureManager.consoleMessage("<red>//DATA storage ERROR//</red>");
            return false;
        }
        return true;
    }
}