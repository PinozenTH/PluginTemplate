package com.pinont.experiencesCraft.Commands;

import com.pinont.experiencesCraft.GUI.Trash_ui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            new Trash_ui().displayTo(player);
            return true;
        }
        return false;
    }
}
