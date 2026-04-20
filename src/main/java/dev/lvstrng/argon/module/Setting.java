package dev.lvstrng.argon.module;

public class Setting {
    private final String name;
    private double value;
    private final double min, max;

    public Setting(String name, double defaultValue, double min, double max) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
    }

    public String getName() { return name; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = Math.min(max, Math.max(min, value)); }
}
