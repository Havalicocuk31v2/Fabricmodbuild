package dev.lvstrng.argon;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.combat.AimAssist;
import dev.lvstrng.argon.module.modules.combat.TriggerBot;
import dev.lvstrng.argon.ui.ClickGui;
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
    public static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        // Modülleri Kaydet
        modules.add(new AimAssist());
        modules.add(new TriggerBot());

        // CLICKGUI J TUŞU AYARI
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.argon.menu", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_J, // Burayı J yaptık kanka
                "category.argon.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Menü Açma
            if (guiKey.wasPressed()) {
                client.setScreen(new ClickGui());
            }

            // Modül Döngüsü
            for (Module m : modules) {
                // Modül Tuş Ataması Kontrolü (Toggle Mantığı)
                if (m.getKey() != -1) {
                    // Tuşa basıldığı anı yakalar (wasPressed benzeri mantık)
                    while (InputUtil.isKeyPressed(client.getWindow().getHandle(), m.getKey())) {
                        // Bu kısım genellikle KeyInputCallback ile daha sağlıklı çalışır 
                        // ama tick içinde de toggleWorking() çağrılabilir.
                    }
                }

                if (m.isEnabled()) {
                    m.onTick();
                }
            }
        });
    }

    // Tuş basıldığında dışarıdan çağrılacak metod
    public static void onKeyPress(int key) {
        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggleWorking(); // Bir kere basınca çalışır, bir daha basınca durur
            }
        }
    }
}
