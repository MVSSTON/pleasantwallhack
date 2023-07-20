package me.mstn.plugin.utilities;

import org.bukkit.Bukkit;

public enum BukkitVersion {

    UNDEFINED(0),
    v1_8_R1(1),
    v1_8_R2(2),
    v1_8_R3(3),
    v1_9_R1(4),
    v1_9_R2(5),
    v1_10_R1(6),
    v1_11_R1(7),
    v1_12_R1(8),
    v1_13_R1(9),
    v1_13_R2(10),
    v1_14_R1(11),
    v1_15_R1(12),
    v1_16_R1(13),
    v1_16_R2(14),
    v1_16_R3(15),
    v1_17_R1(16),
    v1_18_R1(17),
    v1_18_R2(18),
    v1_19_R1(19),
    v1_19_R2(20);

    private static final BukkitVersion VERSION;
    private final int id;

    static {
        BukkitVersion currentVersion = null;

        Package bukkitPackage = Bukkit.getServer().getClass().getPackage();

        if (bukkitPackage != null) {
            String versionName = bukkitPackage.getName().split("\\.")[3];

            try {
                currentVersion = valueOf(versionName);
            } catch (IllegalArgumentException e) {
                currentVersion = UNDEFINED;
                Bukkit.getServer().getLogger().severe("Version could not be found, it may be out of date or not supported! (Version name: " + versionName + ")");
            }
        }

        VERSION = currentVersion;
    }

    BukkitVersion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static BukkitVersion getCurrent() {
        return VERSION;
    }


}