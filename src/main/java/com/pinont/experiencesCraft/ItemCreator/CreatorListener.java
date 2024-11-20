package com.pinont.experiencesCraft.ItemCreator;

import com.pinont.piXLib.api.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CreatorListener implements Listener {

    public static HashMap<Player, ItemStack> item = new HashMap<>();

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("EntityCreator")) {
            player.removeMetadata("EntityCreator", Common.plugin);
            new ItemCreatorUI().setAmount(Integer.parseInt(event.getMessage())).setItem(item.get(player)).displayTo(player);
            item.remove(player);
            event.setCancelled(true);
        }
        if (player.hasMetadata("searching")) {
            player.removeMetadata("searching", Common.plugin);
            event.setCancelled(true);
            new ItemSelector(1).get(event.getMessage()).displayTo(player);
        }
    }

    @EventHandler
    public void onCommandExecute(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("EntityCreator")) {
            player.removeMetadata("EntityCreator", Common.plugin);
        }
        if (player.hasMetadata("searching")) {
            player.removeMetadata("searching", Common.plugin);
        }
    }

}
