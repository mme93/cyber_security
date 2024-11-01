package org.example.ssh.console;

import com.jcraft.jsch.JSchException;


public interface IJConsole {

    void loadSetup() throws JSchException;
    void reloadChannel() throws JSchException;
    void reloadSession() throws JSchException;
    void closeSessions();
    void closeChannel();
}
