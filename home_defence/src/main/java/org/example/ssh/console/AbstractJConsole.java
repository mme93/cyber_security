package org.example.ssh.console;

import com.jcraft.jsch.*;
import org.example.ssh.EDataTyp;
import org.example.ssh.logger.JConsoleLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.*;

import static com.jcraft.jsch.ChannelSftp.APPEND;
import static com.jcraft.jsch.ChannelSftp.OVERWRITE;
import static org.example.ssh.EDataTyp.TXT;

public abstract class AbstractJConsole extends JSch implements IJConsole {

    private ChannelSftp sftpChannel;
    private ChannelShell shellChannel;
    private String username;
    private String host;
    private String passwd;
    private int port;
    private Session session;

    public AbstractJConsole(String username, String host, String passwd, int port, boolean withLogging) {
        this.username = username;
        this.host = host;
        this.passwd = passwd;
        this.port = port;
        if (withLogging) {
            setLogger(new JConsoleLogger());
        }
    }


    public Session getCurrentSession() {
        return session;
    }

    public void loadSetup() throws JSchException {
        createSession();
        createChannels();
    }


    public final void createSession() throws JSchException {
        session = getSession(username, host, 22);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }


    public final void createChannels() throws JSchException {
        sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();

        shellChannel = (ChannelShell) session.openChannel("shell");
        shellChannel.connect();
    }


    public final String cd(String path) throws SftpException {
        sftpChannel.cd(String.format("%s/%s", pwd(), path));
        return sftpChannel.pwd();
    }


    public final String pwd() throws SftpException {
        return sftpChannel.pwd();
    }


    public final void mkdir(String path) throws SftpException {
        sftpChannel.mkdir(path);
    }


    public final void rmdir(String folder) throws SftpException {
        sftpChannel.rmdir(folder);
    }


    public final boolean existDir(String folder) throws JSchException, SftpException {
        return getFolders().stream().filter(folderName -> folderName.equals(folder)).toList().size() == 1;
    }

    public String executeCommand(int timeOutMillis, Scanner scanner) throws InterruptedException {
        StringBuilder sb = new StringBuilder();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            while (true) {
                Future<String> lineFuture = executor.submit(scanner::nextLine);
                try {
                    String line = lineFuture.get(timeOutMillis, TimeUnit.MILLISECONDS);
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

    public String executeCommand(String command, boolean timeoutActivated) throws IOException, InterruptedException {
        InputStream in = shellChannel.getInputStream();
        OutputStream out = shellChannel.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(command);
        writer.flush();
        Scanner scanner = new Scanner(in);
        if (timeoutActivated) {
            return executeCommand(1000, scanner);
        }

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(String.format("%s \n", scanner.nextLine()));
        }
        return sb.toString();
    }

    public String executeCommands(List<String> commands, boolean timeoutActivated) {
        StringBuilder sb = new StringBuilder();
        commands.forEach(command -> {
            try {
                sb.append(String.format("Next Command \n %s", executeCommand(command, timeoutActivated)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return sb.toString();
    }


    public void createFileWithContent(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName);
        inputStream.close();
    }


    public void rename(String oldFileName, String newFileName) throws SftpException {
        sftpChannel.rename(
                String.format("%s/%s", oldFileName),
                String.format("%s/%s", newFileName)
        );
    }


    public void overwriteFile(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName, OVERWRITE);
    }


    public void appendFile(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName, APPEND);
    }


    public void uploadFile(String localPath, String fileName) throws SftpException {
        sftpChannel.put(localPath, fileName);
    }


    public void uploadFile(String localPath, String fileName, String remotePath) throws SftpException {
        uploadFile(localPath, String.format("%s/%s", remotePath, fileName));
    }


    public String readFileContent(String fileName) throws SftpException, IOException {
        StringBuilder content = new StringBuilder();
        InputStream inputStream = sftpChannel.get(String.format("%s/%s", pwd(), fileName));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }


    public String sendCommand(String command) throws JSchException, IOException {
        return "";
    }


    public void resetSession() throws JSchException {
        createSession();
    }


    public void resetChannel() throws JSchException {
        createChannels();
    }


    public void reset() throws JSchException {
        resetSession();
        resetChannel();
    }


    public List<String> getFolders() throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> {
            if (!file.getFilename().contains(".")) {
                folders.add(file.getFilename());
            }
        });
        return folders;
    }


    public List<String> getFiles() throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        return folders.stream().filter(folder -> !(folder.equals(".") || folder.equals(".."))).toList();
    }


    public List<String> getFilesByDataTyp(EDataTyp dataTyp) throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        String typ;
        if (dataTyp.equals(TXT)) {
            typ = ".txt";
        } else {
            throw new RuntimeException(String.format("No Data Typ find by parameter: " + dataTyp));
        }
        return folders.stream().filter(folder -> folder.contains(typ)).toList();
    }

    @Override
    public void reloadChannel() throws JSchException {
        sftpChannel.disconnect();
        createChannels();
    }

    @Override
    public void reloadSession() throws JSchException {
        session.disconnect();
        createSession();
    }

    @Override
    public void closeSessions() {
        session.disconnect();
    }

    @Override
    public void closeChannel() {
        sftpChannel.disconnect();
    }
}
