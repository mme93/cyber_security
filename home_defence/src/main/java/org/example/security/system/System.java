package org.example.security.system;

import org.example.security.system.core.ESystem;
import org.example.security.system.core.ISystem;

public class System implements ISystem {

    @Override
    public ESystem getType() {
        return ESystem.SYSTEM;
    }

    @Override
    public ISystem get() {
        return this;
    }
}
