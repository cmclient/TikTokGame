package pl.kuezese.tiktokgame.task;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.object.MobObject;

import java.util.Queue;

/**
 * A Bukkit task responsible for updating and managing scoreboards for players in the TikTokGame plugin.
 */
public class GameTask extends BukkitRunnable {

    private final TikTokGame plugin;

    /**
     * Constructs the ScoreBoardTask with the necessary dependencies.
     *
     * @param plugin The main plugin instance (TikTokGame).
     */
    public GameTask(TikTokGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Queue<MobObject> mobs = this.plugin.getGameController().getMobsQueue();

        if (!mobs.isEmpty()) {
            // Get the first player, if nobody offline - return
            Player player = this.plugin.getServer().getOnlinePlayers().stream().findAny().orElse(null);

            if (player == null)
                return;

            // Get the first mob object from the set
            MobObject mobObject = mobs.poll();

            // Get the mob type
            EntityType mobType = mobObject.getType();

            // Get the spawn location and spawn radius
            Location spawnLocation = player.getLocation();
            int spawnRadius = this.plugin.getConfiguration().getMobSpawnRadius();

            // Generate a random spawn location within the radius
            Location randomSpawnLocation = generateRandomLocation(spawnLocation, spawnRadius);

            // Spawn the mob at the random spawn location
            LivingEntity mob = (LivingEntity) randomSpawnLocation.getWorld().spawn(randomSpawnLocation, mobType.getEntityClass());

            if (mobObject.getName() != null) {
                mob.setCustomName(mobObject.getName());
                mob.setCustomNameVisible(true);
            }

            mob.getWorld().getPlayers().forEach(other -> {
                other.spawnParticle(Particle.SONIC_BOOM, mob.getLocation(), 2);
                other.playSound(mob.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            });
        }
    }

    // Helper method to generate a random location within a radius
    private Location generateRandomLocation(Location center, int radius) {
        double angle = Math.random() * Math.PI * 2;
        double x = center.getX() + (radius * Math.cos(angle));
        double z = center.getZ() + (radius * Math.sin(angle));

        // Get the highest non-air block Y-coordinate at the calculated X and Z
        double y = center.getWorld().getHighestBlockYAt((int) x, (int) z);

        // Adjust the Y-coordinate to be above the ground
        y += 1.0; // You can adjust this value based on your preference to raise or lower the spawn height

        return new Location(center.getWorld(), x, y, z);
    }
}
