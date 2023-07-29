package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/tiktokchat" command in the TikTokGame plugin.
 * This command is used to broadcast TikTok chat messages.
 */
public class TikTokChatCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new TikTokChatCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public TikTokChatCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/tiktokchat" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Check if showing TikTok chat messages is enabled
        if (!this.plugin.getConfiguration().isShowChatFromTiktok()) return true;

        if (args.length < 2) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/tiktokchat <uzytkownik> <wiadomosc>"));
            return true;
        }

        String player = args[0];
        String message = ChatHelper.join(args, " ", 1, args.length);

        // Broadcast the TikTok chat message to all players
        this.plugin.getServer().broadcastMessage(
                ChatHelper.format(this.plugin.getConfiguration().getChatFormat()
                        .replace("{PREFIX}", this.plugin.getConfiguration().getChatPrefix())
                        .replace("{PLAYER}", player)
                ).replace("{MESSAGE}", message));
        return true;
    }
}
