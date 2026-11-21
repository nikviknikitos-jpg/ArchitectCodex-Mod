package com.example.architect.command;

import com.example.architect.item.ModItems;
import com.example.architect.progress.CodexProgress;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ArchitectCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("givearchitect").executes(ctx -> {
            ServerPlayerEntity p = ctx.getSource().getPlayer();
            if (p != null) {
                for (Item item : ModItems.all()) {
                    p.getInventory().insertStack(new ItemStack(item));
                }
                for (var b : com.example.architect.block.ModBlocks.ALL) {
                    p.getInventory().insertStack(new ItemStack(b));
                }
                ctx.getSource().sendFeedback(() -> Text.literal("Architect kit given"), false);
                return 1;
            }
            return 0;
        }));
        dispatcher.register(CommandManager.literal("architectunlockall").executes(ctx -> {
            ServerPlayerEntity p = ctx.getSource().getPlayer();
            if (p != null) {
                CodexProgress.unlockAll(p);
                ctx.getSource().sendFeedback(() -> Text.literal("Codex fully decrypted"), false);
                return 1;
            }
            return 0;
        }));
    }
}
