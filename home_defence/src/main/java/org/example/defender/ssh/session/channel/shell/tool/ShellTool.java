package org.example.defender.ssh.session.channel.shell.tool;

import org.example.defender.ssh.session.channel.shell.IShell;

public class ShellTool implements IShellTool{

    private IShell iShell;


    @Override
    public void setShell(IShell iShell) {
       this.iShell=iShell;
    }

}
