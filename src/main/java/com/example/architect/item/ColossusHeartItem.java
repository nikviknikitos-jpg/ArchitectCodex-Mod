package com.example.architect.item;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ColossusHeartItem extends Item {
    public ColossusHeartItem(Settings s) { super(s); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity sp) {
            sp.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                    .addPersistentModifier(new EntityAttributeModifier(
                            UUID.nameUUIDFromBytes(("colossus_heart").getBytes()),
                            "colossus_heart", 20.0, EntityAttributeModifier.Operation.ADDITION));
            sp.setHealth(sp.getMaxHealth());
            sp.getStackInHand(hand).decrement(1);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
