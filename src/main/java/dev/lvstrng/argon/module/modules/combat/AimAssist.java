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

    public AimAssist() { 
        super("Aim Assist", -1);
        addSetting(speed);
        addSetting(range);
        addSetting(onHead);
    }

    @Override
    public void onTick() {
        // isWorking: Tuşla Toggle yapıldıysa çalışır
        if (!this.isEnabled() || !this.isWorking() || mc.currentScreen != null || mc.player == null) return;
        
        PlayerEntity target = getTarget();
        if (target != null) {
            double targetY = onHead.isEnabled() ? target.getEyeY() - 0.12 : target.getY() + (target.getHeight() / 1.5);
            
            double diffX = target.getX() - mc.player.getX();
            double diffZ = target.getZ() - mc.player.getZ();
            double diffY = targetY - mc.player.getEyeY();
            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            float targetPitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(diffY, diffXZ)));

            float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
            float pitchDiff = MathHelper.wrapDegrees(targetPitch - mc.player.getPitch());

            // TİTREME SIFIR: Exponential smoothing algoritması
            double smoothing = Math.max(1.0, 20.0 - (speed.getValue() / 5.0)); 
            
            if (Math.abs(yawDiff) > 0.01) {
                mc.player.setYaw((float) (mc.player.getYaw() + (yawDiff / smoothing)));
            }
            if (Math.abs(pitchDiff) > 0.01) {
                mc.player.setPitch((float) (mc.player.getPitch() + (pitchDiff / smoothing)));
            }
        }
    }

    private PlayerEntity getTarget() {
        for (PlayerEntity e : mc.world.getPlayers()) {
            if (e != mc.player && e.isAlive() && mc.player.canSee(e) && mc.player.distanceTo(e) <= range.getValue()) return e;
        }
        return null;
    }
}
