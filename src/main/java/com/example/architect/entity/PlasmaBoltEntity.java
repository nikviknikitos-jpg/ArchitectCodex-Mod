package com.example.architect.entity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class PlasmaBoltEntity extends ThrownItemEntity {
    private int life = 0;

    public PlasmaBoltEntity(EntityType<? extends PlasmaBoltEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() { return Items.MAGMA_CREAM; }

    @Override
    public void tick() {
        super.tick();
        life++;
        if (getWorld().isClient) {
            getWorld().addParticle(ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);
            getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0, 0, 0);
        } else if (life % 5 == 0) {
            var hit = this.getBoundingBox().expand(1.0);

            for (LivingEntity le : getWorld().getEntitiesByClass(LivingEntity.class, hit,
                    e -> e.isAlive() && !((Object) e instanceof PlayerEntity) && !(e instanceof ConstructEntity))) {
                le.damage(getWorld().getDamageSources().magic(), 8.0f);
                this.discard();
                return;
            }
        }
        if (life > 100) this.discard();
    }
}
