package dev.andus.playerutils;

import dev.andus.playerutils.commands.MsgCommand;
import dev.andus.playerutils.commands.teleport.TpaCommand;
import dev.andus.playerutils.commands.teleport.TpacceptCommand;
import dev.andus.playerutils.commands.teleport.TpadenyCommand;
import dev.andus.playerutils.commands.teleport.TpahereCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtils extends Extension {
    public static final Map<UUID, TeleportRequest> pendingTeleportRequests = new HashMap<>();
    public static String prefix = "[PlayerUtils] ";

    @Override
    public void initialize() {
        Settings.read();
        var commandManager = MinecraftServer.getCommandManager();
        // /msg command
        if (Settings.isMsgEnabled()) {
            commandManager.register(new MsgCommand());
            System.out.println(prefix + "MSG Enabled");
        }
        // /tpa commands
        if (Settings.isTpaEnabled()) {
            commandManager.register(new TpaCommand());
            commandManager.register(new TpacceptCommand(pendingTeleportRequests));
            commandManager.register(new TpadenyCommand(pendingTeleportRequests));
            System.out.println(prefix + "TPA Enabled");
            if (Settings.isTpaHereEnabled()) {
                commandManager.register(new TpahereCommand());
                System.out.println(prefix + "TPA Here Enabled");
            }
        }
        // Join/Leave events
        if (Settings.isJoinEventEnabled()) {
            getEventNode().addListener(PlayerLoginEvent.class, this::onPlayerJoin);
            System.out.println("Join Message Enabled");
        } if (Settings.isLeaveEventEnabled()) {
            getEventNode().addListener(PlayerDisconnectEvent.class, this::onPlayerLeave);
            System.out.println("Leave Message Enabled");
        }
        System.out.println("Enabled PlayerUtils extension!");
    }

    @Override
    public void terminate() {
        System.out.println("Disabling PlayerUtils extension!!");
    }

    private void onPlayerJoin(PlayerLoginEvent event) {
        event.getPlayer().sendMessage("§aWelcome to the server, " + event.getPlayer().getUsername() + "!");
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.sendMessage(" §2[§a+§2] §e" + event.getPlayer().getUsername() + " joined the server!");
        }
        System.out.println(" [+] " + event.getPlayer().getUsername() + " joined the server!");
    }

    private void onPlayerLeave(PlayerDisconnectEvent event) {
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.sendMessage(" §4[§c-§4] §e" + event.getPlayer().getUsername() + " left the server.");
        }
        System.out.println(" [-] " + event.getPlayer().getUsername() + " left the server.");
    }

    public static class TeleportRequest {
        public UUID requesterUUID;
        public UUID targetUUID;
        public TeleportType teleportType;

        public TeleportRequest(UUID requesterUUID, UUID targetUUID, TeleportType teleportType) {
            this.requesterUUID = requesterUUID;
            this.targetUUID = targetUUID;
            this.teleportType = teleportType;
        }
    }

    public enum TeleportType {
        SENDER_TO_TARGET,
        TARGET_TO_SENDER
    }
}
