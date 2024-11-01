package org.example.ssh.console.channel.shell;

import com.jcraft.jsch.Session;

import java.io.IOException;
import java.util.List;

public interface IShell {

    void overwriteSession(Session session);
    Session getSession();
    String executeCommand(String command) throws IOException;
    String executeCommand(String command, int timeoutMillis) throws IOException;
    String executeCommands(List<String> commands);
    String executeCommands(List<String> commands, int timeoutMillis);
}
