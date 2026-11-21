package com.example.architect.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReturnBeaconItem extends Item {
    public ReturnBeaconItem(Settings s) { super(s); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity sp && world instanceof ServerWorld sw) {
            BlockPos spawn = sw.getSpawnPos();
            sp.requestTeleport(spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5);
            sw.playSound(null, sp.getBlockPos(), net.minecraft.sound.SoundEvents.ENTITY_ENDERMAN_TELEPORT, sp.getSoundCategory(), 1.5f, 1.0f);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
