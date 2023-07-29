package pl.kuezese.tiktokgame.command;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.kuezese.tiktokgame.TikTokGame;

import javax.annotation.Nonnull;

/**
 * A command executor for the "/heal" command in the TikTokGame plugin.
 * This command is used to heal all online players to their maximum health.
 */
public class HealCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new HealCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public HealCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/heal" command.
     *
     * @param sender The CommandSender who issued the command.
     * @param cmd The executed command.
     * @param s The command's label.
     * @param args The command arguments.
     * @return True if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        // Heal all online players to their maximum health
        this.plugin.getServer().getOnlinePlayers().forEach(player -> player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        return true;
    }
}
