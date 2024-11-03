package org.example.security.system.tool.ssh.session.channel.shell.module;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.example.security.system.core.CSystem;
import org.example.security.system.tool.ssh.session.channel.shell.Shell;

import java.io.IOException;
import java.util.List;

public class UFW extends Shell {

    private SystemTool systemTool;
    private Session session;

    public UFW(Session session) throws JSchException {
        super(session);
        this.session = session;
    }

    private void loadSystemTool() {
        try {
            systemTool = new SystemTool(session);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAllPorts() throws IOException {
        String result = executeCommand(CSystem.Linux.Command.UFW_ALL_PORTS);
        System.err.println(result);
        return null;
    }


}
