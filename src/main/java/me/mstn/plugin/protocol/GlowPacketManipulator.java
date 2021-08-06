package me.mstn.plugin.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.mstn.plugin.PleasantWallHack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class GlowPacketManipulator {

    private static final HashMap<Player, WrappedDataWatcher> watcher = new HashMap<>();

    /**
     * @param receiver Исполнитель, кто будет видеть подсветку
     * @param radius В радиусе скольких блоков будут подсвечены игроки
     * @param seconds Время в секундах
     * @param consumer Consumer, если рядом были игроки и они засветились то вернёт true
     */
    public static void glowPlayersAroundPlayer(Player receiver, int radius, int seconds, Consumer<Boolean> consumer) {
        Collection<Entity> entitiesNearby = receiver.getNearbyEntities(radius, radius, radius);
        Collection<Entity> playersNearby = new ArrayList<>();

        entitiesNearby.forEach(entity -> {
            if (entity instanceof Player) {
                if (!entity.hasMetadata("NPC")) {
                    playersNearby.add(entity);
                }
            }
        });

        if (playersNearby.size() > 0) {
            playersNearby.forEach(playerEntity -> {
                 glowEntity(receiver, playerEntity, true);
            });

            Bukkit.getServer().getScheduler().runTaskLater(PleasantWallHack.getInstance(), () -> {
                playersNearby.forEach(playerEntity -> {
                    glowEntity(receiver, playerEntity, false);
                });
            }, 20L * seconds);

            consumer.accept(true);
        } else consumer.accept(false);
    }

    public static void glowEntity(Player receiver, Entity toGlow, boolean glow) {
        ProtocolManager protocolManager = PleasantWallHack.getMasstonAPI().getProtocolManager();
        watcher.put(receiver, new WrappedDataWatcher());

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.get(receiver).setEntity(toGlow);

        WrappedDataWatcher receiverDataWatcher = watcher.get(receiver);

        if (glow) {
            if (receiverDataWatcher.getObject(0) == null) receiverDataWatcher.setObject(0, serializer, (byte) 0x40);
            else receiverDataWatcher.setObject(0, serializer, (byte) receiverDataWatcher.getObject(0) | 1 << 6);
        } else {
            if (receiverDataWatcher.getObject(0) == null) receiverDataWatcher.setObject(0, serializer, (byte) 0x00);
            else receiverDataWatcher.setObject(0, serializer, (byte) receiverDataWatcher.getObject(0) & ~1 << 6);
        }

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, toGlow.getEntityId());
        packet.getWatchableCollectionModifier().write(0, receiverDataWatcher.getWatchableObjects());

        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
