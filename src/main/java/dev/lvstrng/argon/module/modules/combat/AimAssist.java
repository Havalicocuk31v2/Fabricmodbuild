package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AimAssist extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public float speed = 1.2f;
    public double range = 4.2;

    public AimAssist() { super("Aim Assist", -1); }

    @Override
    public void onTick() {
        if (mc.currentScreen != null || mc.player == null || mc.world == null) return;
        Entity target = getTarget();
        if (target != null) {
            Vec3d targetPos = target.getPos().add(0, target.getStandingEyeHeight() - 0.15, 0);
            Vec3d playerPos = mc.player.getEyePos();
            
            double diffX = targetPos.x - playerPos.x;
            double diffY = targetPos.y - playerPos.y;
            double diffZ = targetPos.z - playerPos.z;
            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            float targetPitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(diffY, diffXZ)));

            mc.player.setYaw(lerpAngle(mc.player.getYaw(), targetYaw, speed));
            mc.player.setPitch(lerpAngle(mc.player.getPitch(), targetPitch, speed));
        }
    }

    private float lerpAngle(float start, float end, float step) {
        float diff = MathHelper.wrapDegrees(end - start);
        return start + MathHelper.clamp(diff, -step, step);
    }

    private Entity getTarget() {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && e != mc.player && e.isAlive() && mc.player.distanceTo(e) <= range) return e;
        }
        return null;
    }
}
