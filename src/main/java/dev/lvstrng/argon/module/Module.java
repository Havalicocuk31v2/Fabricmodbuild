package dev.lvstrng.argon.module;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String name;
    private int key;
    private boolean enabled;
    private List<Setting> settings = new ArrayList<>(); // Ayarları tutan liste

    public Module(String name, int key) {
        this.name = name;
        this.key = key;
    }

    public void addSetting(Setting s) { settings.add(s); }
    public List<Setting> getSettings() { return settings; }
    
    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { this.enabled = !this.enabled; }
    
    public void onTick() {}
}
