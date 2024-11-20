package com.pinont.experiencesCraft.events;

import com.pinont.experiencesCraft.Core;
import com.pinont.piXLib.api.creator.ItemCreator;
import com.pinont.piXLib.api.utils.Common;
import com.pinont.piXLib.api.utils.enums.PersisDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerListener implements Listener {

    private final List<Material> notAllowedCraftingItems = List.of( // List of items that are not allowed to craft
        Material.NETHERITE_BOOTS,
        Material.NETHERITE_CHESTPLATE,
        Material.NETHERITE_HELMET,
        Material.NETHERITE_LEGGINGS,
        Material.NETHERITE_AXE,
        Material.NETHERITE_HOE,
        Material.NETHERITE_PICKAXE,
        Material.NETHERITE_SHOVEL,
        Material.NETHERITE_SWORD,
        Material.DIAMOND_BOOTS,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_AXE,
        Material.DIAMOND_HOE,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_SHOVEL,
        Material.DIAMOND_SWORD,
        Material.CHAINMAIL_BOOTS,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_LEGGINGS,
        Material.GOLDEN_AXE,
        Material.GOLDEN_HOE,
        Material.GOLDEN_PICKAXE,
        Material.GOLDEN_SHOVEL,
        Material.GOLDEN_SWORD,
        Material.IRON_BOOTS,
        Material.IRON_CHESTPLATE,
        Material.IRON_HELMET,
        Material.IRON_LEGGINGS,
        Material.IRON_AXE,
        Material.IRON_HOE,
        Material.IRON_PICKAXE,
        Material.IRON_SHOVEL,
        Material.IRON_SWORD,
        Material.CHAINMAIL_BOOTS,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_LEGGINGS,
        Material.STONE_AXE,
        Material.STONE_HOE,
        Material.STONE_PICKAXE,
        Material.STONE_SHOVEL,
        Material.STONE_SWORD,
        Material.LEATHER_BOOTS,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_HELMET,
        Material.LEATHER_LEGGINGS,
        Material.WOODEN_AXE,
        Material.WOODEN_HOE,
        Material.WOODEN_PICKAXE,
        Material.WOODEN_SHOVEL,
        Material.WOODEN_SWORD
    );

    private final List<String> tools = List.of("pickaxe", "axe", "shovel", "hoe"); // List of tools

    private Boolean isTools(Material material) { // Check if the material is a tool
        return tools.stream().anyMatch(material.name().toLowerCase()::contains);
    }

    private final Map<String, Integer> xp_tools_tiers = Map.of( // Max XP for tier of tools
            "wooden", 128,
            "stone", 256,
            "iron", 512,
            "golden", 4096,
            "diamond", 8192,
            "netherite", 16384
    );

    public ItemStack[] starterKits() {
        return new ItemStack[] {
                new ItemCreator(new ItemStack(Material.WOODEN_AXE)).create(),
                new ItemCreator(new ItemStack(Material.WOODEN_HOE)).create(),
                new ItemCreator(new ItemStack(Material.WOODEN_PICKAXE)).create(),
                new ItemCreator(new ItemStack(Material.WOODEN_SHOVEL)).create(),
                new ItemCreator(new ItemStack(Material.WOODEN_SWORD)).create(),
                new ItemCreator(new ItemStack(Material.LEATHER_BOOTS)).create(),
                new ItemCreator(new ItemStack(Material.LEATHER_CHESTPLATE)).create(),
                new ItemCreator(new ItemStack(Material.LEATHER_HELMET)).create(),
                new ItemCreator(new ItemStack(Material.LEATHER_LEGGINGS)).create(),
                new ItemStack(new ItemStack(Material.BREAD, 16))
        };
    }

    private final NamespacedKey blockMined = new NamespacedKey(Core.plugin, "block_mined"); // namespaced key for block mined

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            for (ItemStack item : starterKits()) { // give starter kits to new players
                event.getPlayer().getInventory().addItem(item);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("not_nature")) return; // check if block is not natural generate

        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType().isAir()) return; // check if player is holding an item

        ItemStack mainHandItem = player.getInventory().getItemInMainHand(); // get the item in player's main hand

        if (isTools(mainHandItem.getType())) { // check if the item is a tool
            PersistentDataContainer container = Objects.requireNonNull(mainHandItem.getItemMeta()).getPersistentDataContainer(); // get the container of the item
            player.getInventory().setItemInMainHand( // set item lore of xp / max xp, add data container tag for item, and set unbreakable
                    new ItemCreator(mainHandItem)
                            .setPersisDataContainer("block_mined", String.valueOf(container.getOrDefault(blockMined, PersistentDataType.INTEGER, 0) + 1), PersisDataType.INT)
                            .setLore(Common.colorize("&l&eXP : ") + container.getOrDefault(blockMined, PersistentDataType.INTEGER, 1) + Common.colorize(" &7/ &e") + xp_tools_tiers.getOrDefault(mainHandItem.getType().name().toLowerCase().split("_")[0], 0))
                            .setUnbreakable(true)
                            .create()
            );
            if (container.getOrDefault(blockMined, PersistentDataType.INTEGER, 0) >= xp_tools_tiers.get(mainHandItem.getType().name().toLowerCase().split("_")[0])) { // check if item xp is enough to upgrade to next tier
                ItemCreator item = new ItemCreator(new ItemStack(mainHandItem))
                        .setType(upgrade(mainHandItem.getType())) // upgrade the item
                        .setPersisDataContainer("block_mined", 0, PersisDataType.INT); // reset the xp
                if (mainHandItem.getType().name().toLowerCase().split("_")[0].equals("netherite")) { // check if the item is netherite
                    int max_xp = container.getOrDefault(new NamespacedKey(Core.plugin, "max_xp"), PersistentDataType.INTEGER, 16384); // get the max xp
                    item.setPersisDataContainer("max_xp", max_xp * 2, PersisDataType.INT); // double the max xp
                }
                player.getInventory().setItemInMainHand(item.create());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("not_nature", new FixedMetadataValue(Core.plugin, true)); // set block metadata to not_nature
    }

    // TODO: When Ever player gets damaged, try adding stats and setup leveling systems
//    @EventHandler
//    public void onPlayerDamage(EntityDamageEvent event) {
//        if (event.getEntity() instanceof Player player) {
//
//        }
//    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        if (notAllowedCraftingItems.contains(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUseSmitingTable(SmithItemEvent event) {
        // TODO: can use nether upgrade to upgrade netherite items
    }

    public Material upgrade(Material item) {

        String first_half = item.name().toLowerCase().split("_")[0];

        switch (item.name().toLowerCase().split("_")[1]) {
            case "pickaxe" -> {
                return switch (first_half) {
                    case "wooden" -> Material.STONE_PICKAXE;
                    case "stone" -> Material.IRON_PICKAXE;
                    case "iron" -> Material.GOLDEN_PICKAXE;
                    case "golden" -> Material.DIAMOND_PICKAXE;
                    case "diamond" -> Material.NETHERITE_PICKAXE;
                    default -> Material.WOODEN_PICKAXE;
                };
            }
            case "axe" -> {
                return switch (first_half) {
                    case "wooden" -> Material.STONE_AXE;
                    case "stone" -> Material.IRON_AXE;
                    case "iron" -> Material.GOLDEN_AXE;
                    case "golden" -> Material.DIAMOND_AXE;
                    case "diamond" -> Material.NETHERITE_AXE;
                    default -> Material.WOODEN_AXE;
                };
            }
            case "shovel" -> {
                return switch (first_half) {
                    case "wooden" -> Material.STONE_SHOVEL;
                    case "stone" -> Material.IRON_SHOVEL;
                    case "iron" -> Material.GOLDEN_SHOVEL;
                    case "golden" -> Material.DIAMOND_SHOVEL;
                    case "diamond" -> Material.NETHERITE_SHOVEL;
                    default -> Material.WOODEN_SHOVEL;
                };
            }
            case "hoe" -> {
                return switch (first_half) {
                    case "wooden" -> Material.STONE_HOE;
                    case "stone" -> Material.IRON_HOE;
                    case "iron" -> Material.GOLDEN_HOE;
                    case "golden" -> Material.DIAMOND_HOE;
                    case "diamond" -> Material.NETHERITE_HOE;
                    default -> Material.WOODEN_HOE;
                };
            }
            case "sword" -> {
                return switch (first_half) {
                    case "wooden" -> Material.STONE_SWORD;
                    case "stone" -> Material.IRON_SWORD;
                    case "iron" -> Material.GOLDEN_SWORD;
                    case "golden" -> Material.DIAMOND_SWORD;
                    case "diamond" -> Material.NETHERITE_SWORD;
                    default -> Material.WOODEN_SWORD;
                };
            }
            case "boots" -> {
                return switch (first_half) {
                    case "leather" -> Material.IRON_BOOTS;
                    case "iron" -> Material.CHAINMAIL_BOOTS;
                    case "chainmail" -> Material.GOLDEN_BOOTS;
                    case "golden" -> Material.DIAMOND_BOOTS;
                    case "diamond" -> Material.NETHERITE_BOOTS;
                    default -> Material.LEATHER_BOOTS;
                };
            }
            case "chestplate" -> {
                return switch (first_half) {
                    case "leather" -> Material.IRON_CHESTPLATE;
                    case "iron" -> Material.CHAINMAIL_CHESTPLATE;
                    case "chainmail" -> Material.GOLDEN_CHESTPLATE;
                    case "golden" -> Material.DIAMOND_CHESTPLATE;
                    case "diamond" -> Material.NETHERITE_CHESTPLATE;
                    default -> Material.LEATHER_CHESTPLATE;
                };
            }
            case "helmet" -> {
                return switch (first_half) {
                    case "leather" -> Material.IRON_HELMET;
                    case "iron" -> Material.CHAINMAIL_HELMET;
                    case "chainmail" -> Material.GOLDEN_HELMET;
                    case "golden" -> Material.DIAMOND_HELMET;
                    case "diamond" -> Material.NETHERITE_HELMET;
                    default -> Material.LEATHER_HELMET;
                };
            }
            case "leggings" -> {
                return switch (first_half) {
                    case "leather" -> Material.IRON_LEGGINGS;
                    case "iron" -> Material.CHAINMAIL_LEGGINGS;
                    case "chainmail" -> Material.GOLDEN_LEGGINGS;
                    case "golden" -> Material.DIAMOND_LEGGINGS;
                    case "diamond" -> Material.NETHERITE_LEGGINGS;
                    default -> Material.LEATHER_LEGGINGS;
                };
            }
            default -> {
                return item; // return the same item if not found prevent from item losses
            }
        }
    }
}
