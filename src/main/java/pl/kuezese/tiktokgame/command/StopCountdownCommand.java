package pl.kuezese.tiktokgame.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/stopcountdown" command in the TikTokGame plugin.
 * This command is used to manually stop the countdown during the game.
 */
public class StopCountdownCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new StopCountdownCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public StopCountdownCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/stopcountdown" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/stopcountdown <json>"));
            return true;
        }

        // Check if the game is started
        if (!this.plugin.getGameController().isGameStarted()) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cGra nie jest wystartowana."));
            return true;
        }

        // Check if there is an ongoing countdown
        if (this.plugin.getGameController().getCountdown() == -1) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cAktualnie nie ma odliczania."));
            return true;
        }

        String json = String.join(" ", args);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        // Check if the required fields are present in the JSON
        if (!jsonObject.has("player") || !jsonObject.has("giftName") || !jsonObject.has("amount")) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cNieprawidlowy json."));
            return true;
        }

        String player = jsonObject.get("player").getAsString();
        String giftName = jsonObject.get("giftName").getAsString();
        int amount = jsonObject.get("amount").getAsInt();

        // Stop the countdown and set the countdown throttle
        this.plugin.getGameController().setCountdown(-1);
        this.plugin.getGameController().setCountdownThrottle(3);
        // Broadcast the message about the countdown being stopped by the specified player
        this.plugin.getServer().getOnlinePlayers().forEach(other -> ChatHelper.title(other, this.plugin.getConfiguration().getCountdownCancelTitle(), this.plugin.getConfiguration().getCountdownCancelSubtitle()
                .replace("{PLAYER}", player)
                .replace("{GIFT}", giftName)
                .replace("{AMOUNT}", String.valueOf(amount)), 5, 20, 5));
        return true;
    }
}
