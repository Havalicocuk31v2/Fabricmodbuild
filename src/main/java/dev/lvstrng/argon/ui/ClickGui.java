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

    public ClickGui() { 
        super(Text.of("havalicocuk31v2")); 
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka planı hafif karart
        this.renderBackground(context);
        
        // --- CLIENT LOGO ---
        context.drawText(textRenderer, "havalicocuk31v2", 10, 10, 0xFF00FBFF, true);

        int x = 60, y = 40, width = 125;
        
        // Kategori Başlığı
        context.fill(x, y, x + width, y + 16, 0xFF111111);
        context.fill(x, y + 15, x + width, y + 16, 0xFF00FBFF); // Alt neon çizgi
        context.drawText(textRenderer, "COMBAT", x + 6, y + 4, 0xFFFFFFFF, false);
        
        y += 18;
        for (Module m : Argon.modules) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 16;
            
            // Modül Kutusu
            context.fill(x, y, x + width, y + 16, hovered ? 0xFF252525 : 0xFF181818);
            
            // Tuş İsmi Hazırlama
            String keyName = (m.getKey() == -1) ? "NONE" : GLFW.glfwGetKeyName(m.getKey(), 0);
            if (keyName == null && m.getKey() != -1) keyName = "KEY " + m.getKey();
            if (bindingModule == m) keyName = "...";
            
            int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
            context.drawText(textRenderer, m.getName() + " [" + (keyName != null ? keyName.toUpperCase() : "NONE") + "]", x + 6, y + 4, color, false);

            // Seçili Modülün Ayarlarını Göster
            if (selectedModule == m) {
                int sx = x + width + 5, sy = y;
                for (Setting s : m.getSettings()) {
                    context.fill(sx, sy, sx + 110, sy + 22, 0xFF111111);
                    
                    if (s.isBoolean) {
                        // On/Off Ayarı
                        int bCol = s.isEnabled() ? 0xFF00FBFF : 0xFF666666;
                        context.drawText(textRenderer, s.name, sx + 5, sy + 6, bCol, false);
                    } else {
                        // Slider Ayarı
                        context.drawText(textRenderer, s.name + ": " + s.getValue(), sx + 5, sy + 2, 0xFFCCCCCC, false);
                        
                        // Slider Arka Plan
                        context.fill(sx + 5, sy + 14, sx + 105, sy + 17, 0xFF333333);
                        
                        // Slider Doluluk Oranı
                        double renderWidth = (s.getValue() - s.min) / (s.max - s.min) * 100;
                        context.fill(sx + 5, sy + 14, (int)(sx + 5 + renderWidth), sy + 17, 0xFF00FBFF);

                        // Sürükleme Mantığı
                        if (s.dragging) {
                            double val = ((mouseX - (sx + 5)) / 100.0) * (s.max - s.min) + s.min;
                            s.setValue(val);
                        }
                    }
                    sy += 24;
                }
            }
            y += 17;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 60, y = 58;
        for (Module m : Argon.modules) {
            // Modül üzerine tıklandı mı?
            if (mouseX >= x && mouseX <= x + 125 && mouseY >= y && mouseY <= y + 16) {
                if (button == 0) m.toggle(); // Sol Tık: Aç/Kapat
                if (button == 1) selectedModule = (selectedModule == m) ? null : m; // Sağ Tık: Ayarlar
                if (button == 2) bindingModule = m; // Orta Tık: Tuş Ata
                return true;
            }
            
            // Ayarlar üzerine tıklandı mı?
            if (selectedModule == m) {
                int sx = x + 130, sy = y;
                for (Setting s : m.getSettings()) {
                    if (mouseX >= sx && mouseX <= sx + 110 && mouseY >= sy && mouseY <= sy + 22) {
                        if (s.isBoolean) s.toggle(); 
                        else s.dragging = true;
                        return true;
                    }
                    sy += 24;
                }
            }
            y += 17;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Fare bırakıldığında tüm sürüklemeleri durdur
        for (Module m : Argon.modules) {
            for (Setting s : m.getSettings()) s.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Eğer tuş atama modundaysak
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                bindingModule.setKey(-1); // Tuşu temizle
            } else {
                bindingModule.setKey(keyCode); // Yeni tuşu ata
            }
            bindingModule = null;
            return true;
        }
        
        // ESC ile menüyü kapat
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { 
            mc.setScreen(null); 
            return true; 
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false; // Menü açıldığında oyun durmasın (Serverlar için şart)
    }
        }
