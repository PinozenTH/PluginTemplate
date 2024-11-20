package com.pinont.experiencesCraft.events;

import com.pinont.experiencesCraft.Core;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockGenerate(BlockFormEvent event) {
        Block block = event.getBlock();
        switch (block.getType()) {
            case Material.WATER, Material.LAVA, Material.FIRE, Material.CAVE_AIR, Material.COBBLESTONE, Material.STONE -> block.setMetadata("not_nature", new FixedMetadataValue(Core.plugin, true));
        }
    }

}
