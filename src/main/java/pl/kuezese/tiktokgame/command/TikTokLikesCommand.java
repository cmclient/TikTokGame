package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/tiktoklikes" command in the TikTokGame plugin.
 * This command is used to broadcast TikTok likes information.
 */
public class TikTokLikesCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new TikTokLikesCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public TikTokLikesCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/tiktoklikes" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Check if showing TikTok likes is enabled
        if (!this.plugin.getConfiguration().isShowLikesFromTiktok()) return true;

        if (args.length < 3) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/tiktoklikes <uzytkownik> <ilosc> <ogolna ilosc>"));
            return true;
        }

        String player = args[0];
        int amount = this.getInteger(args[1]);
        int total = this.getInteger(args[2]);

        // Broadcast the TikTok likes information to all players
        this.plugin.getServer().broadcastMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &a" + player + " &7wysłał &a" + amount + " polubień&7, łącznie jest już &a" + total + " polubień&7."));
        return true;
    }

    /**
     * Converts a string argument to an integer.
     * If the conversion fails, returns Integer.MIN_VALUE.
     *
     * @param arg The string argument to convert.
     * @return The integer value of the argument, or Integer.MIN_VALUE if the conversion fails.
     */
    private int getInteger(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }
}
