package com.example.architect.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static Block RUNE_CORE, ETHER_CRYSTAL, GRAVITY_PLATE, RESTORATION_PEDESTAL;
    public static final List<Block> ALL = new ArrayList<>();

    public static void register() {
        RUNE_CORE = registerBlock("rune_core", new Block(Block.Settings.create().strength(3f).luminance(s -> 7)));
        ETHER_CRYSTAL = registerBlock("ether_crystal", new Block(Block.Settings.create().strength(3f).luminance(s -> 12)));
        GRAVITY_PLATE = registerBlock("gravity_plate", new Block(Block.Settings.create().strength(2f)));
        RESTORATION_PEDESTAL = registerBlock("restoration_pedestal", new Block(Block.Settings.create().strength(3f)));
    }

    private static Block registerBlock(String name, Block block) {
        Registry.register(Registries.BLOCK, new Identifier("architect", name), block);
        Registry.register(Registries.ITEM, new Identifier("architect", name),
                new BlockItem(block, new Item.Settings()));
        ALL.add(block);
        return block;
    }
}
