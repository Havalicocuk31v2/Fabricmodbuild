package dev.lvstrng.argon.module;

public class Setting {
    public String name;
    public double value, min, max;
    public boolean enabled;
    public boolean isBoolean;
    public boolean dragging = false;

    // Sayısal ayar (Slider)
    public Setting(String name, double defaultValue, double min, double max) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.isBoolean = false;
    }

    // On/Off ayarı
    public Setting(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
        this.isBoolean = true;
    }

    // DERLEME HATASINI ÇÖZEN METODLAR
    public double getValue() { return value; }
    public boolean isEnabled() { return enabled; }

    public void setValue(double value) { 
        this.value = Math.round(Math.min(max, Math.max(min, value)) * 10.0) / 10.0; 
    }

    public void toggle() { if (isBoolean) this.enabled = !this.enabled; }
}
