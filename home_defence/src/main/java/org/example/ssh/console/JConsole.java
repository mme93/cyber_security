package org.example.ssh.console;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

public class JConsole extends AbstractJConsole {

    public JConsole(String username, String host, String passwd, int port, boolean withLogging) {
        super(username, host, passwd, port, withLogging);
    }

    @Override
    public void loadSetup() throws JSchException {
            createSession();
    }

    @Override
    public Channel reloadChannel(String channelName) throws JSchException {
        if(channelName.equals("shell")){
            return getCurrentSession().openChannel("shell");
        }else if(channelName.equals("sftp")){
            return getCurrentSession().openChannel("sftp");
        }
        throw new RuntimeException(String.format("No Channel with name %s found.",channelName));
    }

    @Override
    public void closeChannel() {

    }

    @Override
    public void reloadSession() throws JSchException {
        super.reloadSession();
    }

    @Override
    public void closeSessions() {
        super.closeSessions();
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

        public JConsole build() {
            return new JConsole(username, host, passwd, port, activated);
        }
    }

}
