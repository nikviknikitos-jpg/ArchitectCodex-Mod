package com.example.architect.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class CorruptedColossusEntity extends HostileEntity {
    private ServerBossBar bossBar;

    public CorruptedColossusEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.setPersistent();
        if (!world.isClient) {
            bossBar = new ServerBossBar(this.getDisplayName(), ServerBossBar.Color.RED, ServerBossBar.Style.PROGRESS);
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 32));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient && bossBar != null) {
            bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            for (ServerPlayerEntity p : ((ServerWorld) getWorld()).getPlayers()) {
                if (p.squaredDistanceTo(this) < 4096) bossBar.addPlayer(p);
                else bossBar.removePlayer(p);
            }
        }
    }

    @Override
    public void onDeath(net.minecraft.entity.damage.DamageSource source) {
        super.onDeath(source);
        if (!getWorld().isClient && bossBar != null) bossBar.clearPlayers();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }
}
