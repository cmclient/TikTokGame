package pl.kuezese.tiktokgame.task;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Bukkit task responsible for updating and managing scoreboards for players in the TikTokGame plugin.
 */
public class ScoreBoardTask extends BukkitRunnable {

    private final TikTokGame plugin;
    private final Netherboard netherboard;
    private final ConcurrentHashMap<Player, BPlayerBoard> boards;

    /**
     * Constructs the ScoreBoardTask with the necessary dependencies.
     *
     * @param plugin The main plugin instance (TikTokGame).
     */
    public ScoreBoardTask(TikTokGame plugin) {
        this.plugin = plugin;
        this.netherboard = Netherboard.instance();
        this.boards = new ConcurrentHashMap<>();
    }

    /**
     * Runs the scoreboard update task. It creates scoreboards for uninitialized players, removes scoreboards
     * for offline players, and updates the scoreboards for online players with the latest data.
     */
    @Override
    public void run() {
        // Create scoreboards for uninitialized players
        this.plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(player -> !boards.containsKey(player))
                .forEach(player -> {
                    BPlayerBoard board = this.netherboard.createBoard(player, this.plugin.getConfiguration().getScoreboardHeader());
                    boards.put(player, board);
                });

        // Remove scoreboards for offline players
        this.boards.entrySet().stream().filter(entry -> !entry.getKey().isOnline()).forEach(entry -> entry.getValue().delete());
        this.boards.entrySet().removeIf(entry -> !entry.getKey().isOnline());

        // Update scoreboards for online players
        this.boards.forEach((player, board) -> {
            List<String> contents = ChatHelper.format(this.plugin.getConfiguration().getScoreboardContents());
            ChatHelper.replace(contents, "{PLAYER-SCORE}", String.valueOf(this.plugin.getGameController().getPlayerScore()));
            ChatHelper.replace(contents, "{VIEWER-SCORE}", String.valueOf(this.plugin.getGameController().getViewerScore()));
            ChatHelper.replace(contents, "{MOBS}", String.valueOf(player.getWorld().getEntities().stream().filter(entity -> entity instanceof Monster).count()));
            ChatHelper.replace(contents, "{LAST-GIFTER}", this.plugin.getGameController().getLastGifter());
            ChatHelper.replace(contents, "{LAST-FOLLOWER}", this.plugin.getGameController().getLastFollower());
            ChatHelper.replace(contents, "{LIKES}", String.valueOf(this.plugin.getGameController().getLikes()));
            ChatHelper.replace(contents, "{VIEWERS}", String.valueOf(this.plugin.getGameController().getViewers()));
            board.setAll(contents.toArray(new String[0]));
        });
    }
}