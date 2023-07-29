package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/reset" command in the TikTokGame plugin.
 * This command is used to reset the game and clear its state.
 */
public class ResetCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new ResetCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public ResetCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/reset" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Call the GameController's reset method to reset the game state
        this.plugin.getGameController().reset();
        return true;
    }
}
