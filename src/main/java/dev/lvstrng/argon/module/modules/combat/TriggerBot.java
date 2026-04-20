package dev.lvstrng.argon.module.modules.combat;
import dev.lvstrng.argon.module.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import java.util.Random;

public class TriggerBot extends Module {
    private long lastHit = 0;
    private final Random rand = new Random();

    public TriggerBot() { super("Trigger Bot", GLFW.GLFW_KEY_NONE); }

    @Override
    public void onTick() {
        if (mc.currentScreen != null || mc.targetedEntity == null) return;
        if (mc.targetedEntity instanceof PlayerEntity && mc.targetedEntity.isAlive()) {
            // C2S Legit Timing (10-14 CPS arası randomize)
            long delay = 1000 / (10 + rand.nextInt(4)); 
            if (System.currentTimeMillis() - lastHit >= delay) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastHit = System.currentTimeMillis();
            }
        }
    }
}