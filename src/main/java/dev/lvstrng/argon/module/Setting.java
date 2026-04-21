package dev.lvstrng.argon.module;

public class Setting {
    public String name;
    public double value, min, max;
    public boolean enabled; // On/Off ayarları için
    public boolean isBoolean;
    public boolean dragging = false;

    // Sayısal ayar (Slider) için
    public Setting(String name, double defaultValue, double min, double max) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.isBoolean = false;
    }

    // On/Off ayarı için
    public Setting(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
        this.isBoolean = true;
    }

    public void setValue(double value) { 
        this.value = Math.round(Math.min(max, Math.max(min, value)) * 10.0) / 10.0; 
    }

    public void toggle() { if (isBoolean) this.enabled = !this.enabled; }
}
