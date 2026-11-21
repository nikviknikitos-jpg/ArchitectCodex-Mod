package com.example.architect.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArchitectCodexScreen extends Screen {
    private int page = 0;
    private static final String[] CHAPTERS = {
            "Глава I: Завет Архитектора",
            "Глава II: Добытчики Недр",
            "Глава III: Хранители Земли",
            "Глава IV: Небесные Инженеры",
            "Глава V: Мастера Эфира",
            "Глава VI: Древние Башни и Колосс",
            "Глава VII: Орудия Перестройки",
            "Глава VIII: Доспехи Архитектора"
    };
    private static final String[] HINTS = {
            "Открыто: история создания Искры и базовые правила.",
            "Открыто: схемы постройки Рудокопа и Дровосека.",
            "Намёк: собери урожай с грядки или подбери 10 предметов.",
            "Намёк: поднимись выше облаков (Y > 130) или спустись в пещеры (Y < 10).",
            "Намёк: улучши конструкта Эфирной Шестеренкой или выпей зелье.",
            "Намёк: воспользуйся Компасом Руин или найди древнюю постройку.",
            "Намёк: пробуди не менее 3 разных конструктов.",
            "Намёк: победи Поврежденного Колосса или создай алмазную броню."
    };

    protected ArchitectCodexScreen() { super(Text.literal("Кодекс Архитектора")); }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Оглавление"), b -> page = 0)
                .dimensions(this.width / 2 - 150, this.height - 40, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("< Назад"), b -> {
            if (page > 0) page--;
        }).dimensions(this.width / 2 - 40, this.height - 40, 80, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Вперед >"), b -> {
            if (page < ClientCodex.total) page++;
        }).dimensions(this.width / 2 + 50, this.height - 40, 100, 20).build());

        // Chapter buttons on page 0
        if (page == 0) {
            int y = 60;
            for (int i = 0; i < ClientCodex.total; i++) {
                final int idx = i;
                boolean unlocked = ClientCodex.UNLOCKED.contains(i);
                String label = (unlocked ? "§a" : "§7") + CHAPTERS[i];
                this.addDrawableChild(ButtonWidget.builder(Text.literal(label), b -> page = i + 1)
                        .dimensions(this.width / 2 - 180, y, 360, 20).build());
                y += 24;
            }
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx);
        // Parchment background
        int w = 420, h = 260;
        int x = (this.width - w) / 2;
        int y = (this.height - h) / 2;
        ctx.fill(x, y, x + w, y + h, 0xFFF4E2C0);
        ctx.fill(x, y, x + w, y + 8, 0xFF6A4A28);
        ctx.fill(x, y + h - 8, x + w, y + h, 0xFF6A4A28);
        ctx.fill(x, y, x + 8, y + h, 0xFF6A4A28);
        ctx.fill(x + w - 8, y, x + w, y + h, 0xFF6A4A28);

        ctx.drawText(this.textRenderer, this.title, x + 16, y + 16, 0x442200, false);

        if (page == 0) {
            ctx.drawText(this.textRenderer, Text.literal("Оглавление — 8 глав"), x + 16, y + 36, 0x442200, false);
            ctx.drawItem(new ItemStack(Items.BOOK), x + w - 40, y + 24);
        } else {
            int idx = page - 1;
            boolean unlocked = ClientCodex.UNLOCKED.contains(idx);
            ctx.drawText(this.textRenderer, Text.literal(CHAPTERS[idx]), x + 16, y + 36, 0x442200, false);
            ctx.drawText(this.textRenderer, Text.literal(unlocked ? "§2Расшифровано" : "§4Зашифровано"), x + 16, y + 52, 0xAA3300, false);
            String[] lines = wrap(unlocked ? HINTS[idx] : HINTS[idx], 46);
            int ly = y + 80;
            for (String line : lines) {
                ctx.drawText(this.textRenderer, Text.literal(line), x + 24, ly, 0x442200, false);
                ly += 14;
            }
            ctx.drawItem(new ItemStack(Items.WRITABLE_BOOK), x + w - 48, y + 40);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    private String[] wrap(String s, int max) {
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();
        int len = 0;
        for (String w : words) {
            if (len + w.length() + 1 > max) {
                lines.add(sb.toString());
                sb = new StringBuilder();
                len = 0;
            }
            sb.append(w).append(" ");
            len += w.length() + 1;
        }
        if (sb.length() > 0) lines.add(sb.toString());
        return lines.toArray(new String[0]);
    }

    @Override
    public boolean shouldPause() { return false; }
}
