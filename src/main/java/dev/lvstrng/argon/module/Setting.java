package dev.lvstrng.argon.module;

public class Setting {
    public String name;
    public double value, min, max;
    public boolean dragging = false; // Slider sürükleme durumu

    public Setting(String name, double defaultValue, double min, double max) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
    }

    public double getValue() { return value; }
    public void setValue(double value) { 
        this.value = Math.round(Math.min(max, Math.max(min, value)) * 10.0) / 10.0; 
    }
}
