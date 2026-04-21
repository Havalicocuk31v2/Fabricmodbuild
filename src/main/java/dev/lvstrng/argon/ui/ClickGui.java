package dev.lvstrng.argon.ui;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Screen {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private Module selectedModule = null;
    private Module bindingModule = null;

    public ClickGui() { super(Text.of("Argon Pro")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = 50, y = 50, width = 120;
        context.fill(x, y - 2, x + width, y + 16, 0xFF101010);
        context.fill(x, y + 16, x + width, y + 17, 0xFF00FBFF);
        context.drawText(textRenderer, "COMBAT", x + 6, y + 4, 0xFF00FBFF, false);
        
        y += 18;
        for (Module m : Argon.modules) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 16;
            context.fill(x, y, x + width, y + 16, hovered ? 0xFF1A1A1A : 0xFF121212);
            
            int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
            String keyName = m.getKey() == -1 ? "" : " [" + GLFW.glfwGetKeyName(m.getKey(), 0).toUpperCase() + "]";
            if (bindingModule == m) keyName = " [???]";
            
            context.drawText(textRenderer, m.getName() + keyName, x + 6, y + 4, color, false);

            if (selectedModule == m) {
                int sx = x + width + 5, sy = y;
                for (Setting s : m.getSettings()) {
                    context.fill(sx, sy, sx + 100, sy + 20, 0xFF121212);
                    context.drawText(textRenderer, s.name + ": " + s.value, sx + 4, sy + 2, 0xFFBBBBBB, false);
                    context.fill(sx + 4, sy + 12, sx + 96, sy + 14, 0xFF202020);
                    double renderWidth = (s.value - s.min) / (s.max - s.min) * 92;
                    context.fill(sx + 4, sy + 12, (int)(sx + 4 + renderWidth), sy + 14, 0xFF00FBFF);

                    if (s.dragging) {
                        double val = ((mouseX - (sx + 4)) / 92.0) * (s.max - s.min) + s.min;
                        s.setValue(val);
                    }
                    sy += 22;
                }
            }
            y += 17;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 50, y = 68;
        for (Module m : Argon.modules) {
            if (mouseX >= x && mouseX <= x + 120 && mouseY >= y && mouseY <= y + 16) {
                if (button == 0) m.toggle();
                if (button == 1) selectedModule = (selectedModule == m) ? null : m;
                if (button == 2) bindingModule = m;
                return true;
            }
            if (selectedModule == m) {
                int sx = x + 125, sy = y;
                for (Setting s : m.getSettings()) {
                    if (mouseX >= sx && mouseX <= sx + 100 && mouseY >= sy && mouseY <= sy + 20) {
                        s.dragging = true;
                        return true;
                    }
                    sy += 22;
                }
            }
            y += 17;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Module m : Argon.modules) {
            for (Setting s : m.getSettings()) s.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE) bindingModule.setKey(-1);
            else bindingModule.setKey(keyCode);
            bindingModule = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { mc.setScreen(null); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
