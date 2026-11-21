package com.example.architect.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static EntityType<ConstructEntity> CONSTRUCT;
    public static EntityType<CorruptedColossusEntity> CORRUPTED_COLOSSUS;
    public static EntityType<PlasmaBoltEntity> PLASMA_BOLT;

    public static void register() {
        CONSTRUCT = Registry.register(Registries.ENTITY_TYPE,
                new Identifier("architect", "construct"),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ConstructEntity::new)
                        .dimensions(EntityDimensions.fixed(1.2f, 1.8f)).build());
        CORRUPTED_COLOSSUS = Registry.register(Registries.ENTITY_TYPE,
                new Identifier("architect", "corrupted_colossus"),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CorruptedColossusEntity::new)
                        .dimensions(EntityDimensions.fixed(3.0f, 4.0f)).build());
        PLASMA_BOLT = Registry.register(Registries.ENTITY_TYPE,
                new Identifier("architect", "plasma_bolt"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, PlasmaBoltEntity::new)
                        .dimensions(EntityDimensions.fixed(0.4f, 0.4f)).build());
    }
}
