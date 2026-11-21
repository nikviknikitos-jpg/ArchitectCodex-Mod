package com.example.architect.network;

import com.example.architect.progress.CodexProgress;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CodexSync {
    public static final Identifier CHANNEL = new Identifier("architect", "codex_sync");

    public static void registerCommon() {}

    public static void send(ServerPlayerEntity p) {
        if (!ServerPlayNetworking.canSend(p, CHANNEL)) return;
        PacketByteBuf buf = PacketByteBufs.create();
        var unlocked = CodexProgress.get(p);
        buf.writeInt(CodexProgress.TOTAL_CHAPTERS);
        for (int i = 0; i < CodexProgress.TOTAL_CHAPTERS; i++) {
            buf.writeBoolean(unlocked.contains(i));
        }
        ServerPlayNetworking.send(p, CHANNEL, buf);
    }
}
