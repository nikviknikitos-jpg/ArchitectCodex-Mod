package com.example.architect.item;

import com.example.architect.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    public static Item ARCHITECT_SPARK, ARCHITECT_CODEX, ETHER_GEAR, ANCIENT_LOCATOR,
            REBUILD_HAMMER, MAGNETIC_STAFF, RETURN_BEACON, RESONANCE_WHISTLE, COLOSSUS_HEART,
            HELMET, CHESTPLATE, LEGGINGS, BOOTS, PLASMA_CORE, ETHER_SHARD;
    public static ItemGroup TAB;

    public static void register() {
        ARCHITECT_SPARK = reg("architect_spark", new ArchitectSparkItem(new Item.Settings().maxCount(1)));
        ARCHITECT_CODEX = reg("architect_codex", new ArchitectCodexItem(new Item.Settings().maxCount(1)));
        ETHER_GEAR = reg("ether_gear", new EtherGearItem(new Item.Settings().maxCount(1)));
        ANCIENT_LOCATOR = reg("ancient_locator", new Item(new Item.Settings()));
        REBUILD_HAMMER = reg("rebuild_hammer", new RebuildHammerItem(new Item.Settings().maxCount(1)));
        MAGNETIC_STAFF = reg("magnetic_staff", new MagneticStaffItem(new Item.Settings().maxCount(1)));
        RETURN_BEACON = reg("return_beacon", new ReturnBeaconItem(new Item.Settings().maxCount(1)));
        RESONANCE_WHISTLE = reg("resonance_whistle", new Item(new Item.Settings()));
        COLOSSUS_HEART = reg("colossus_heart", new ColossusHeartItem(new Item.Settings().maxCount(1)));
        HELMET = reg("architect_helmet", new ArmorItem(ModArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Settings()));
        CHESTPLATE = reg("architect_chestplate", new ArmorItem(ModArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
        LEGGINGS = reg("architect_leggings", new ArmorItem(ModArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Settings()));
        BOOTS = reg("architect_boots", new ArmorItem(ModArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Settings()));
        PLASMA_CORE = reg("plasma_core", new Item(new Item.Settings()));
        ETHER_SHARD = reg("ether_shard", new Item(new Item.Settings()));

        TAB = Registry.register(Registries.ITEM_GROUP, new Identifier("architect", "architect_tab"),
                FabricItemGroup.builder().displayName(Text.translatable("itemGroup.architect.architect_tab"))
                        .icon(() -> new ItemStack(ARCHITECT_SPARK))
                        .entries((ctx, entries) -> {
                            entries.add(ARCHITECT_SPARK);
                            entries.add(ARCHITECT_CODEX);
                            entries.add(ETHER_GEAR);
                            entries.add(ANCIENT_LOCATOR);
                            entries.add(REBUILD_HAMMER);
                            entries.add(MAGNETIC_STAFF);
                            entries.add(RETURN_BEACON);
                            entries.add(RESONANCE_WHISTLE);
                            entries.add(COLOSSUS_HEART);
                            entries.add(HELMET);
                            entries.add(CHESTPLATE);
                            entries.add(LEGGINGS);
                            entries.add(BOOTS);
                            entries.add(PLASMA_CORE);
                            entries.add(ETHER_SHARD);
                            for (var b : com.example.architect.block.ModBlocks.ALL) {
                                entries.add(b);
                            }
                        }).build());
    }

    private static Item reg(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("architect", name), item);
    }

    public static Item[] all() {
        return new Item[]{ARCHITECT_SPARK, ARCHITECT_CODEX, ETHER_GEAR, ANCIENT_LOCATOR,
                REBUILD_HAMMER, MAGNETIC_STAFF, RETURN_BEACON, RESONANCE_WHISTLE, COLOSSUS_HEART,
                HELMET, CHESTPLATE, LEGGINGS, BOOTS, PLASMA_CORE, ETHER_SHARD};
    }
}
