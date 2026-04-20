package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AimAssist extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public Setting speed = new Setting("Speed", 2.0, 0, 20); // 100 çok fazla, 20 idealdir
    public Setting range = new Setting("Range", 4.2, 3.0, 6.0);

    public AimAssist() { 
        super("Aim Assist", -1);
        addSetting(speed);
        addSetting(range);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.player == null) return;
        
        Entity target = getTarget();
        if (target != null) {
            double diffX = target.getX() - mc.player.getX();
            double diffZ = target.getZ() - mc.player.getZ();
            float targetYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            
            float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
            float step = (float) speed.getValue();
            
            if (Math.abs(yawDiff) > 0.1) {
                mc.player.setYaw(mc.player.getYaw() + MathHelper.clamp(yawDiff, -step, step));
            }
        }
    }

    private Entity getTarget() {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && e != mc.player && e.isAlive() && mc.player.distanceTo(e) <= range.getValue()) return e;
        }
        return null;
    }
}
