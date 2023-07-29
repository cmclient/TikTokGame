package pl.kuezese.tiktokgame.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;
import pl.kuezese.tiktokgame.object.Gift;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents the configuration settings for the TikTokGame plugin.
 * This class stores various configuration parameters that are loaded from the plugin's configuration file.
 */
public @Getter class Configuration {

    /**
     * Base variables
     */
    private final TikTokGame plugin;
    private final File scoresFile;

    /**
     * Main configuration settings.
     */
    private final String license; // The license key for the plugin.

    /**
     * Arena configuration settings.
     */
    private final int arenaSize; // The size of the game arena.
    private final int countdownTime; // Countdown time to end game when no mobs.

    /**
     * Mob spawning configuration settings.
     */
    private final int mobStartAmount; // The initial amount of mobs spawned in the arena.
    private final int mobSpawnRadius; // The radius around the spawn location for mob spawning.

    /**
     * Scoreboard configuration settings.
     */
    private final String scoreboardHeader; // The header/title of the scoreboard.
    private final List<String> scoreboardContents; // The contents of the scoreboard.

    /**
     * Chat configuration settings.
     */
    private final boolean showChatFromTiktok; // Determines if TikTok chat messages should be shown.
    private final boolean showJoinsFromTiktok; // Determines if TikTok joins should be shown.
    private final boolean showFollowsFromTiktok; // Determines if TikTok follows should be shown.
    private final boolean showLikesFromTiktok; // Determines if TikTok likes should be shown.

    /**
     * Gifts configuration settings.
     */
    private final boolean announceGiftsOnChat; // Determines if gift announcements should be displayed in chat.
    private final boolean announceGiftsOnTitle; // Determines if gift announcements should be displayed in titles.

    /**
     * Chat messages configuration settings.
     */
    private final String gamePrefix; // Prefix for game-related messages.
    private final String chatPrefix; // Prefix for chat messages.
    private final String chatFormat; // Format for chat messages.
    private final String gameReset;
    private final String gameStop;
    private final String playerJoin;
    private final String playerQuit;
    private final String giftChatPrefix; // Gift prefix for chat messages.
    private final String giftChatMessage;
    private final String giftTitle;
    private final String giftSubtitle;
    private final String countdownTitle;
    private final String countdownSubtitle;
    private final String countdownCancelTitle;
    private final String countdownCancelSubtitle;
    private final String playerWinTitle;
    private final String playerWinSubtitle;
    private final String playerLoseTitle;
    private final String playerLoseSubtitle;

    /**
     * Constructs the Configuration object by loading values from the plugin's configuration file.
     *
     * @param plugin The main instance of the TikTokGame plugin.
     */
    public Configuration(TikTokGame plugin) {
        this.plugin = plugin;
        this.scoresFile = new File(this.plugin.getDataFolder(), "scores.yml");

        // Create scores file if missing
        if (!this.scoresFile.exists()) {
            this.plugin.saveResource("scores.yml", false);
        }

        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        // Load base configuration
        this.license = config.getString("license");
        this.arenaSize = config.getInt("arena-size");
        this.countdownTime= config.getInt("countdown-time");
        this.mobStartAmount = config.getInt("mob-start-amount");
        this.mobSpawnRadius = config.getInt("mob-spawn-radius");
        this.scoreboardHeader = ChatHelper.format(config.getString("scoreboard-header"));
        this.scoreboardContents = ChatHelper.format(config.getStringList("scoreboard-contents"));
        this.showChatFromTiktok = config.getBoolean("show-chat-from-tiktok");
        this.showJoinsFromTiktok = config.getBoolean("show-joins-from-tiktok");
        this.showFollowsFromTiktok = config.getBoolean("show-follows-from-tiktok");
        this.showLikesFromTiktok = config.getBoolean("show-likes-from-tiktok");
        this.announceGiftsOnChat = config.getBoolean("announce-gifts-on-chat");
        this.announceGiftsOnTitle = config.getBoolean("announce-gifts-on-title");
        this.gamePrefix = config.getString("game-prefix");
        this.chatPrefix = config.getString("chat-prefix");
        this.chatFormat = config.getString("chat-format");
        this.gameReset = config.getString("game-reset");
        this.gameStop = config.getString("game-stop");
        this.playerJoin = config.getString("player-join");
        this.playerQuit = config.getString("player-quit");
        this.giftChatPrefix = config.getString("gift-chat-prefix");
        this.giftChatMessage = config.getString("gift-chat-message");
        this.giftTitle = config.getString("gift-title");
        this.giftSubtitle = config.getString("gift-subtitle");
        this.countdownTitle = config.getString("countdown-title");
        this.countdownSubtitle = config.getString("countdown-subtitle");
        this.countdownCancelTitle = config.getString("countdown-cancel-title");
        this.countdownCancelSubtitle = config.getString("countdown-cancel-subtitle");
        this.playerWinTitle = config.getString("player-win-title");
        this.playerWinSubtitle = config.getString("player-win-subtitle");
        this.playerLoseTitle = config.getString("player-lose-title");
        this.playerLoseSubtitle = config.getString("player-lose-subtitle");

        // Load gifts
        config.getConfigurationSection("gifts").getKeys(false).forEach(giftName -> {
            List<String> commands = config.getStringList("gifts." + giftName);
            Gift gift = new Gift(giftName, commands);
            plugin.getGameController().getGifts().put(giftName, gift);
        });

        // Load scores
        YamlConfiguration scoresConfiguration = YamlConfiguration.loadConfiguration(this.scoresFile);
        this.plugin.getGameController().setPlayerScore(scoresConfiguration.getInt("player"));
        this.plugin.getGameController().setViewerScore(scoresConfiguration.getInt("viewer"));
    }

    public void save() {
        // Save scores
        YamlConfiguration scoresConfiguration = YamlConfiguration.loadConfiguration(this.scoresFile);
        scoresConfiguration.set("player", this.plugin.getGameController().getPlayerScore());
        scoresConfiguration.set("viewer", this.plugin.getGameController().getViewerScore());
        try {
            scoresConfiguration.save(this.scoresFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to save configuration!", ex);
        }
    }
}
