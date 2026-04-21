package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AimAssist extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public Setting speed = new Setting("Speed", 5.0, 0, 100);
    public Setting range = new Setting("Range", 4.0, 3.0, 6.0);

    public AimAssist() { 
        super("Aim Assist", -1);
        addSetting(speed);
        addSetting(range);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.player == null) return;
        
        PlayerEntity target = getTarget();
        if (target != null) {
            double diffX = target.getX() - mc.player.getX();
            double diffZ = target.getZ() - mc.player.getZ();
            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            
            float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
            
            // Polar Bypass: Hız yüksek olsa bile hareketi titreşimli (insansı) yapar
            double randomNoise = (Math.random() - 0.5) * 0.2; 
            float currentSpeed = (float) (speed.getValue() + randomNoise);
            
            if (Math.abs(yawDiff) > 0.1) {
                float move = MathHelper.clamp(yawDiff, -currentSpeed, currentSpeed);
                mc.player.setYaw(mc.player.getYaw() + move);
            }
        }
    }

    private PlayerEntity getTarget() {
        for (PlayerEntity e : mc.world.getPlayers()) {
            if (e != mc.player && e.isAlive() && mc.player.distanceTo(e) <= range.getValue()) return e;
        }
        return null;
    }
}
