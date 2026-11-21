package com.example.architect.item;

import com.example.architect.entity.ConstructEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class EtherGearItem extends Item {
    public EtherGearItem(Settings s) { super(s); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity sp) {
            Box box = new Box(sp.getX() - 4, sp.getY() - 4, sp.getZ() - 4,
                    sp.getX() + 4, sp.getY() + 4, sp.getZ() + 4);
            var list = world.getEntitiesByClass(ConstructEntity.class, box, e -> true);
            if (!list.isEmpty()) {
                ConstructEntity c = list.get(0);
                c.evolve();
                if (!sp.isCreative()) sp.getStackInHand(hand).decrement(1);
                sp.playSound(net.minecraft.sound.SoundEvents.BLOCK_ANVIL_USE, 1.0f, 1.2f);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
