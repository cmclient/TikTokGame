package pl.kuezese.tiktokgame.command;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/addmaxhp" command in the TikTokGame plugin.
 * This command is used to add maximum health (max HP) to all online players.
 */
public class AddMaxHpCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new AddMaxHpCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public AddMaxHpCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/addmaxhp" command.
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
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/addmaxhp <ilosc>"));
            return true;
        }

        // Check if the game is started
        if (!this.plugin.getGameController().isGameStarted()) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cGra nie jest wystartowana."));
            return true;
        }

        int amount = this.getInteger(args[0]);

        // Check if the provided amount is valid
        if (amount <= 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodana ilosc jest nieprawidlowa."));
            return true;
        }

        // Increase maximum health for all online players
        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            AttributeInstance genericMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            genericMaxHealth.setBaseValue(genericMaxHealth.getBaseValue() + (amount * 2.0D));
            player.setHealth(player.getHealth() + (amount * 2.0D));
        });
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
