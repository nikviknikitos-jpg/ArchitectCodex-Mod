package com.example.architect.item;

import com.example.architect.entity.ConstructEntity;
import com.example.architect.entity.ModEntities;
import com.example.architect.progress.CodexProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArchitectSparkItem extends Item {
    public ArchitectSparkItem(Settings s) { super(s); }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || !(user instanceof ServerPlayerEntity sp)) return TypedActionResult.pass(user.getStackInHand(hand));
        ServerWorld sw = (ServerWorld) world;
        BlockHitResult hit = (BlockHitResult) sp.raycast(8.0, 0.0f, false);
        if (hit == null) return TypedActionResult.pass(user.getStackInHand(hand));
        BlockPos pos = hit.getBlockPos().up();
        var state = world.getBlockState(hit.getBlockPos());
        int type = 0;
        var block = state.getBlock();
        if (block == net.minecraft.block.Blocks.COPPER_BLOCK) type = 0;
        else if (block == net.minecraft.block.Blocks.OBSIDIAN) type = 1;
        else if (block == net.minecraft.block.Blocks.AMETHYST_BLOCK) type = 2;
        else if (block == net.minecraft.block.Blocks.EMERALD_BLOCK) type = 3;
        else if (block == net.minecraft.block.Blocks.CRYING_OBSIDIAN) type = 4;
        else if (block == net.minecraft.block.Blocks.HAY_BLOCK) type = 5;
        else if (block == net.minecraft.block.Blocks.OAK_LOG) type = 6;
        else if (block == net.minecraft.block.Blocks.STONE_BRICKS) type = 7;
        else if (block == net.minecraft.block.Blocks.IRON_BLOCK) type = 8;
        else return TypedActionResult.pass(user.getStackInHand(hand));

        ConstructEntity c = ModEntities.CONSTRUCT.create(sw);
        if (c != null) {
            c.init(sp, type);
            c.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
            sw.spawnEntity(c);
            sw.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.5f, 1.2f);
            CodexProgress.ConstructRegistry.count(sp);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
