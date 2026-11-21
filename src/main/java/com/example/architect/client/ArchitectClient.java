package com.example.architect.client;

import com.example.architect.entity.ModEntities;
import com.example.architect.network.CodexSync;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ArchitectClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CONSTRUCT, ConstructRenderer::new);
        EntityRendererRegistry.register(ModEntities.CORRUPTED_COLOSSUS, ColossusRenderer::new);
        EntityRendererRegistry.register(ModEntities.PLASMA_BOLT, PlasmaBoltRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(CodexSync.CHANNEL, (client, handler, buf, sender) -> {
            int total = buf.readInt();
            java.util.Set<Integer> set = new java.util.HashSet<>();
            for (int i = 0; i < total; i++) {
                if (buf.readBoolean()) set.add(i);
            }
            client.execute(() -> {
                ClientCodex.UNLOCKED.clear();
                ClientCodex.UNLOCKED.addAll(set);
                ClientCodex.total = total;
            });
        });
    }
}
