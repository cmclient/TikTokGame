package pl.kuezese.tiktokgame.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;
import pl.kuezese.tiktokgame.object.Gift;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/tiktokgift" command in the TikTokGame plugin.
 * This command is used to process TikTok gift information.
 */
public class TikTokGiftCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new TikTokGiftCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public TikTokGiftCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/tiktokgift" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd    The executed command.
     * @param s      The command's label.
     * @param args   The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/tiktokgift <json>"));
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

        Gift gift = this.plugin.getGameController().getGifts().get(giftName);

        // Check if the specified gift exists in the config
        if (gift == null) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cGift " + giftName + " nie istnieje w configu (config.yml)."));
            return true;
        }

        // Broadcast the gift information to players based on config settings
        if (this.plugin.getConfiguration().isAnnounceGiftsOnChat()) {
            this.plugin.getServer().broadcastMessage(ChatHelper.format(this.plugin.getConfiguration().getGiftChatMessage()
                    .replace("{PREFIX}", this.plugin.getConfiguration().getGiftChatPrefix())
                    .replace("{PLAYER}", player)
                    .replace("{GIFT}", giftName)
                    .replace("{AMOUNT}", String.valueOf(amount))));
        }
        // Execute the gift title and  action if the game is started
        if (this.plugin.getGameController().isGameStarted()) {
            if (this.plugin.getConfiguration().isAnnounceGiftsOnTitle()) {
                this.plugin.getServer().getOnlinePlayers().forEach(other -> ChatHelper.title(other, this.plugin.getConfiguration().getGiftTitle(), this.plugin.getConfiguration().getGiftSubtitle()
                        .replace("{PLAYER}", player)
                        .replace("{GIFT}", giftName)
                        .replace("{AMOUNT}", String.valueOf(amount)), 5, 30, 5));
            }

            gift.execute(player, amount);
        }
        return true;
    }
}
