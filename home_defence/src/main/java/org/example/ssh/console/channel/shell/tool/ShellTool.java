package org.example.ssh.console.channel.shell.tool;

import org.example.ssh.console.channel.shell.IShell;

public class ShellTool implements IShellTool{

    private IShell iShell;


    @Override
    public void setShell(IShell iShell) {
       this.iShell=iShell;
    }

}
