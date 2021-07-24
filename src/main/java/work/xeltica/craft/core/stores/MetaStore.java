package work.xeltica.craft.core.stores;

import java.io.IOException;

import work.xeltica.craft.core.XCorePlugin;
import work.xeltica.craft.core.utils.Config;

public class MetaStore {
    public MetaStore() {
        MetaStore.instance = this;
        meta = new Config("meta");
        checkUpdate();
    }

    public static MetaStore getInstance() {
        return instance;
    }

    public String getCurrentVersion() {
        return XCorePlugin.getInstance().getDescription().getVersion();
    }

    public String getPreviousVersion() {
        return previousVersion;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public String[] getChangeLog() {
        return changeLog;
    }

    private void checkUpdate() {
        var conf = meta.getConf();
        var currentVersion = conf.getString("version", null);
        previousVersion = conf.getString("previousVersion", null);
        if (currentVersion == null || !currentVersion.equals(getCurrentVersion())) {
            conf.set("version", getCurrentVersion());
            conf.set("previousVersion", currentVersion);
            previousVersion = currentVersion;
            isUpdated = true;
            try {
                meta.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private final Config meta;
    private String previousVersion;
    private boolean isUpdated;

    private String[] changeLog = {
        "ボート・トロッコの召喚可能なワールドを制限",
        "乗り物召喚ができないワールドの場合、スマホからも非表示に",
        "§mボート・トロッコ召喚を試験的に有償化(1つ5EP)§rやっぱりやめた",
        "プレイヤーへのテレポートを試験的に有償化(1回100EP)",
        "プレイヤーへのテレポートを5秒後に行うように",
        "プレイヤーへのテレポート時に、相手に予告が送られるように",
    };
    
    private static MetaStore instance;
}
