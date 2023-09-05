package dev.andus.playerutils.commands.teleport;

import dev.andus.playerutils.Utils;
import dev.andus.playerutils.PlayerUtils;
import dev.andus.playerutils.PlayerUtils.TeleportType;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpacceptCommand extends Command {
    private final Map<UUID, PlayerUtils.TeleportRequest> pendingTeleportRequests;

    public TpacceptCommand(Map<UUID, PlayerUtils.TeleportRequest> pendingTeleportRequests) {
        super("tpaccept", "teleportaccept");
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
                // Check the teleport type
                if (teleportRequest.teleportType == TeleportType.SENDER_TO_TARGET) {
                    requester.teleport(target.getPosition());
                    requester.sendMessage("§aTeleport request to §b" + target.getUsername() + " §aaccepted.");
                    target.sendMessage("§aTeleport request from §b" + requester.getUsername() + " §aaccepted.");
                } else if (teleportRequest.teleportType == TeleportType.TARGET_TO_SENDER) {
                    target.teleport(requester.getPosition());
                    target.sendMessage("§aTeleport request from §b" + requester.getUsername() + " §aaccepted.");
                    requester.sendMessage("§aTeleport request to §b" + target.getUsername() + " §aaccepted.");
                }
            } else {
                target.sendMessage("§cThe player who requested the teleport is no longer online.");
            }

            pendingTeleportRequests.remove(targetUUID);
        } else {
            target.sendMessage("§cYou have no pending teleport requests.");
        }
    }
}
