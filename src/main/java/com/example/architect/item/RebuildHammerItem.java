package com.example.architect.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RebuildHammerItem extends Item {
    public RebuildHammerItem(Settings s) { super(s); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity sp && world instanceof ServerWorld sw) {
            BlockHitResult hit = (BlockHitResult) sp.raycast(8.0, 0.0f, false);
            BlockPos bp = hit.getBlockPos();
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos p2 = bp.add(dx, dy, dz);
                        if (!sw.getBlockState(p2).isAir()) sw.breakBlock(p2, true);
                    }
                }
            }
            sp.playSound(net.minecraft.sound.SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
