package org.example.security.system.firewall;

import com.jcraft.jsch.JSchException;
import org.example.security.system.core.ESystem;
import org.example.security.system.core.ISystem;
import org.example.security.system.System;
import org.example.security.system.tool.ssh.model.SessionInfo;
import org.example.security.system.tool.ssh.session.JSession;
import org.example.security.system.tool.ssh.session.channel.shell.module.UFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.security.system.core.CSystem.Linux.Command.UFW_ALL_PORTS;

public class Firewall extends System {

    private JSession session;
    private UFW ufw;


    @Override
    public ESystem getType() {
        return ESystem.FIREWALL;
    }

    @Override
    public ISystem get() {
        return this;
    }

    public void loadConfigurations(SessionInfo sessionInfo){
      try {
          session =JSession.builder()
                  .withHost(sessionInfo.getHost())
                  .withLogger(sessionInfo.isSessionActivated())
                  .withPassword(sessionInfo.getPasswd())
                  .build();
          ufw = new UFW(session.getSession());
      } catch (RuntimeException | JSchException e) {
          throw new RuntimeException("Can´t create UFW, because of Sessions error.",e);
      }

    }

    public List<Integer> ports() throws IOException {
        return ufw.getAllPorts();
    }

    public String getStatusReport() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Ports on System: \n");
        ports().forEach(port -> sb.append(String.format("%s \n")));
        return sb.toString();
    }

}
