package com.example.architect.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ModArmorMaterial implements ArmorMaterial {
    public static final ModArmorMaterial INSTANCE = new ModArmorMaterial();
    @Override public int getDurability(ArmorItem.Type type) { return 1200; }
    @Override public int getProtection(ArmorItem.Type type) { return 6; }
    @Override public int getEnchantability() { return 20; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.ofItems(Items.DIAMOND); }
    @Override public String getName() { return "architect"; }
    @Override public float getToughness() { return 3.0f; }
    @Override public float getKnockbackResistance() { return 0.2f; }
}
