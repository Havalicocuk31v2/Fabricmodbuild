package dev.lvstrng.argon.ui;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public ClickGui() { super(Text.of("Argon Menu")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka planı çiz (ESC ile kapatıldığında sorun olmaması için)
        this.renderBackground(context);
        
        int x = 40, y = 40, width = 110;
        
        // --- COMBAT HEADER ---
        context.fill(x, y, x + width, y + 16, 0xDD0C0C0C);
        context.fill(x, y, x + 2, y + 16, 0xFF00FBFF);
        context.drawText(textRenderer, "⚔ Combat", x + 8, y + 4, 0xFFFFFFFF, false);
        
        y += 16;
        // --- MODULE LIST ---
        if (Argon.modules != null) {
            for (Module m : Argon.modules) {
                context.fill(x, y, x + width, y + 14, 0xAA080808); 
                int color = m.isEnabled() ? 0xFF00FBFF : 0xFFBBBBBB;
                context.drawText(textRenderer, m.getName(), x + 8, y + 3, color, false);
                y += 14;
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // 256 = ESC
            mc.setScreen(null); 
            return true; 
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
