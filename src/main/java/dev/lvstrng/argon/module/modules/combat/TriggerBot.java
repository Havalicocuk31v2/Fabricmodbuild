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
    public Setting ms = new Setting("MS Delay", 100, 10, 1000); // 1000'e çıkarıldı

    public TriggerBot() { 
        super("Trigger Bot", -1);
        addSetting(ms);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.player == null) return;
        
        if (mc.targetedEntity instanceof PlayerEntity target && target.isAlive()) {
            // Polar Bypass: Dinamik MS sapması
            long dynamicDelay = (long) (ms.getValue() + rand.nextInt(30));
            
            if (System.currentTimeMillis() - lastHit >= dynamicDelay) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis();
            }
        }
    }
}
