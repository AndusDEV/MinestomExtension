package dev.andus.playerutils.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

import java.util.Collection;

public class MsgCommand extends Command {
    public MsgCommand() {
        super("message", "msg", "tell");
        setDefaultExecutor(this::execute);
    }

    public void execute(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player player = (Player) sender;
        String[] args = context.getInput().split(" ");
        if (args.length < 3) {
            player.sendMessage("§cUsage: /msg <player> <message>");
            return;
        }

        String targetName = args[1];
        Player target = findPlayerByName(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found: §c§l" + targetName);
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim();

        player.sendMessage("§7[§b§lYou §7-> §3" + target.getUsername() + "§7] §f" + message);
        target.sendMessage("§7[§3" + player.getUsername() + " §7-> §b§lYou§7] §f" + message);
    }

    private Player findPlayerByName(String name) {
        Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player player : onlinePlayers) { if (player.getUsername().equalsIgnoreCase(name)) { return player; } }
        return null;
    }
}