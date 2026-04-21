package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AimAssist extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public Setting speed = new Setting("Speed", 5.0, 0, 100);
    public Setting range = new Setting("Range", 4.2, 3.0, 6.0);
    public Setting onHead = new Setting("On the Head", true);
    public Setting onChest = new Setting("On the Chest", false);

    public AimAssist() { 
        super("Aim Assist", -1); // Varsayılan tuş yok
        addSetting(speed);
        addSetting(range);
        addSetting(onHead);
        addSetting(onChest);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.player == null) return;
        
        PlayerEntity target = getTarget();
        if (target != null) {
            // Hedef Noktası Hesaplama (Head vs Chest)
            double targetY = target.getY();
            if (onHead.enabled) targetY = target.getEyeY() - 0.1;
            else if (onChest.enabled) targetY = target.getY() + (target.getHeight() / 1.5);

            double diffX = target.getX() - mc.player.getX();
            double diffZ = target.getZ() - mc.player.getZ();
            double diffY = targetY - mc.player.getEyeY();
            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            float targetPitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(diffY, diffXZ)));

            // SIFIR TİTREME (Smooth Logic)
            float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
            float pitchDiff = MathHelper.wrapDegrees(targetPitch - mc.player.getPitch());

            float currentSpeed = (float) (speed.getValue() * 0.1); // Ultra hassas kontrol

            if (Math.abs(yawDiff) > 0.05) {
                mc.player.setYaw(mc.player.getYaw() + MathHelper.clamp(yawDiff, -currentSpeed, currentSpeed));
            }
            if (Math.abs(pitchDiff) > 0.05) {
                mc.player.setPitch(mc.player.getPitch() + MathHelper.clamp(pitchDiff, -currentSpeed, currentSpeed));
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
