package pl.kuezese.tiktokgame.object;

import lombok.Getter;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents a gift in the TikTokGame plugin, consisting of a name and a list of commands to be executed when the gift is given.
 */
public @Getter class Gift {

    private final String name;
    private final List<String> commands;

    /**
     * Constructs a new Gift with the given name and list of commands.
     *
     * @param name     The name of the gift.
     * @param commands The list of commands to be executed when the gift is given.
     */
    public Gift(String name, List<String> commands) {
        this.name = name;
        this.commands = commands;
    }

    /**
     * Executes the gift by running the associated commands a specified number of times.
     * @param gifter The nickname of the gifter.
     * @param amount The number of times the gift should be executed.
     */
    public void execute(@Nullable String gifter, int amount) {
        IntStream.range(0, amount).<Consumer<? super String>>mapToObj(i -> command -> {
            // Execute each command using the console sender
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{GIFTER}", gifter));
        }).forEach(this.commands::forEach);
    }
}
