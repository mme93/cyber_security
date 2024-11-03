package org.example.defender.ssh.session.channel.shell.command;

public class ShellCommand implements IShellCommand{

    private String command;

    @Override
    public String getCommand() {
        return command;
    }
}
