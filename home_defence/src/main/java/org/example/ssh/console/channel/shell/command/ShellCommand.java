package org.example.ssh.console.channel.shell.command;

public class ShellCommand implements IShellCommand{

    private String command;

    @Override
    public String getCommand() {
        return command;
    }
}
