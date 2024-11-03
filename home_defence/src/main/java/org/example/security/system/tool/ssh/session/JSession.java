package org.example.security.system.tool.ssh.session;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.example.security.system.tool.ssh.session.logger.JConsoleLogger;

public class JSession extends JSch {

    private Session session;
    private String username;
    private String host;
    private String passwd;
    private int port;

    private JSession(String username, String host, String passwd, int port, boolean withLogging) throws JSchException {
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

    public static JSessionBuilder builder() {
        return new JSessionBuilder();
    }

    public static class JSessionBuilder {
        private String username;
        private String host;
        private String passwd;
        private int port = 22;
        private boolean activated = false;

        private JSessionBuilder() {
        }

        public JSessionBuilder withUserName(String username) {
            this.username = username;
            return this;
        }

        public JSessionBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public JSessionBuilder withPassword(String passwd) {
            this.passwd = passwd;
            return this;
        }

        public JSessionBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public JSessionBuilder withLogger(boolean activated) {
            this.activated = activated;
            return this;
        }

        public JSession build() throws JSchException {
            return new JSession(username, host, passwd, port, activated);
        }
    }

}
