package dev.lvstrng.argon.module;
import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private String name;
    private int key;
    private boolean enabled;

    public Module(String name, int key) {
        this.name = name;
        this.key = key;
        this.enabled = false;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public void onTick() {}
    public void onEnable() {}
    public void onDisable() {}
    public String getName() { return name; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public boolean isEnabled() { return enabled; }
}