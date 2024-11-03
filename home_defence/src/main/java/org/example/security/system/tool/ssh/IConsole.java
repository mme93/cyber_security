package org.example.security.system.tool.ssh;

import com.jcraft.jsch.JSchException;
import org.example.security.system.tool.ssh.model.SessionInfo;

public interface IConsole {

    void loadConsole();
    void loadSession(SessionInfo context) throws JSchException;

}
