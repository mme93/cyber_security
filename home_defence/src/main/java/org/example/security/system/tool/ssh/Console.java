package org.example.security.system.tool.ssh;

import com.jcraft.jsch.JSchException;
import org.example.security.exception.SessionError;
import org.example.security.system.tool.ssh.model.SessionInfo;
import org.example.security.system.tool.ssh.session.JSession;

public class Console implements IConsole {

    private JSession session;


    @Override
    public void loadConsole() {

    }

    @Override
    public void loadSession(SessionInfo context)  {
        try {
            session = JSession.builder()
                    .withHost(context.getHost())
                    .withUserName(context.getUsername())
                    .withPassword(context.getPasswd())
                    .withLogger(context.isSessionActivated())
                    .build();
        }catch (JSchException ex){
            throw new SessionError("Problem to create a Session",ex);
        }
    }
}
