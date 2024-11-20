package com.pinont.experiencesCraft;

import com.pinont.experiencesCraft.Commands.entities;
import com.pinont.experiencesCraft.Commands.Trash;
import com.pinont.experiencesCraft.ItemCreator.CreatorListener;
import com.pinont.experiencesCraft.events.BlockListener;
import com.pinont.experiencesCraft.events.PlayerListener;
import com.pinont.piXLib.PiXLib;
import com.pinont.piXLib.api.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class Core extends JavaPlugin {

    public static Plugin plugin;
    public Core() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // add listeners and commands here for registration
        PiXLib.listeners.addAll(List.of(
                new PlayerListener(),
                new BlockListener(),
                new CreatorListener()
        ));
        PiXLib.commands.putAll(Map.of(
                "trash", new Trash(),
                "itemcreator", new entities()
        ));

        // setup plugin
        PiXLib.setup(this);

        /*
         * add your code here
         */
    }

    @Override
    public void onDisable() {
        /*
         * add your code here
         */

        // unregister the plugin
        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer(Common.colorize("&b&aNew Patch Has been launched, Please Rejoin!"));
        }
        PiXLib.unregister();
    }
}
