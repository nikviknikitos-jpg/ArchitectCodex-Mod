package com.example.architect.entity;
import net.minecraft.item.Item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ConstructEntity extends PathAwareEntity {
    public int type = 0;
    public int evolution = 0;
    public int ticks = 0;
    public ServerPlayerEntity owner;

    public ConstructEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.setPersistent();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    public void init(ServerPlayerEntity owner, int type) {
        this.owner = owner;
        this.type = type;
    }

    public void evolve() {
        evolution = Math.min(evolution + 1, 3);
        if (getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.END_ROD, getX(), getY() + 1, getZ(), 30, 0.6, 0.6, 0.6, 0.1);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) return;
        ticks++;
        if (owner == null || !owner.isAlive()) {
            // find closest player
            var player = getWorld().getClosestPlayer(this, 32.0);
            if (player instanceof ServerPlayerEntity sp) owner = sp;
            if (owner == null) return;
        }

        // Follow owner
        if (getWorld() instanceof ServerWorld sw) {
            double dist2 = squaredDistanceTo(owner);
            if (dist2 > 64) {
                Vec3d dir = owner.getPos().subtract(getPos()).normalize().multiply(0.35);
                setVelocity(dir.x, getVelocity().y, dir.z);
                velocityModified = true;
            }
            if (ticks % 40 == 0) {
                if (type == 0) { // Miner: bring items
                    if (dist2 < 100) {
                        for (Item item : new Item[]{Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, Items.IRON_INGOT}) {
                            if (sw.random.nextInt(3) == 0) {
                                net.minecraft.entity.ItemEntity ie = new net.minecraft.entity.ItemEntity(sw,
                                        owner.getX(), owner.getY() + 0.5, owner.getZ(), new ItemStack(item));
                                sw.spawnEntity(ie);
                            }
                        }
                    }
                } else if (type == 1) { // Guardian: heal
                    if (dist2 < 64 && owner.getHealth() < owner.getMaxHealth()) {
                        owner.heal(2.0f);
                    }
                    var mobs = sw.getEntitiesByClass(LivingEntity.class,
                            new Box(getX() - 8, getY() - 4, getZ() - 8, getX() + 8, getY() + 4, getZ() + 8),
                            e -> e.isAlive() && ((Object) e instanceof MobEntity) && !(e instanceof ConstructEntity));
                    for (LivingEntity le : mobs) {
                        le.damage(sw.getDamageSources().magic(), 6.0f);
                    }
                } else if (type == 2) { // Scout: glow
                    if (dist2 < 100) {
                        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0, false, false));
                    }
                } else if (type == 3) { // Alchemist
                    if (dist2 < 64) {
                        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1, false, false));
                        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1, false, false));
                        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0, false, false));
                        owner.removeStatusEffect(StatusEffects.POISON);
                    }
                } else if (type == 4) { // Turret
                    if (dist2 < 400) {
                        var mobs = sw.getEntitiesByClass(LivingEntity.class,
                                new Box(getX() - 18, getY() - 8, getZ() - 18, getX() + 18, getY() + 8, getZ() + 18),
                                e -> e.isAlive() && ((Object) e instanceof MobEntity) && !(e instanceof ConstructEntity));
                        if (!mobs.isEmpty()) {
                            LivingEntity t = mobs.get(0);
                            Vec3d d = new Vec3d(t.getX() - getX(), t.getY() + 1 - getY(), t.getZ() - getZ()).normalize().multiply(0.5);
                            PlasmaBoltEntity b = ModEntities.PLASMA_BOLT.create(sw);
                            if (b != null) {
                                b.refreshPositionAndAngles(getX(), getY() + 1, getZ(), 0, 0);
                                b.setVelocity(d);
                                sw.spawnEntity(b);
                            }
                        }
                    }
                } else if (type == 5) { // Farmer
                    if (dist2 < 100 && sw.random.nextBoolean()) {
                        net.minecraft.entity.ItemEntity ie = new net.minecraft.entity.ItemEntity(sw,
                                owner.getX(), owner.getY() + 0.5, owner.getZ(),
                                new ItemStack(Items.WHEAT, 1 + sw.random.nextInt(3)));
                        sw.spawnEntity(ie);
                    }
                } else if (type == 6) { // Lumberjack
                    if (dist2 < 100 && sw.random.nextBoolean()) {
                        net.minecraft.entity.ItemEntity ie = new net.minecraft.entity.ItemEntity(sw,
                                owner.getX(), owner.getY() + 0.5, owner.getZ(),
                                new ItemStack(Items.OAK_LOG, 1 + sw.random.nextInt(2)));
                        sw.spawnEntity(ie);
                    }
                } else if (type == 7) { // Bridge builder
                    if (owner.isSneaking() && owner.getWorld() == sw) {
                        net.minecraft.util.math.BlockPos below = owner.getBlockPos().down();
                        if (sw.getBlockState(below).isAir()) {
                            sw.setBlockState(below, net.minecraft.block.Blocks.LIGHT_BLUE_STAINED_GLASS.getDefaultState());
                        }
                    }
                } else if (type == 8) { // Magnetic collector
                    var items = sw.getEntitiesByClass(net.minecraft.entity.ItemEntity.class,
                            new Box(getX() - 20, getY() - 20, getZ() - 20, getX() + 20, getY() + 20, getZ() + 20),
                            e -> true);
                    for (var ie : items) {
                        ie.setPosition(owner.getX(), owner.getY() + 0.5, owner.getZ());
                    }
                }
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("type", type);
        nbt.putInt("evo", evolution);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        type = nbt.getInt("type");
        evolution = nbt.getInt("evo");
    }
}
