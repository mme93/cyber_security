package org.example;

import org.example.ssh.console.JConsole;


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
        System.err.println(console.readFileContent("test.txt"));
       // console.createFileWithContent(console.getCurrentPath()+"/hello.txt","Hello World");
        //console.createFileWithContent("asd","a.txt");
        //System.err.println("<-------------Load----------------->");
       // console.getFolderFromPath().forEach(x-> System.err.println(x));
    }
}