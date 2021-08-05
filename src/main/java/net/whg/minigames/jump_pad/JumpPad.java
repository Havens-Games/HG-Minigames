package net.whg.minigames.jump_pad;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.events.JoinMinigameEvent;

/**
 * A lobby game that involves giving all players jump boast and knock back
 * sticks, then letting them all jump around on slime blocks for a while.
 */
public class JumpPad extends Minigame {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoinMinigame(JoinMinigameEvent e) {
        if (e.getMinigame() != this)
            return;

        var player = e.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 7, true, false, true));
    }
}
