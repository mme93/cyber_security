package org.example.defender.ssh.session;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.example.defender.ssh.logger.JConsoleLogger;

public class JSession extends JSch {

    private Session session;
    private String username;
    private String host;
    private String passwd;
    private int port;

    public JSession(String username, String host, String passwd, int port, boolean withLogging) throws JSchException {
        this.username = username;
        this.host = host;
        this.passwd = passwd;
        this.port = port;
        if (withLogging) {
            setLogger(new JConsoleLogger());
        }
        reloadSession();
    }

    public void reloadSession() throws JSchException {
        session = getSession(username, host, port);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public Session getSession() {
        return session;
    }

    public void closeSession() {
        session.disconnect();
    }

    public JConsoleBuilder builder() {
        return new JConsoleBuilder();
    }

    public static class JConsoleBuilder {
        private String username;
        private String host;
        private String passwd;
        private int port = 22;
        private boolean activated = false;

        private JConsoleBuilder() {
        }

        public JConsoleBuilder withUserName(String username) {
            this.username = username;
            return this;
        }

        public JConsoleBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public JConsoleBuilder withPassword(String passwd) {
            this.passwd = passwd;
            return this;
        }

        public JConsoleBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public JConsoleBuilder withLogger(boolean activated) {
            this.activated = activated;
            return this;
        }

        public JSession build() throws JSchException {
            return new JSession(username, host, passwd, port, activated);
        }
    }

}
