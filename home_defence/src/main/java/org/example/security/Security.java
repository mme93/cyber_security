package org.example.security;

import org.example.security.system.core.ESystem;
import org.example.security.system.core.ISystem;
import org.example.security.system.firewall.Firewall;

import java.util.List;

public class Security {

    List<ISystem>systems;

    public void loadSystems(List<ESystem> systems){
        systems.forEach(system -> loadSystem(system));
    }

    public void loadSystem(ESystem system){
        switch (system){
            case SYSTEM ->{
                Firewall firewall= new Firewall();
                systems.add(firewall);
            }
        }
    }

}
