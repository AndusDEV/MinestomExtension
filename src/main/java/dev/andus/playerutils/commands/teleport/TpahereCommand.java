package dev.andus.playerutils.commands.teleport;

import dev.andus.playerutils.PlayerUtils;
import dev.andus.playerutils.Utils;
import dev.andus.playerutils.PlayerUtils.TeleportType;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class TpahereCommand extends Command {
    public TpahereCommand() {
        super("tpahere", "tpaskhere", "teleportaskhere");
        setDefaultExecutor(this::execute);
    }

    public void execute(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player senderPlayer = (Player) sender;
        String[] args = context.getInput().split(" ");
        if (args.length != 2) {
            senderPlayer.sendMessage("§cUsage: /tpahere <player>");
            return;
        }

        String targetName = args[1];
        Player target = Utils.findPlayerByName(targetName);
        if (target == null) {
            senderPlayer.sendMessage("§cPlayer not found: §l" + targetName);
            return;
        }

        UUID requesterUUID = senderPlayer.getUuid();
        UUID targetUUID = target.getUuid();
        PlayerUtils.pendingTeleportRequests.put(targetUUID, new PlayerUtils.TeleportRequest(requesterUUID, targetUUID, TeleportType.TARGET_TO_SENDER));
        senderPlayer.sendMessage("§eTeleport request sent to §b" + target.getUsername() + "§e. Waiting for their response...");
        target.sendMessage("§b" + senderPlayer.getUsername() + " §ehas requested you to teleport to them. Type §a/tpaccept §eor §c/tpadeny §eto respond.");
    }
}