package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;
import pl.kuezese.tiktokgame.object.MobObject;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/addmob" command in the TikTokGame plugin.
 * This command is used to add mobs to the game queue to be spawned later.
 */
public class AddMobCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new AddMobCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public AddMobCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/addmob" command.
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
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/addmob <typ> <ilosc> [gifter]"));
            return true;
        }

        // Check if the game is started
        if (!this.plugin.getGameController().isGameStarted()) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cGra nie jest wystartowana."));
            return true;
        }

        EntityType type = EntityType.fromName(args[0]);

        // Check if the provided mob type is valid
        if (type == null) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodany typ moba nie istnieje."));
            return true;
        }

        int amount = this.getInteger(args[1]);

        // Check if the provided amount is valid
        if (amount <= 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodana ilosc jest nieprawidlowa."));
            return true;
        }

        // Check if a gifter was provided
        String gifter = args.length == 2 ? null : args[2];

        // Add the specified number of mobs to the game queue
        for (int i = 0; i < amount; i++) {
            this.plugin.getGameController().getMobsQueue().add(new MobObject(type, gifter));
        }
        return true;
    }

    private int getInteger(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }
}
