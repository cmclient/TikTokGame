package pl.kuezese.tiktokgame.task;

import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

/**
 * A task that handles the countdown logic and game progression in the TikTokGame plugin.
 */
public class CountdownTask extends BukkitRunnable {

    private final TikTokGame plugin;

    /**
     * Constructs a new CountdownTask.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public CountdownTask(TikTokGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Check if the game is started
        if (this.plugin.getGameController().isGameStarted()) {
            int throttle = this.plugin.getGameController().getCountdownThrottle();

            // Throttle the countdown if needed
            if (throttle > 0) {
                this.plugin.getGameController().setCountdownThrottle(--throttle);
                return;
            }

            // Count the number of Monster entities in the world
            long mobs = this.plugin.getServer().getWorlds().get(0).getEntities().stream().filter(entity -> entity instanceof Monster).count();
            int countdown = this.plugin.getGameController().getCountdown();
            boolean isCountingDown = countdown != -1;

            // Handle scenarios based on countdown status and mob presence
            if (isCountingDown && mobs != 0) {
                this.plugin.getGameController().setCountdown(-1);
                return;
            }

            if (!isCountingDown && mobs == 0) {
                this.plugin.getGameController().setCountdown(this.plugin.getConfiguration().getCountdownTime());
                return;
            }

            if (isCountingDown) {
                countdown--;

                // Check if the countdown has reached 0
                if (countdown == 0) {
                    // Display titles to players, run the GameEndTask, update player score, and stop the game
                    this.plugin.getGameController().setPlayerScore(this.plugin.getGameController().getPlayerScore() + 1);
                    this.plugin.getGameController().stop();
                    this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                        ChatHelper.title(player, this.plugin.getConfiguration().getPlayerWinTitle(), this.plugin.getConfiguration().getPlayerWinSubtitle(), 10, 80, 10);
                        new GameEndTask(player).runTaskTimer(this.plugin, 5L, 5L);
                        player.setVelocity(new Vector(0.0D, 2.0D, 0.0D));
                    });
                    return;
                }

                // Update the countdown and display titles to players
                this.plugin.getGameController().setCountdown(countdown);
                this.plugin.getServer().getOnlinePlayers().forEach(player -> ChatHelper.title(player, this.plugin.getConfiguration().getCountdownTitle(), this.plugin.getConfiguration().getCountdownSubtitle().replace("{TIME}", String.valueOf(this.plugin.getGameController().getCountdown())), 5, 20, 5));
            }
        }
    }
}
