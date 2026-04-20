package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import java.util.Random;

public class TriggerBot extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private long lastHit = 0;
    private final Random rand = new Random();
    public Setting ms = new Setting("MS", 120, 10, 1000);

    public TriggerBot() { 
        super("Trigger Bot", -1);
        addSetting(ms);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.targetedEntity == null) return;
        
        if (mc.targetedEntity instanceof PlayerEntity && mc.targetedEntity.isAlive()) {
            if (System.currentTimeMillis() - lastHit >= ms.getValue()) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis() + rand.nextInt(20); // Küçük randomize delay (Polar için)
            }
        }
    }
}
