package pl.kuezese.tiktokgame;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kuezese.tiktokgame.command.*;
import pl.kuezese.tiktokgame.config.Configuration;
import pl.kuezese.tiktokgame.game.GameController;
import pl.kuezese.tiktokgame.listener.PlayerDeathListener;
import pl.kuezese.tiktokgame.listener.PlayerJoinListener;
import pl.kuezese.tiktokgame.task.CountdownTask;
import pl.kuezese.tiktokgame.task.GameTask;
import pl.kuezese.tiktokgame.task.ScoreBoardTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;

/**
 * The main class of the TikTokGame plugin.
 * This class extends JavaPlugin and handles plugin initialization and event registration.
 */
public final @Getter class TikTokGame extends JavaPlugin {

    private GameController gameController; // The game controller that manages the game logic.
    private Configuration configuration; // The configuration settings for the plugin.

    /**
     * Called when the plugin is enabled. Performs plugin setup, command and listener registration, and task scheduling.
     */
    @Override
    public void onEnable() {
        // Configure server game rules
        World defaultWorld = Bukkit.getWorlds().get(0);
        defaultWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        defaultWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        defaultWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        defaultWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);

        // Declare variable if modified settings
        boolean modifiedSettings = false;

        // Configure server settings
        try (InputStream in = Files.newInputStream(new File(getServer().getWorldContainer(), "server.properties").toPath())) {
            Properties properties = new Properties();
            properties.load(in);

            if (!properties.getProperty("allow-nether").equals("false")) {
                properties.setProperty("allow-nether", "false");
                this.getLogger().info("Modified server.properties -> allow-nether to false");
                modifiedSettings = true;
            }

            if (!properties.getProperty("spawn-npcs").equals("false")) {
                properties.setProperty("spawn-npcs", "false");
                this.getLogger().info("Modified server.properties -> spawn-npcs to false");
                modifiedSettings = true;
            }

            if (!properties.getProperty("spawn-animals").equals("false")) {
                properties.setProperty("spawn-animals", "false");
                this.getLogger().info("Modified server.properties -> spawn-animals to false");
                modifiedSettings = true;
            }

            if (!properties.getProperty("spawn-monsters").equals("false")) {
                properties.setProperty("spawn-monsters", "false");
                this.getLogger().info("Modified server.properties -> spawn-monsters to false");
                modifiedSettings = true;
            }

            if (modifiedSettings) {
                try (OutputStream out = Files.newOutputStream(new File(getServer().getWorldContainer(), "server.properties").toPath())) {
                    properties.store(out, "Minecraft server properties");
                }
            }
        } catch (IOException ex) {
            this.getLogger().log(Level.WARNING, "Failed to modify server.properties");
        }

        // Configure bukkit settings
        FileConfiguration bukkit = YamlConfiguration.loadConfiguration(new File(getServer().getWorldContainer(), "bukkit.yml"));

        if (bukkit.getBoolean("settings.allow-end")) {
            bukkit.set("settings.allow-end", false);
            this.getLogger().info("Modified bukkit.yml -> settings.allow-end to false");
            modifiedSettings = true;
        }

        if (bukkit.getInt("settings.connection-throttle") != -1) {
            bukkit.set("settings.connection-throttle", -1);
            this.getLogger().info("Modified bukkit.yml -> settings.connection-throttle to -1");
            modifiedSettings = true;
        }

        if (bukkit.getBoolean("query-plugins")) {
            bukkit.set("settings.query-plugins", false);
            this.getLogger().info("Modified bukkit.yml -> settings.query-plugins to false");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.monsters") != 0) {
            bukkit.set("spawn-limits.monsters", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.monsters to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.animals") != 0) {
            bukkit.set("spawn-limits.animals", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.animals to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.water-animals") != 0) {
            bukkit.set("spawn-limits.water-animals", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.water-animals to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.water-ambient") != 0) {
            bukkit.set("spawn-limits.water-ambient", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.water-ambient to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.water-underground-creature") != 0) {
            bukkit.set("spawn-limits.water-underground-creature", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.water-underground-creature to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.axolotls") != 0) {
            bukkit.set("spawn-limits.axolotls", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.axolotls to 0");
            modifiedSettings = true;
        }

        if (bukkit.getInt("spawn-limits.ambient") != 0) {
            bukkit.set("spawn-limits.ambient", 0);
            this.getLogger().info("Modified bukkit.yml -> spawn-limits.ambient to 0");
            modifiedSettings = true;
        }

        if (modifiedSettings) {
            try {
                bukkit.save(new File(getServer().getWorldContainer(), "bukkit.yml"));
            } catch (IOException ex) {
                this.getLogger().log(Level.WARNING, "Failed to save bukkit.yml");

            }
            this.getServer().getLogger().info("Modified server settings, server needs to be restarted.");
            this.getServer().shutdown();
            return;
        }

        // Initialize game controller and configuration
        this.gameController = new GameController(this);
        this.configuration = new Configuration(this);

        // Check license
        // if (!TikAuth.checkLicense(this, this.configuration.getLicense())) {
        //     this.getServer().shutdown();
        //     return;
        // } else {
        //     this.getLogger().info(ChatColor.GREEN + "Successfully authorized license.");
        // }

        // Stop game if running
        this.gameController.stop();

        // Register commands
        this.getCommand("tiktokchat").setExecutor(new TikTokChatCommand(this));
        this.getCommand("tiktokjoin").setExecutor(new TikTokJoinCommand(this));
        this.getCommand("tiktokgift").setExecutor(new TikTokGiftCommand(this));
        this.getCommand("tiktoklikes").setExecutor(new TikTokLikesCommand(this));
        this.getCommand("score").setExecutor(new ScoreCommand(this));
        this.getCommand("addmaxhp").setExecutor(new AddMaxHpCommand(this));
        this.getCommand("addmob").setExecutor(new AddMobCommand(this));
        this.getCommand("heal").setExecutor(new HealCommand(this));
        this.getCommand("strength").setExecutor(new StrengthCommand(this));
        this.getCommand("reset").setExecutor(new ResetCommand(this));
        this.getCommand("update").setExecutor(new UpdateCommand(this));
        this.getCommand("stopcountdown").setExecutor(new StopCountdownCommand(this));
        this.getCommand("stopgame").setExecutor(new StopGameCommand(this));

        // Register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

        // Register tasks
        new GameTask(this).runTaskTimer(this, 2L, 2L);
        new ScoreBoardTask(this).runTaskTimer(this, 5L, 5L);
        new CountdownTask(this).runTaskTimer(this, 20L, 20L);

        this.getLogger().info(String.format("Plugin %s by %s has been loaded successfully.", this.getDescription().getFullName(), this.getDescription().getAuthors().get(0)));
    }

    /**
     * Called when the plugin is disabled. Performs cleanup or save operations if necessary.
     */
    @Override
    public void onDisable() {
        if (this.gameController != null) {
            this.gameController.stop();
        }
        if (this.configuration != null) {
            this.configuration.save();
        }
        this.getLogger().info(String.format("Plugin %s by %s has been disabled successfully.", this.getDescription().getFullName(), this.getDescription().getAuthors().get(0)));
    }
}
