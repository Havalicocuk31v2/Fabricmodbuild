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
    
    public Setting msDelay = new Setting("MS", 100, 1, 1000);

    public TriggerBot() { 
        super("Trigger Bot", -1);
        addSetting(msDelay);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen != null || mc.targetedEntity == null || mc.player == null) return;
        if (mc.targetedEntity instanceof PlayerEntity && mc.targetedEntity.isAlive()) {
            // MS ayarı + Randomize sapma (Legit C2S)
            long currentDelay = (long) (msDelay.getValue() + rand.nextInt(50));
            if (System.currentTimeMillis() - lastHit >= currentDelay) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis();
            }
        }
    }
}
