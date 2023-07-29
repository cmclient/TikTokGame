package pl.kuezese.tiktokgame.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

/**
 * A listener that handles player join and quit events in the TikTokGame plugin.
 */
public class PlayerJoinListener implements Listener {

    private final TikTokGame plugin;

    /**
     * Constructs a new PlayerJoinListener.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public PlayerJoinListener(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player join event.
     *
     * @param event The PlayerJoinEvent instance.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Set a custom join message
        event.setJoinMessage(ChatHelper.format(this.plugin.getConfiguration().getPlayerJoin()
                .replace("{PREFIX}", this.plugin.getConfiguration().getGamePrefix())
                .replace("{PLAYER}", player.getName())));
        // Let the GameController know that the player joined
        this.plugin.getGameController().join(player);
    }

    /**
     * Handles the player quit event.
     *
     * @param event The PlayerQuitEvent instance.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Set a custom quit message
        event.setQuitMessage(ChatHelper.format(this.plugin.getConfiguration().getPlayerQuit()
                .replace("{PREFIX}", this.plugin.getConfiguration().getGamePrefix())
                .replace("{PLAYER}", player.getName())));
    }
}
