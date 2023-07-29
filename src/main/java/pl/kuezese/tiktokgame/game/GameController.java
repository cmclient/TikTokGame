package pl.kuezese.tiktokgame.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import pl.kuezese.tiktokgame.TikTokGame;
import pl.kuezese.tiktokgame.helper.ChatHelper;
import pl.kuezese.tiktokgame.helper.ItemMaker;
import pl.kuezese.tiktokgame.object.Gift;
import pl.kuezese.tiktokgame.object.MobObject;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The GameController class manages the game state and player interactions in the TikTokGame plugin.
 */
@Getter
@Setter
public class GameController {

    private boolean gameStarted;
    private int countdown;
    private int countdownThrottle;
    private int playerScore;
    private int viewerScore;
    private String lastGifter = "(?)";
    private String lastFollower = "(?)";
    private int likes;
    private int viewers;

    private final TikTokGame plugin;
    private final Queue<MobObject> mobsQueue;
    private final Map<String, Gift> gifts;

    /**
     * Constructs a new GameController instance for managing the TikTokGame plugin.
     *
     * @param plugin The main TikTokGame plugin instance.
     */
    public GameController(TikTokGame plugin) {
        this.plugin = plugin;
        this.gameStarted = false;
        this.countdown = -1;
        this.mobsQueue = new ConcurrentLinkedQueue<>();
        this.gifts = new ConcurrentHashMap<>();
    }

    /**
     * Resets the game state and players for a new game round.
     */
    public void reset() {
        // Start game
        this.gameStarted = true;
        this.countdown = -1;

        // Join all players
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        players.forEach(this::join);

        // Remove all monsters from the world
        this.plugin.getServer().getWorlds().get(0).getEntities().stream().filter(entity -> entity instanceof Monster).forEach(Entity::remove);

        // Clear mob queue
        this.mobsQueue.clear();

        // Add start mobs
        int amount = this.plugin.getConfiguration().getMobStartAmount();
        for (int i = 0; i < amount; i++) {
            this.mobsQueue.add(new MobObject(EntityType.ZOMBIE, null));
        }

        // Broadcast message to all players about the game reset
        this.plugin.getServer().broadcastMessage(ChatHelper.format(this.plugin.getConfiguration().getGameReset().replace("{PREFIX}", this.plugin.getConfiguration().getGamePrefix())));
    }

    /**
     * Stops the game state and prepare players for a new game round.
     */
    public void stop() {
        // Stop game
        this.gameStarted = false;
        this.countdown = -1;

        // Join all players
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        players.forEach(this::join);

        // Remove all monsters from the world
        this.plugin.getServer().getWorlds().get(0).getEntities().stream().filter(entity -> entity instanceof Monster).forEach(Entity::remove);

        // Clear mobs queue
        this.mobsQueue.clear();

        // Broadcast message to all players about the game stop
        this.plugin.getServer().broadcastMessage(ChatHelper.format(this.plugin.getConfiguration().getGameStop().replace("{PREFIX}", this.plugin.getConfiguration().getGamePrefix())));
    }

    /**
     * Handles player join event by resetting the player attributes and inventory.
     *
     * @param player The player who joined the game.
     */
    public void join(Player player) {
        AttributeInstance genericMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        genericMaxHealth.setBaseValue(genericMaxHealth.getDefaultValue());
        player.setHealth(genericMaxHealth.getBaseValue());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.teleport(player.getWorld().getSpawnLocation());
        this.setInventory(player);
    }

    /**
     * Sets the initial inventory for a player joining the game.
     *
     * @param player The player to set the inventory for.
     */
    private void setInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHelmet(new ItemMaker(Material.DIAMOND_HELMET)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3)
                .make());
        player.getInventory().setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3)
                .make());
        player.getInventory().setLeggings(new ItemMaker(Material.DIAMOND_LEGGINGS)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3)
                .make());
        player.getInventory().setBoots(new ItemMaker(Material.DIAMOND_BOOTS)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3)
                .make());
        player.getInventory().addItem(
                new ItemMaker(Material.DIAMOND_SWORD)
                        .addEnchant(Enchantment.DAMAGE_ALL, 5).make(),
                new ItemMaker(Material.COOKED_BEEF, 32).make(),
                new ItemMaker(Material.GOLDEN_APPLE, 1).make()
        );
    }
}
