package dev.andus.playerutils.commands.teleport;

import dev.andus.playerutils.Utils;
import dev.andus.playerutils.PlayerUtils;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpadenyCommand extends Command {
    private final Map<UUID, PlayerUtils.TeleportRequest> pendingTeleportRequests;

    public TpadenyCommand(Map<UUID, PlayerUtils.TeleportRequest> pendingTeleportRequests) {
        super("tpadeny", "tpdeny", "teleportdeny", "teleportaskdeny");
        this.pendingTeleportRequests = pendingTeleportRequests;
        setDefaultExecutor(this::execute);
    }

    public void execute(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        Player target = (Player) sender;
        UUID targetUUID = target.getUuid();
        if (pendingTeleportRequests.containsKey(targetUUID)) {
            PlayerUtils.TeleportRequest teleportRequest = pendingTeleportRequests.get(targetUUID);
            UUID requesterUUID = teleportRequest.requesterUUID;
            Player requester = Utils.findPlayerByUUID(requesterUUID);
            if (requester != null) {
                requester.sendMessage("§cTeleport request to §b" + target.getUsername() + " §cdenied.");
                target.sendMessage("§cTeleport request from §b" + requester.getUsername() + " §cdenied.");
            }
            pendingTeleportRequests.remove(targetUUID);
        } else {
            target.sendMessage("§cYou have no pending teleport requests.");
        }
    }
}
