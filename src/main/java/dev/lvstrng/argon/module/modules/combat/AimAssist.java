package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AimAssist extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public Setting speed = new Setting("Speed", 1.2, 0, 100);
    public Setting smoothness = new Setting("Smoothness", 3.0, 1, 10);
    public double range = 4.2;

    public AimAssist() { 
        super("Aim Assist", -1);
        addSetting(speed);
        addSetting(smoothness);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen != null || mc.player == null || mc.world == null) return;
        Entity target = getTarget();
        if (target != null) {
            Vec3d targetPos = target.getPos().add(0, target.getStandingEyeHeight() - 0.15, 0);
            Vec3d playerPos = mc.player.getEyePos();
            
            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(targetPos.z - playerPos.z, targetPos.x - playerPos.x)) - 90.0);
            float targetPitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(targetPos.y - playerPos.y, Math.sqrt(Math.pow(targetPos.x - playerPos.x, 2) + Math.pow(targetPos.z - playerPos.z, 2)))));

            // Polar Bypass Smooth Logic: Titremeyi engeller
            float softSpeed = (float) (speed.getValue() / (smoothness.getValue() * 10));
            mc.player.setYaw(mc.player.getYaw() + MathHelper.clamp(MathHelper.wrapDegrees(targetYaw - mc.player.getYaw()), -softSpeed, softSpeed));
            mc.player.setPitch(mc.player.getPitch() + MathHelper.clamp(MathHelper.wrapDegrees(targetPitch - mc.player.getPitch()), -softSpeed, softSpeed));
        }
    }

    private Entity getTarget() {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && e != mc.player && e.isAlive() && mc.player.distanceTo(e) <= range) return e;
        }
        return null;
    }
}
