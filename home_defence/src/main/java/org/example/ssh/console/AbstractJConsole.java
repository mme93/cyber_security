package org.example.ssh.console;

import com.jcraft.jsch.*;
import org.example.ssh.logger.JConsoleLogger;


public abstract class AbstractJConsole extends JSch implements IJConsole {

    private String username;
    private String host;
    private String passwd;
    private int port;
    private Session session;

    public AbstractJConsole(String username, String host, String passwd, int port, boolean withLogging) {
        this.username = username;
        this.host = host;
        this.passwd = passwd;
        this.port = port;
        if (withLogging) {
            setLogger(new JConsoleLogger());
        }
    }

    public Session getCurrentSession() {
        return session;
    }

    public final void createSession() throws JSchException {
        session = getSession(username, host, 22);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    @Override
    public void reloadSession() throws JSchException {
        session.disconnect();
        createSession();
    }

    @Override
    public void closeSessions() {
        session.disconnect();
    }

}
