package org.example.ssh.console;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;


public interface IJConsole {

    void loadSetup() throws JSchException;
    Channel reloadChannel(String channelName) throws JSchException;
    void reloadSession() throws JSchException;
    void closeSessions();
    void closeChannel();
}
