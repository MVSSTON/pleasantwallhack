package me.mstn.plugin.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
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

public class GlowHandler {

    private static final HashMap<Player, WrappedDataWatcher> watcher = new HashMap<>();

    public static boolean glowPlayers(Player receiver, int radius, int seconds) {
        Collection<Entity> entitiesNearby = receiver.getNearbyEntities(radius, radius, radius);
        Collection<Entity> playersNearby = new ArrayList<>();

        for (Entity entity : entitiesNearby) {
            if (!(entity instanceof Player)) {
                continue;
            }

            if (entity.hasMetadata("NPC")) {
                continue;
            }

            playersNearby.add(entity);
        }

        if (playersNearby.isEmpty()) {
            return false;
        }

        for (Entity entity : playersNearby) {
            glowEntity(receiver, entity, true);
        }

        Bukkit.getServer().getScheduler().runTaskLater(PleasantWallHack.getInstance(), () -> {
            for (Entity entity : playersNearby) {
                glowEntity(receiver, entity, false);
            }
        }, 20L * seconds);

        return true;
    }

    public static void glowEntity(Player receiver, Entity entity, boolean glow) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        watcher.put(receiver, new WrappedDataWatcher());

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.get(receiver).setEntity(entity);

        WrappedDataWatcher receiverDataWatcher = watcher.get(receiver);
        Object watcherObject = receiverDataWatcher.getObject(0);

        if (glow) {
            if (watcherObject == null) {
                receiverDataWatcher.setObject(0, serializer, (byte) 0x40);
            } else {
                receiverDataWatcher.setObject(0, serializer, (byte) watcherObject | 1 << 6);
            }
        } else {
            if (watcherObject == null) {
                receiverDataWatcher.setObject(0, serializer, (byte) 0x00);
            } else {
                receiverDataWatcher.setObject(0, serializer, (byte) watcherObject & ~1 << 6);
            }
        }

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, entity.getEntityId());
        packet.getWatchableCollectionModifier().write(0, receiverDataWatcher.getWatchableObjects());

        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
