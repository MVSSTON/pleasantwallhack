package me.mstn.api;

import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import lombok.Setter;
import me.mstn.api.command.ICommandManager;
import me.mstn.api.service.IServiceManager;
import me.mstn.api.service.impl.ServiceManager;

public class MasstonAPI {

    @Getter private final IServiceManager serviceManager = new ServiceManager();

    @Setter private ICommandManager commandManager;
    @Setter @Getter ProtocolManager protocolManager;

}
