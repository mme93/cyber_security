package org.example.ssh.session.channel.shell.tool;

import org.example.ssh.session.channel.shell.IShell;

public class ShellTool implements IShellTool{

    private IShell iShell;


    @Override
    public void setShell(IShell iShell) {
       this.iShell=iShell;
    }

}
