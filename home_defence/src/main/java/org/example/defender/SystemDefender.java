package org.example.defender;

import org.example.defender.model.SystemSettings;
import org.example.defender.model.Port;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SystemDefender extends Defender {

    private String host;
    private SystemSettings systemSettings;
    private final int maxPort = 65355;
    private final int minPort = 1;


    public SystemDefender(String host) {
        super();
        this.host = host;
    }

    public List<Port> portScanning() {
        return portScanning(minPort, maxPort);
    }

    public List<String> hostScanning() throws IOException {
        return executeProcess(createProcess("nmap -sP 192.168.1.0/24"));
    }

    public void printResult(List<String> list) {
        for (String line : list) {
            System.out.println(line);
        }
    }

    public List<Port> portScanning(int startPort, int endPort) {
        List<Port> ports = new ArrayList<>();
        for (int port = startPort; port <= endPort; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 100);
                System.out.println("Port " + port + " is open");
                ports.add(new Port(host, port));
            } catch (Exception e) {

            }
        }
        return ports;
    }

    public void loadSystemSettings() throws IOException {
        if (isLinux()) {

        } else if (isWindows()) {
            String ipv4 = "";
            String ipv6 = "";
            for (String result : executeProcess(createProcess("ipconfig"))) {
                if (result.contains("IPv4-Adresse  . . . . . . . . . . :")) {
                    ipv4 = result.split(":")[1];
                } else if (result.contains("IPv6-Adresse. . . . . . . . . . . : ")) {
                    StringBuilder sb = new StringBuilder();
                    String resultArray[] = result.split(":");
                    for (int i = 0; i < resultArray.length - 1; i++) {
                        sb.append(resultArray[i + 1]);
                        if (i != resultArray.length - 2) {
                            sb.append(":");
                        }
                    }
                    ipv6 = sb.toString();
                }
            }
            this.systemSettings = new SystemSettings(ipv4, ipv6);
        }
    }

    public SystemSettings getSystemSettings() {
        return systemSettings;
    }

}
