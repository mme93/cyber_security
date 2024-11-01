package org.example;

import org.example.ssh.console.tool.ShellToolJConsole;


public class Main {

    public static void main(String[] args) throws Exception {

        ShellToolJConsole console = ShellToolJConsole.builder()
                .withUsername("root")
                .withHost("217.160.26.246")
                .withPassword("!Mameie93")
                .withPort(22)
                //.withLogger()
                .build();
        console.loadSetup();
        System.err.println(console.executeCommand("git -v",true));
        //console.install();
    }
}