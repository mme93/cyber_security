package org.example;

import com.jcraft.jsch.*;
import org.example.ssh.JConsole;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        JConsole console = JConsole.builder()
                .withUserName("root")
                .withHost("217.160.26.246")
                .withPassword("!Mameie93")
                .withPort(22)
                //.withLogger()
                .build();
        console.loadSetup();

        if(!console.existDir("test")){
            console.mkdir("test");
        }
        console.cd("test");
       // console.createFileWithContent(console.getCurrentPath()+"/hello.txt","Hello World");
        console.createFileWithContent("asd","a.txt");
        System.err.println("<-------------Load----------------->");
        console.getFolderFromPath().forEach(x-> System.err.println(x));
    }
}