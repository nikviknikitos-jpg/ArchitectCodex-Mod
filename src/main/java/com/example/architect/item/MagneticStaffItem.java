package com.example.architect.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class MagneticStaffItem extends Item {
    public MagneticStaffItem(Settings s) { super(s); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity sp && world instanceof ServerWorld sw) {
            Box box = new Box(sp.getX() - 16, sp.getY() - 8, sp.getZ() - 16,
                    sp.getX() + 16, sp.getY() + 8, sp.getZ() + 16);
            for (ItemEntity ie : sw.getEntitiesByClass(ItemEntity.class, box, e -> true)) {
                ie.setPosition(sp.getX(), sp.getY() + 0.5, sp.getZ());
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
