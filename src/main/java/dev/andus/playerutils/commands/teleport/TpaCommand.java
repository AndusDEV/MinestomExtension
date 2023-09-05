package dev.andus.playerutils.commands.teleport;

import dev.andus.playerutils.PlayerUtils;
import dev.andus.playerutils.Utils;
import dev.andus.playerutils.PlayerUtils.TeleportType;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class TpaCommand extends Command {
    public TpaCommand() {
        super("tpa", "tpask", "teleportask");
        setDefaultExecutor(this::execute);
    }

    public void execute(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        Player player = (Player) sender;
        String[] args = context.getInput().split(" ");
        if (args.length != 2) {
            player.sendMessage("§cUsage: /tpa <player>");
            return;
        }

        String targetName = args[1];
        Player target = Utils.findPlayerByName(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found: §l" + targetName);
            return;
        }

        UUID requesterUUID = player.getUuid();
        UUID targetUUID = target.getUuid();

        // Set teleport type as SENDER_TO_TARGET
        PlayerUtils.pendingTeleportRequests.put(targetUUID, new PlayerUtils.TeleportRequest(requesterUUID, targetUUID, TeleportType.SENDER_TO_TARGET));

        player.sendMessage("§eTeleport request sent to §b" + target.getUsername() + "§e. Waiting for their response...");
        target.sendMessage("§b" + player.getUsername() + " §ehas requested to teleport to you. Type §a/tpaccept §eor §c/tpadeny §eto respond.");
    }
}