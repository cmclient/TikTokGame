package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/stopgame" command in the TikTokGame plugin.
 * This command is used to manually stop the ongoing game.
 */
public class StopGameCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new StopGameCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public StopGameCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/stopgame" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Call the GameController's stop method to stop the ongoing game
        this.plugin.getGameController().stop();
        return true;
    }
}
