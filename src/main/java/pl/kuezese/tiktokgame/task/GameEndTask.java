package pl.kuezese.tiktokgame.task;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A task that handles the spawning of a Firework effect when the game ends.
 */
public class GameEndTask extends BukkitRunnable {

    private final Player player;
    private int time;

    /**
     * Constructs a new GameEndTask.
     *
     * @param player The player for whom the Firework will be spawned.
     */
    public GameEndTask(Player player) {
        this.player = player;
        this.time = 200;
    }

    @Override
    public void run() {
        // Check if the task's time has expired or if the player is no longer online.
        if (this.time <= 0 || !this.player.isOnline()) {
            this.cancel();
            return;
        }

        // Decrement the time and spawn a Firework.
        this.time -= 10;
        this.spawnFirework(this.player.getLocation());
    }

    /**
     * Spawns a Firework at the given location.
     *
     * @param location The location where the Firework will be spawned.
     */
    private void spawnFirework(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect.Builder effect = FireworkEffect.builder();

        // Set the firework's color
        effect.withColor(Color.BLUE);
        effect.withFade(Color.RED);

        // Choose the firework's shape
        effect.with(FireworkEffect.Type.BALL);

        // Set other properties
        fireworkMeta.addEffect(effect.build());
        fireworkMeta.setPower(1); // The power of the firework (how high it goes)

        firework.setFireworkMeta(fireworkMeta);
    }
}
