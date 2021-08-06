package me.mstn.api.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final Map<String, Map<String, Long>> PLAYERS = new ConcurrentHashMap<>();

    static {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> PLAYERS.forEach((name, data) -> {
            data.forEach((type, time) -> {
                if (time < System.currentTimeMillis()) {
                    data.remove(type);
                }
            });

            if (data.isEmpty()) {
                PLAYERS.remove(name);
            }
        }), 0, 50, TimeUnit.MILLISECONDS);
    }

    public static boolean hasCooldown(String playerName, String type) {
        if (playerName == null || type == null) {
            return false;
        }

        String name = playerName.toLowerCase();
        return PLAYERS.containsKey(name) && PLAYERS.get(name).containsKey(type.toLowerCase());
    }

    public static int getSecondCooldown(String playerName, String type) {
        if (!hasCooldown(playerName, type)) {
            return 0;
        }
        String name = playerName.toLowerCase();
        Map<String, Long> cooldownData = PLAYERS.get(name);
        if (cooldownData == null) {
            return 0;
        }

        Long startTime = cooldownData.get(type.toLowerCase());

        if (startTime == null) {
            return 0;
        }

        int time = (int) ((startTime - System.currentTimeMillis()) / 50 / 20);
        return (time == 0 ? 1 : time);
    }

    public static void addCooldown(String playerName, String type, long ticks) {
        String name = playerName.toLowerCase();
        long time = System.currentTimeMillis() + ticks * 50;

        Map<String, Long> cooldownData = PLAYERS.get(name);
        if (cooldownData == null) {
            cooldownData = new ConcurrentHashMap<>();
            cooldownData.put(type.toLowerCase(), time);
            PLAYERS.put(name, cooldownData);
            return;
        }

        if (cooldownData.containsKey(type.toLowerCase()))
            return;

        cooldownData.put(type.toLowerCase(), time);
    }

}