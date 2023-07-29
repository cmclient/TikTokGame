package pl.kuezese.tiktokgame.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;
import pl.kuezese.tiktokgame.task.GameEndTask;

/**
 * A listener that handles player death events in the TikTokGame plugin.
 */
public class PlayerDeathListener implements Listener {

    private final TikTokGame plugin;

    /**
     * Constructs a new PlayerDeathListener.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public PlayerDeathListener(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player death event.
     *
     * @param event The PlayerDeathEvent instance.
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        Player entity = event.getEntity();

        // Delay the player respawn and set velocity to create the "bouncing" effect
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            this.plugin.getGameController().join(entity);
            entity.setVelocity(new Vector(0.0D, 2.0D, 0.0D));
        }, 1L);

        // Check if the game is started
        if (this.plugin.getGameController().isGameStarted()) {
            // Display titles to players, run the GameEndTask, update viewer score, and stop the game
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                ChatHelper.title(player, this.plugin.getConfiguration().getPlayerLoseTitle(), this.plugin.getConfiguration().getPlayerLoseSubtitle(), 10, 80, 10);
                new GameEndTask(player).runTaskTimer(this.plugin, 5L, 5L);
            });
            this.plugin.getGameController().setViewerScore(this.plugin.getGameController().getViewerScore() + 1);
            this.plugin.getGameController().stop();
        }
    }
}
