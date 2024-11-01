package org.example.ssh.console;

import com.jcraft.jsch.*;
import org.example.ssh.EDataTyp;
import org.example.ssh.logger.JConsoleLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.jcraft.jsch.ChannelSftp.APPEND;
import static com.jcraft.jsch.ChannelSftp.OVERWRITE;
import static org.example.ssh.EDataTyp.TXT;

public abstract class AbstractJConsole extends JSch implements IJConsole {

    private ChannelSftp sftpChannel;
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

    @Override
    public void loadSetup() throws JSchException {
        createSession();
        createChannels();
    }

    @Override
    public final void createSession() throws JSchException {
        session = getSession(username, host, 22);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    @Override
    public final void createChannels() throws JSchException {
        sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
    }

    @Override
    public final String cd(String path) throws SftpException {
        sftpChannel.cd(String.format("%s/%s", pwd(), path));
        return sftpChannel.pwd();
    }

    @Override
    public final String pwd() throws SftpException {
        return sftpChannel.pwd();
    }

    @Override
    public final void mkdir(String path) throws SftpException {
        sftpChannel.mkdir(path);
    }

    @Override
    public final void rmdir(String folder) throws SftpException {
        sftpChannel.rmdir(folder);
    }

    @Override
    public final boolean existDir(String folder) throws JSchException, SftpException {
        return getFolders().stream().filter(folderName -> folderName.equals(folder)).toList().size() == 1;
    }

    @Override
    public void createFileWithContent(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName);
        inputStream.close();
    }

    @Override
    public void rename(String oldFileName, String newFileName) throws SftpException {
        sftpChannel.rename(
                String.format("%s/%s", oldFileName),
                String.format("%s/%s", newFileName)
        );
    }

    @Override
    public void overwriteFile(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName, OVERWRITE);
    }

    @Override
    public void appendFile(String content, String fileName) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftpChannel.put(inputStream, fileName, APPEND);
    }

    @Override
    public void uploadFile(String localPath, String fileName) throws SftpException {
        sftpChannel.put(localPath, fileName);
    }

    @Override
    public void uploadFile(String localPath, String fileName, String remotePath) throws SftpException {
        uploadFile(localPath, String.format("%s/%s", remotePath, fileName));
    }

    @Override
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

    @Override
    public String sendCommand(String command) throws JSchException, IOException {
        return "";
    }

    @Override
    public void resetSession() throws JSchException {
        createSession();
    }

    @Override
    public void resetChannel() throws JSchException {
        createChannels();
    }

    @Override
    public void reset() throws JSchException {
        resetSession();
        resetChannel();
    }

    @Override
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

    @Override
    public List<String> getFiles() throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        return folders.stream().filter(folder -> !(folder.equals(".") || folder.equals(".."))).toList();
    }

    @Override
    public List<String> getFilesByDataTyp(EDataTyp dataTyp) throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        String typ;
        if (dataTyp.equals(TXT)) {
            typ = ".txt";
        } else {
           throw new RuntimeException(String.format("No Data Typ find by parameter: "+dataTyp));
        }
        return folders.stream().filter(folder -> folder.contains(typ)).toList();
    }
}
