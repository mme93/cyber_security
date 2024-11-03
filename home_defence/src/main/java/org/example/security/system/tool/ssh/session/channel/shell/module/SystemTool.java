package org.example.security.system.tool.ssh.session.channel.shell.module;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.example.security.system.tool.ssh.session.channel.shell.Shell;

public class SystemTool extends Shell {

    public SystemTool(Session session) throws JSchException {
        super(session);
    }

    public void install() {

    }

    public void remove() {

    }

    public String version() {

        return null;
    }
}
