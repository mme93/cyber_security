package org.example.ssh;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Getter
@Setter
public class JConsole extends JSch {

    private String username;
    private String host;
    private String passwd;
    private int port;
    private boolean withLogging;
    private Session session;
    private ChannelExec execChannel;
    private ChannelSftp sftpChannel;
    private String currentPath;

    private JConsole(String username, String host, String passwd, int port, boolean withLogging) {
        this.username = username;
        this.host = host;
        this.passwd = passwd;
        this.port = port;
        if (withLogging) {
            JSch.setLogger(new JConsoleLogger());
        }
        createSession();
    }

    public void loadSetup() throws JSchException {
        createSession();
        createChannels();
    }

    private void createSession() {
        try {
            session = getSession(username, host, 22);
            session.setPassword(passwd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createChannels() throws JSchException {
        sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();

        execChannel = (ChannelExec) session.openChannel("exec");
        execChannel.connect();
    }


    public String cd(String path) throws SftpException {
        sftpChannel.cd(String.format("%s/%s", pwd(), path));
        return sftpChannel.pwd();
    }

    public String pwd() throws SftpException {
        return sftpChannel.pwd();
    }

    public void mkdir(String path) throws SftpException {
        sftpChannel.mkdir(path);
    }

    public void rmdir(String folder) throws SftpException {
        sftpChannel.rmdir(folder);
    }

    public boolean existDir(String folder) throws JSchException, SftpException {
        return getFolderFromPath().stream().filter(folderName -> folderName.equals(folder)).toList().size() == 1;
    }


    public void createFileWithContent(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName);
        inputStream.close();
    }

    public void uploadFile(String localPath, String fileName) throws SftpException {
        sftpChannel.put(localPath, fileName);
    }

    public void uploadFile(String localPath, String fileName, String remotePath) throws SftpException {
        uploadFile(localPath, String.format("%s/%s", remotePath, fileName));
    }


    public String sendCommand(String command) throws JSchException, IOException {
        execChannel.setCommand(command);
        InputStream input = execChannel.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder outputBuffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            outputBuffer.append(line).append("\n");
        }
        return outputBuffer.toString();
    }


    public String test(String command) throws IOException {

        execChannel.setCommand(command);
        InputStream in = execChannel.getInputStream();
        BufferedReader scriptReader = new BufferedReader(
                new InputStreamReader(in));
        String result = scriptReader.readLine();
        InputStream err = execChannel.getErrStream();
        BufferedReader errReader = new BufferedReader(new InputStreamReader(err));
        if (errReader.readLine() != null && errReader.readLine().length() > 0) {
            System.err.println(errReader.readLine());
        }
        return result;
    }


    public List<String> getFolderFromPath() throws JSchException, SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        return folders;
    }


    public static JConsoleBuilder builder() {
        return new JConsoleBuilder();
    }

    public static class JConsoleBuilder {
        private boolean withLogging = false;
        private String userName;
        private String host;
        private String password;
        private int port = 22;

        public JConsoleBuilder() {

        }

        public JConsoleBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public JConsoleBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public JConsoleBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public JConsoleBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public JConsoleBuilder withLogger() {
            this.withLogging = true;
            return this;
        }

        public JConsole build() {
            return new JConsole(userName, host, password, port, withLogging);
        }
    }
}