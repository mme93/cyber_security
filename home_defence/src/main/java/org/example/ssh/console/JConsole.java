package org.example.ssh.console;

import com.jcraft.jsch.JSchException;

public class JConsole extends AbstractJConsole implements IJConsole {


    public JConsole(String username, String host, String password, int port, boolean withLogging) {
        super(username, host, password, port, withLogging);
    }

    @Override
    public void loadSetup() throws JSchException {
        super.loadSetup();
    }

    @Override
    public void reloadChannel() throws JSchException {
        super.reloadChannel();
    }

    @Override
    public void reloadSession() throws JSchException {
        super.reloadSession();
    }

    @Override
    public void closeSessions() {
        super.closeSessions();
    }

    @Override
    public void closeChannel() {
        super.closeChannel();
    }

    public static JConsoleBuilder builder() {
        return new JConsoleBuilder();
    }

    public static class JConsoleBuilder {
        private int port;
        private String host;
        private String username;
        private String password;
        private boolean withLogging = false;

        private JConsoleBuilder() {
        }

        public JConsoleBuilder withLogging(boolean activate) {
            this.withLogging = activate;
            return this;
        }

        public JConsoleBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public JConsoleBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public JConsoleBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public JConsoleBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public JConsole build() {
            return new JConsole(username, host, password, port, withLogging);
        }
    }

}