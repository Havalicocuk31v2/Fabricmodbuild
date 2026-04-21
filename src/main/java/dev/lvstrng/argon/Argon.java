package dev.lvstrng.argon;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.combat.AimAssist;
import dev.lvstrng.argon.module.modules.combat.TriggerBot;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Argon implements ClientModInitializer {
    public static String name = "havalicocuk31v2";
    public static List<Module> modules = new ArrayList<>();
    
    // Menüyü açmak için varsayılan tuş (SAĞ SHIFT)
    public static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        // 1. Modülleri Listeye Ekle
        modules.add(new AimAssist());
        modules.add(new TriggerBot());

        // 2. ClickGUI Tuşunu Kaydet
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.argon.menu", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_RIGHT_SHIFT, 
                "category.argon.general"
        ));

        // 3. Her Tick'te Çalışacak Ana Döngü
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // --- Menü Açma Kontrolü ---
            if (guiKey.wasPressed()) {
                client.setScreen(new dev.lvstrng.argon.ui.ClickGui());
            }

            // --- Modül Ticklerini ve Tuş Atamalarını Çalıştır ---
            for (Module m : modules) {
                // Modül Tuşuna Basıldı mı? (Keybind Sistemi)
                if (m.getKey() != -1) {
                    if (InputUtil.isKeyPressed(client.getWindow().getHandle(), m.getKey())) {
                        // Basılı tutulduğunda sürekli toggle olmasın diye basit bir bekleme eklenebilir 
                        // veya direkt tick bazlı kontrol edilir.
                    }
                }

                // Modül Açıksa onTick'i çalıştır
                if (m.isEnabled()) {
                    m.onTick();
                }
            }
        });
    }

    // Tuş basımını algılayıp toggle yapan yardımcı metod
    // (KeyInputCallback yerine direkt pencere üzerinden kontrol daha stabildir)
    public static void onKeyPress(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;
        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }
}
