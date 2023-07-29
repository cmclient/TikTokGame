package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/score" command in the TikTokGame plugin.
 * This command is used to update various game settings.
 */
public class ScoreCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new ScoreCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public ScoreCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/score" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/score <player/viewer> <wartosc>"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "player":
                int amount = this.getInteger(args[1]);

                if (amount <= 0) {
                    sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodana ilosc jest nieprawidlowa."));
                    return true;
                }

                this.plugin.getGameController().setPlayerScore(amount);
                this.plugin.getConfiguration().save();
                break;
            case "viewer":
                int amount1 = this.getInteger(args[1]);

                if (amount1 <= 0) {
                    sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodana ilosc jest nieprawidlowa."));
                    return true;
                }

                this.plugin.getGameController().setViewerScore(amount1);
                this.plugin.getConfiguration().save();
                break;
            default:
                sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/score <player/viewer> <wartosc>"));
        }
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
