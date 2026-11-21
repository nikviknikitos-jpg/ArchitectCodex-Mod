package com.example.architect.progress;

import com.example.architect.network.CodexSync;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.*;

public class CodexProgress {
    public static final Map<UUID, Set<Integer>> UNLOCKED = new HashMap<>();
    public static final int TOTAL_CHAPTERS = 8;

    public static Set<Integer> get(ServerPlayerEntity p) {
        return UNLOCKED.computeIfAbsent(p.getUuid(), k -> {
            Set<Integer> s = new HashSet<>();
            s.add(0); s.add(1);
            return s;
        });
    }

    public static void unlock(ServerPlayerEntity p, int chapter) {
        Set<Integer> s = get(p);
        if (s.add(chapter)) {
            CodexSync.send(p);
            p.playSound(net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_CHIME,
                    net.minecraft.sound.SoundCategory.PLAYERS, 1.5f, 1.4f);
            p.sendMessage(Text.literal("§6Новая глава расшифрована в Кодексе Архитектора!"), false);
        }
    }

    public static void unlockAll(ServerPlayerEntity p) {
        Set<Integer> s = get(p);
        for (int i = 0; i < TOTAL_CHAPTERS; i++) s.add(i);
        CodexSync.send(p);
    }

    public static void tickServer(ServerWorld world) {
        if (world.getTime() % 20 != 0) return;
        for (ServerPlayerEntity p : world.getPlayers()) {
            Set<Integer> s = get(p);
            // Chapter III: harvest / pick up - simplified: y between 60-100 near farmland
            if (!s.contains(2)) {
                Box box = new Box(p.getX() - 6, p.getY() - 4, p.getZ() - 6,
                        p.getX() + 6, p.getY() + 4, p.getZ() + 6);
                boolean crop = !world.getEntitiesByClass(net.minecraft.entity.ItemEntity.class, box, e -> true).isEmpty();
                if (crop) unlock(p, 2);
            }
            // Chapter IV: Y>130 or Y<10
            if (!s.contains(3)) {
                if (p.getY() > 130 || p.getY() < 10) unlock(p, 3);
            }
            // Chapter V: potion effect
            if (!s.contains(4)) {
                if (!p.getStatusEffects().isEmpty()) unlock(p, 4);
            }
            // Chapter VI: nearby ancient tower (we check by structure tag approximated: block position of rune_core)
            if (!s.contains(5)) {
                Box box = new Box(p.getX() - 50, p.getY() - 50, p.getZ() - 50,
                        p.getX() + 50, p.getY() + 50, p.getZ() + 50);
                if (world.getBlockState(p.getBlockPos().add(0, -3, 0)).isOf(com.example.architect.block.ModBlocks.RUNE_CORE)) {
                    unlock(p, 5);
                }
            }
            // Chapter VII: 3 different constructs awakened (approx: track via CodexProgress counters)
            if (!s.contains(6)) {
                Integer count = ConstructRegistry.COUNTS.get(p.getUuid());
                if (count != null && count >= 3) unlock(p, 6);
            }
            // Chapter VIII: diamond armor
            if (!s.contains(7)) {
                var inv = p.getInventory();
                boolean diamond = false;
                for (int i = 0; i < inv.size(); i++) {
                    var st = inv.getStack(i);
                    if (st.isEmpty()) continue;
                    if (st.getItem() instanceof net.minecraft.item.ArmorItem armor) {
                        if (armor.getMaterial() == net.minecraft.item.ArmorMaterials.DIAMOND) diamond = true;
                    }
                }
                if (diamond) unlock(p, 7);
            }
        }
    }

    public static class ConstructRegistry {
        public static final Map<UUID, Integer> COUNTS = new HashMap<>();
        public static void count(ServerPlayerEntity p) {
            COUNTS.merge(p.getUuid(), 1, Integer::sum);
        }
    }
}
