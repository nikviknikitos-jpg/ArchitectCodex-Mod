package com.example.architect.client;

import net.minecraft.client.MinecraftClient;

public class ClientHooks {
    public static void openCodex() {
        MinecraftClient.getInstance().setScreen(new ArchitectCodexScreen());
    }
}
