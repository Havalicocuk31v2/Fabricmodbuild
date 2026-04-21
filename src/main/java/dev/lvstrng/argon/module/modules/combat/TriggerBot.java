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
    public Setting ms = new Setting("MS Delay", 100, 10, 500);

    public TriggerBot() { 
        super("Trigger Bot", -1);
        addSetting(ms);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled() || mc.currentScreen != null || mc.player == null) return;
        
        if (mc.targetedEntity instanceof PlayerEntity target && target.isAlive()) {
            // Senin MS ayarın + %5-10 arası çok küçük sapma
            long baseDelay = (long) ms.getValue();
            long dynamicDelay = baseDelay + rand.nextInt(Math.max(1, (int)(baseDelay * 0.1)));
            
            if (System.currentTimeMillis() - lastHit >= dynamicDelay) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis();
            }
        }
    }
}
