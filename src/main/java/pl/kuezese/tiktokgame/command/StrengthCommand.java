package pl.kuezese.tiktokgame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;

import javax.annotation.Nonnull;

public class StrengthCommand implements CommandExecutor {

    private final TikTokGame plugin;

    /**
     * Constructs a new StrengthCommand.
     *
     * @param plugin The TikTokGame plugin instance.
     */
    public StrengthCommand(TikTokGame plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the "/strength" command.
     *
     * @param sender The CommandSender executing the command.
     * @param cmd    The Command that was executed.
     * @param s      The command label.
     * @param args   The arguments provided with the command.
     * @return true if the command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String s, @Nonnull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &7Poprawne uzycie: &c/addmaxhp <sekundy> <mnoznik>"));
            return true;
        }

        // Check if the game is started
        if (!this.plugin.getGameController().isGameStarted()) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cGra nie jest wystartowana."));
            return true;
        }

        int seconds = this.getInteger(args[0]);

        // Check if the provided amount is valid
        if (seconds <= 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodany czas jest nieprawidlowa."));
            return true;
        }

        int multiplier = this.getInteger(args[1]);

        // Check if the provided amount is valid
        if (multiplier <= 0) {
            sender.sendMessage(ChatHelper.format(this.plugin.getConfiguration().getGamePrefix() + " &cPodany mnoznik jest nieprawidlowa."));
            return true;
        }

        // Apply the strength effect to all online players
        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
            int currentTime = potionEffect == null ? 0 : potionEffect.getDuration();
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (seconds * 20) + currentTime, multiplier));
        });
        return true;
    }

    /**
     * Parses the input string and returns its integer value. If the input is not a valid integer, returns Integer.MIN_VALUE.
     *
     * @param arg The input string to be parsed as an integer.
     * @return The integer value of the input string, or Integer.MIN_VALUE if it's not a valid integer.
     */
    private int getInteger(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }
}
