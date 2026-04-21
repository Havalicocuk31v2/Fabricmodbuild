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

    public ClickGui() { super(Text.of("havalicocuk31v2")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawText(textRenderer, "havalicocuk31v2", 10, 10, 0xFF00FBFF, true);

        int x = 60, y = 40, width = 120;
        context.fill(x, y, x + width, y + 16, 0xFF111111);
        context.drawText(textRenderer, "COMBAT", x + 5, y + 4, 0xFFFFFFFF, false);
        
        y += 18;
        for (Module m : Argon.modules) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 16;
            context.fill(x, y, x + width, y + 16, hovered ? 0xFF252525 : 0xFF181818);
            
            String key = (m.getKey() == -1) ? "NONE" : GLFW.glfwGetKeyName(m.getKey(), 0).toUpperCase();
            if (bindingModule == m) key = "...";
            
            int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
            context.drawText(textRenderer, m.getName() + " [" + key + "]", x + 5, y + 4, color, false);

            if (selectedModule == m) {
                int sx = x + width + 5, sy = y;
                for (Setting s : m.getSettings()) {
                    context.fill(sx, sy, sx + 110, sy + 20, 0xFF111111);
                    if (s.isBoolean) {
                        int bCol = s.isEnabled() ? 0xFF00FBFF : 0xFF666666;
                        context.drawText(textRenderer, s.name, sx + 5, sy + 6, bCol, false);
                    } else {
                        context.drawText(textRenderer, s.name + ": " + s.getValue(), sx + 5, sy + 2, 0xFFCCCCCC, false);
                        context.fill(sx + 5, sy + 14, sx + 105, sy + 16, 0xFF333333);
                        double progress = (s.getValue() - s.min) / (s.max - s.min) * 100;
                        context.fill(sx + 5, sy + 14, (int)(sx + 5 + progress), sy + 16, 0xFF00FBFF);
                        if (s.dragging) {
                            double val = ((mouseX - (sx + 5)) / 100.0) * (s.max - s.min) + s.min;
                            s.setValue(val);
                        }
                    }
                    sy += 22;
                }
            }
            y += 17;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 60, y = 58;
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
                    if (mouseX >= sx && mouseX <= sx + 110 && mouseY >= sy && mouseY <= sy + 20) {
                        if (s.isBoolean) s.toggle(); else s.dragging = true;
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
        for (Module m : Argon.modules) for (Setting s : m.getSettings()) s.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            bindingModule.setKey(keyCode == GLFW.GLFW_KEY_ESCAPE ? -1 : keyCode);
            bindingModule = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { mc.setScreen(null); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
