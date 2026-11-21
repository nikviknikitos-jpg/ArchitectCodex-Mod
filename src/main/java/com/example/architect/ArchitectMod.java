package com.example.architect;

import com.example.architect.block.ModBlocks;
import com.example.architect.command.ArchitectCommands;
import com.example.architect.entity.ModEntities;
import com.example.architect.item.ModItems;
import com.example.architect.network.CodexSync;
import com.example.architect.progress.CodexProgress;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchitectMod implements ModInitializer {
    public static final String MODID = "architect";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModItems.register();
        ModBlocks.register();
        CodexSync.registerCommon();
        FabricDefaultAttributeRegistry.register(ModEntities.CONSTRUCT, com.example.architect.entity.ConstructEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.CORRUPTED_COLOSSUS, com.example.architect.entity.CorruptedColossusEntity.createAttributes());
        CommandRegistrationCallback.EVENT.register((dispatcher, ra, env) -> ArchitectCommands.register(dispatcher));
        ServerTickEvents.END_WORLD_TICK.register(CodexProgress::tickServer);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> CodexSync.send(handler.getPlayer()));
        LOGGER.info("ArchitectMod initialized");
    }
}
