package org.example.ssh.session.channel.shell.command;

public class ShellCommand implements IShellCommand{

    private String command;

    @Override
    public String getCommand() {
        return command;
    }
}
