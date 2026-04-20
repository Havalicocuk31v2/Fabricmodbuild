package dev.lvstrng.argon;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.combat.*;
import dev.lvstrng.argon.ui.ClickGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class Argon implements ClientModInitializer {
    public static List<Module> modules = new ArrayList<>();
    public static ClickGui gui;

    @Override
    public void onInitializeClient() {
        modules.add(new AimAssist());
        modules.add(new TriggerBot());
        gui = new ClickGui();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            // J tuşu menüyü açar
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_J) == GLFW.GLFW_PRESS) {
                if (!(client.currentScreen instanceof ClickGui)) client.setScreen(gui);
            }

            for (Module m : modules) if (m.isEnabled()) m.onTick();
        });
    }
}