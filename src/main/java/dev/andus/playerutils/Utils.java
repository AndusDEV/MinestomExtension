package dev.andus.playerutils;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class Utils {
    public static Player findPlayerByName(String name) {
        Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player player : onlinePlayers) { if (player.getUsername().equalsIgnoreCase(name)) { return player; } }
        return null;
    }

    public static Player findPlayerByUUID(UUID uuid) {
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }
}
