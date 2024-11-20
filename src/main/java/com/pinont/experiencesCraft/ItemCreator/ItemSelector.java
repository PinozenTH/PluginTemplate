package com.pinont.experiencesCraft.ItemCreator;

import com.pinont.experiencesCraft.Core;
import com.pinont.piXLib.api.creator.ItemCreator;
import com.pinont.piXLib.api.menus.Button;
import com.pinont.piXLib.api.menus.MenuCreator;
import com.pinont.piXLib.api.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class ItemSelector extends MenuCreator {

    public Material[] items = Common.itemOnlyMaterial();

    int item_count = items.length;
    int container_size = 9 * 5;
    int max_page = (item_count / (container_size)) + 1;

    public ItemSelector(int page) {
        this.setSize(9*6);
        this.setTitle("&4&lItem Selector (Page " + page + "/" + max_page + ")");
        items = Arrays.copyOfRange(items, (page - 1) * (container_size), Math.min(item_count, page * (container_size)));
        int i = 0;
        for (Material item : items) {
            this.addButton(new Button(i) {
                @Override
                public ItemStack getItem() {
                    return new ItemCreator(new ItemStack(item)).setLore(Common.colorize("&e&lClick to select")).create();
                }

                @Override
                public void onClick(Player player) {
                    new ItemCreatorUI().setItem(new ItemStack(item)).displayTo(player);
                }
            });
            i++;
            if (i >= (container_size)) break;
        }
        this.addButton(new Button(container_size + 8) {
            @Override
            public ItemStack getItem() {
                return new ItemCreator(new ItemStack(Material.BARRIER)).setDisplayName("&c&lClose").create();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
            }
        });
        this.addButton(new Button(container_size + 6) {

            @Override
            public ItemStack getItem() {
                return new ItemCreator(new ItemStack(Material.BIRCH_SIGN)).setDisplayName("&6&lSearch").create();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                player.setMetadata("searching", new FixedMetadataValue(Core.plugin, true));
            }
        });
        if (page < max_page) {
            this.addButton(new Button(container_size + 4) {
                @Override
                public ItemStack getItem() {
                    return new ItemCreator(new ItemStack(Material.ARROW)).setDisplayName("&c&lNext").create();
                }

                @Override
                public void onClick(Player player) {
                    new ItemSelector(page + 1).displayTo(player);
                }
            });
        }
        if (page > 1) {
            this.addButton(new Button(container_size) {
                @Override
                public ItemStack getItem() {
                    return new ItemCreator(new ItemStack(Material.ARROW)).setDisplayName("&c&lPrevious").create();
                }

                @Override
                public void onClick(Player player) {
                    new ItemSelector(page - 1).displayTo(player);
                }
            });
        }
    }

    public ItemSelector get(String query) {
        if (query.isEmpty()) items = Arrays.stream(Material.values()).filter(Material::isItem).toArray(Material[]::new);
        else {
            items = Arrays.stream(Material.values()).filter(material -> material.name().contains(query.toUpperCase()) && material.isItem()).toArray(Material[]::new);
        }
        return this;
    }


}
