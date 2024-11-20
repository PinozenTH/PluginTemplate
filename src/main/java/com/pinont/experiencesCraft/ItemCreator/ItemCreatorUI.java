package com.pinont.experiencesCraft.ItemCreator;

import com.pinont.experiencesCraft.Core;
import com.pinont.piXLib.api.creator.ItemCreator;
import com.pinont.piXLib.api.menus.Button;
import com.pinont.piXLib.api.menus.MenuCreator;
import com.pinont.piXLib.api.menus.Props;
import com.pinont.piXLib.api.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ItemCreatorUI extends MenuCreator {

    public ItemStack item = new ItemCreator(new ItemStack(Material.IRON_INGOT)).setDisplayName("&cItem Not Set").setLore(Common.colorize("&e&lClick to select an entity")).create();
    public int amount = 0;

    public ItemCreatorUI() {
        this.setSize(9 * 4);
        this.setTitle("&a&lEntity Creator");
        this.addProp(new Props(0, 1, 2, 3, 5, 6, 7, 8) {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(" ").create();
            }
        });
        this.addButton(new Button(4) {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(item).setAmount(amount).create();
            }

            @Override
            public void onClick(Player player) {
                new ItemSelector(1).displayTo(player);
            }
        });
        this.addButton(new Button(9) {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(new ItemStack(Material.FLOWER_POT)).setDisplayName("&c&lAmount").setLore(Common.colorize("&d&lClick to change"), Common.colorize("&6Amount: " + amount)).create();
            }

            @Override
            public void onClick(Player player) {
                getInput(player);
            }
        });
    }

    public ItemCreatorUI setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ItemCreatorUI setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    private void getInput(Player player) {
        player.closeInventory();
        player.sendMessage("Please input value in chat");
        player.setMetadata("EntityCreator", new FixedMetadataValue(Core.plugin, true));
        CreatorListener.item.put(player, item);
    }

}
