package org.example.security.system.tool.ssh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionInfo {

    private String username;
    private String host;
    private String passwd;
    private int port;
    private boolean sessionActivated;

}
