package org.example;


import org.example.security.Security;
import static org.example.security.system.core.ESystem.FIREWALL;

public class Main {

    public static void main(String[] args) throws Exception {
        Security security = new Security();
        security.loadSystem(FIREWALL);
    }
}