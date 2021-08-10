package work.xeltica.craft.core.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.Setter;

/**
 * カウンターの位置情報を登録するシステムです。
 */
public class CounterData implements Cloneable, ConfigurationSerializable {

    public CounterData(
        String name, 
        Location location1, 
        Location location2, 
        boolean isDaily, 
        @Nullable String javaRankingId,
        @Nullable String bedrockRankingId,
        @Nullable String uwpRankingId,
        @Nullable String phoneRankingId
    ) {
        this.name = name;
        this.location1 = location1;
        this.location2 = location2;
        this.isDaily = isDaily;
        this.javaRankingId = javaRankingId;
        this.bedrockRankingId = bedrockRankingId;
        this.uwpRankingId = uwpRankingId;
        this.phoneRankingId = phoneRankingId;
    }

    public @NotNull Map<String, Object> serialize() {
        final var serialized = new HashMap<String, Object>();
        serialized.put("name", name);
        serialized.put("location1", location1.serialize());
        serialized.put("location2", location2.serialize());
        serialized.put("isDaily", isDaily);
        serialized.put("javaRankingId", javaRankingId);
        serialized.put("bedrockRankingId", bedrockRankingId);
        serialized.put("uwpRankingId", uwpRankingId);
        serialized.put("phoneRankingId", phoneRankingId);
        return serialized;
    }

    public static CounterData deserialize(Map<String, Object> args) {
        final String name;
        final Location location1;
        final Location location2;
        final boolean isDaily;
        final String javaRankingId;
        final String bedrockRankingId;
        final String uwpRankingId;
        final String phoneRankingId;

        assertKey(args, "name");
        assertKey(args, "location1");
        assertKey(args, "location2");
        assertKey(args, "isDaily");

        name = (String)args.get("name");
        location1 = Location.deserialize((Map<String, Object>) args.get("location1"));
        location2 = Location.deserialize((Map<String, Object>) args.get("location2"));
        isDaily = (Boolean)args.get("isDaily");

        javaRankingId = (String)args.get("javaRankingId");
        bedrockRankingId = (String)args.get("bedrockRankingId");
        uwpRankingId = (String)args.get("uwpRankingId");
        phoneRankingId = (String)args.get("phoneRankingId");

        return new CounterData(name, location1, location2, isDaily, javaRankingId, bedrockRankingId, uwpRankingId, phoneRankingId);
    }

    protected static void assertKey(Map<String, Object> args, @NotNull String key) {
        if (!args.containsKey(key)) throw new IllegalArgumentException(key + " is null");
    }

    @Getter private final String name;
    @Getter private final Location location1;
    @Getter private final Location location2;
    @Getter private final boolean isDaily;

    /** Java版プレイヤー用 紐付けたランキングID */
    @Nullable @Getter @Setter private String javaRankingId;
    /** 統合版プレイヤー用 紐付けたランキングID */
    @Nullable @Getter @Setter private String bedrockRankingId;
    /** Windows10版プレイヤー用 紐付けたランキングID */
    @Nullable @Getter @Setter private String uwpRankingId;
    /** スマホ・タブレット・ゲーム機版プレイヤー用 紐付けたランキングID */
    @Nullable @Getter @Setter private String phoneRankingId;
}