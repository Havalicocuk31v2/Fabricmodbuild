package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import java.util.Random;

public class TriggerBot extends Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private long lastHit = 0;
    private final Random rand = new Random();

    public TriggerBot() { super("Trigger Bot", -1); }

    @Override
    public void onTick() {
        if (mc.currentScreen != null || mc.targetedEntity == null || mc.player == null || mc.interactionManager == null) return;
        if (mc.targetedEntity instanceof PlayerEntity && mc.targetedEntity.isAlive()) {
            long delay = 1000 / (10 + rand.nextInt(4)); 
            if (System.currentTimeMillis() - lastHit >= delay) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis();
            }
        }
    }
}
