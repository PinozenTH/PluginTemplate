package com.pinont.experiencesCraft.Commands;

import com.pinont.experiencesCraft.ItemCreator.ItemCreatorUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class entities implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            new ItemCreatorUI().displayTo(player);
        }
        return true;
    }
}
