package dev.andus.playerutils;

import dev.andus.playerutils.commands.MsgCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

public class PlayerUtils extends Extension {
    @Override
    public void initialize() {
        var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new MsgCommand());
        System.out.println("Enabled PlayerUtils extension!");
    }

    @Override
    public void terminate() {
        System.out.println("Disabling PlayerUtils extension!!");
    }
}
