package pl.kuezese.tiktokgame.object;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.Nonnull;

/**
 * Represents a mob object in the TikTokGame plugin, with a specific EntityType and an optional name.
 */
public @Getter class MobObject {

    private final @Nonnull EntityType type;
    private final @Nullable String name;

    /**
     * Constructs a new MobObject with the specified EntityType and optional name.
     *
     * @param type The EntityType of the mob object.
     * @param name The optional name of the mob object.
     */
    public MobObject(EntityType type, String name) {
        this.type = type;
        this.name = name;
    }
}
