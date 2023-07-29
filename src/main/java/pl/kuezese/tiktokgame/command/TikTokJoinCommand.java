package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/tiktokjoin" command in the TikTokGame plugin.
 * This command is used to broadcast TikTok user joins.
 */
public class TikTokJoinCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new TikTokJoinCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public TikTokJoinCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/tiktokjoin" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Check if showing TikTok joins is enabled
        if (!this.plugin.getConfiguration().isShowJoinsFromTiktok()) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/tiktokjoin <uzytkownik>"));
            return true;
        }

        String player = args[0];
        // Broadcast the TikTok user join to all players
        this.plugin.getServer().broadcastMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &a" + player + " &7dołączył."));
        return true;
    }
}
