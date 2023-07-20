package me.mstn.plugin.utilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CoolDown {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final Map<String, Long> PLAYERS = new ConcurrentHashMap<>();

    static {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, Long> entry : PLAYERS.entrySet()) {
                if (entry.getValue() < System.currentTimeMillis()) {
                    PLAYERS.remove(entry.getKey());
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static boolean hasCoolDown(String playerName) {
        if (playerName == null) {
            return false;
        }

        return PLAYERS.containsKey(playerName.toLowerCase());
    }

    public static void addCoolDown(String playerName, long ticks) {
        String name = playerName.toLowerCase();
        long time = System.currentTimeMillis() + ticks * 50;

        if (PLAYERS.containsKey(name)) {
            return;
        }

        PLAYERS.put(name, time);
    }

}