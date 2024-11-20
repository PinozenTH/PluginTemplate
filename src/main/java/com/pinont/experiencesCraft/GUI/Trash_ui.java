package com.pinont.experiencesCraft.GUI;

import com.pinont.piXLib.api.creator.ItemCreator;
import com.pinont.piXLib.api.menus.Button;
import com.pinont.piXLib.api.menus.MenuCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Trash_ui extends MenuCreator {
    public Trash_ui() {
        this.setSize(9 * 3);
        this.setTitle("&a&lTrash");
        this.addButton(new Button((9 * 2) + 4) {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(new ItemStack(Material.BARRIER)).setDisplayName("&c&lClose").create();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
            }
        });
    }
}
