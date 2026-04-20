package dev.lvstrng.argon.ui;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private Module selectedModule = null;

    public ClickGui() { super(Text.of("Argon Menu")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int x = 40, y = 40, width = 110;
        
        context.fill(x, y, x + width, y + 16, 0xDD0C0C0C);
        context.fill(x, y, x + 2, y + 16, 0xFF00FBFF);
        context.drawText(textRenderer, "⚔ Combat", x + 8, y + 4, 0xFFFFFFFF, false);
        
        y += 16;
        for (Module m : Argon.modules) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 14;
            context.fill(x, y, x + width, y + 14, hovered ? 0xEE151515 : 0xAA080808); 
            int color = m.isEnabled() ? 0xFF00FBFF : 0xFFBBBBBB;
            context.drawText(textRenderer, m.getName(), x + 8, y + 3, color, false);

            // Eğer modül seçiliyse (sağ tıklandıysa) ayarları yanında göster
            if (selectedModule == m) {
                int sy = y;
                for (Setting s : m.getSettings()) {
                    context.fill(x + width + 5, sy, x + width + 100, sy + 14, 0xDD0C0C0C);
                    context.drawText(textRenderer, s.getName() + ": " + (int)s.getValue(), x + width + 10, sy + 3, 0xFFFFFFFF, false);
                    sy += 14;
                }
            }
            y += 14;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40, y = 56;
        for (Module m : Argon.modules) {
            if (mouseX >= x && mouseX <= x + 110 && mouseY >= y && mouseY <= y + 14) {
                if (button == 0) m.toggle(); // Sol tık aç/kapat
                if (button == 1) selectedModule = (selectedModule == m) ? null : m; // Sağ tık ayarları aç
                return true;
            }
            y += 14;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
