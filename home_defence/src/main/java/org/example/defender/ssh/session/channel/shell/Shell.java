package org.example.defender.ssh.session.channel.shell;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public abstract class Shell implements IShell{

    private Session session;
    private ChannelShell shell;

    public Shell(Session session) throws JSchException {
        this.session = session;
    }


    public void connect() throws JSchException {
        shell = (ChannelShell) session.openChannel("shell");
    }

    public void disconnect(){
        shell.disconnect();
    }

    public void reconnect() throws JSchException {
        disconnect();
        connect();
    }

    @Override
    public void overwriteSession(Session session) {
        this.session=session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String executeCommand(String command) throws IOException {
        InputStream in = shell.getInputStream();
        OutputStream out = shell.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(command);
        writer.flush();
        Scanner scanner = new Scanner(in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(String.format("%s \n", scanner.nextLine()));
        }
        return sb.toString();
    }

    @Override
    public String executeCommand(String command, int timeoutMillis) throws IOException {
        InputStream in = shell.getInputStream();
        OutputStream out = shell.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(command);
        writer.flush();
        Scanner scanner = new Scanner(in);

        StringBuilder sb = new StringBuilder();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            while (true) {
                Future<String> lineFuture = executor.submit(scanner::nextLine);
                try {
                    String line = lineFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
                    sb.append(line).append("\n");
                } catch (TimeoutException e) {
                    System.err.println("Timeout: Keine weitere Eingabe innerhalb der Zeitgrenze.");
                    break;
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } finally {
            executor.shutdown();
        }

        return sb.toString();
    }

    @Override
    public String executeCommands(List<String> commands) {
        StringBuilder sb = new StringBuilder();
        commands.forEach(command -> {
            try {
                sb.append(String.format("Next Command \n %s", executeCommand(command)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return sb.toString();
    }

    @Override
    public String executeCommands(List<String> commands, int timeoutMillis) {
        StringBuilder sb = new StringBuilder();
        commands.forEach(command -> {
            try {
                sb.append(String.format("Next Command \n %s", executeCommand(command,timeoutMillis)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return sb.toString();
    }
}
