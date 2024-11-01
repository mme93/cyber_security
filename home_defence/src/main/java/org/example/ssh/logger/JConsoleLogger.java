package org.example.ssh.logger;

public class JConsoleLogger  implements com.jcraft.jsch.Logger {
    static java.util.Hashtable<Integer, String> name = new java.util.Hashtable<>();

    static {
        name.put(DEBUG, "DEBUG: ");
        name.put(INFO, "INFO: ");
        name.put(WARN, "WARN: ");
        name.put(ERROR, "ERROR: ");
        name.put(FATAL, "FATAL: ");
    }

    @Override
    public boolean isEnabled(int level) {
        return true;
    }

    @Override
    public void log(int level, String message) {
        System.out.println(name.get(level) + message);
    }
}