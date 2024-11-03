package org.example.defender.ssh.session.channel.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Sftp implements ISftp {

    private Session session;
    private ChannelSftp sftp;

    public Sftp(Session session) {
        this.session = session;
    }

    public void connect() throws JSchException {
        sftp = (ChannelSftp) session.openChannel("sftp");
    }

    public void disconnect() {
        sftp.disconnect();
    }

    public void reconnect() throws JSchException {
        disconnect();
        connect();
    }

    @Override
    public void overwriteSession(Session session) {
        this.session.disconnect();
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String pwd() throws SftpException {
        return sftp.pwd();
    }

    @Override
    public void mkdir(String path) throws SftpException {
        sftp.mkdir(path);
    }

    @Override
    public void rmdir(String folder) throws SftpException {
        sftp.rmdir(folder);
    }

    @Override
    public boolean existDir(String folder) throws SftpException {
        return getFolders().stream().filter(folderName -> folderName.equals(folder)).toList().size() == 1;
    }

    @Override
    public void rename(String oldFileName, String newFileName) throws SftpException {
        sftp.rename(
                String.format("%s/%s", oldFileName),
                String.format("%s/%s", newFileName)
        );
    }

    @Override
    public void put(String content, String fileName) throws SftpException, IOException {
        put(content, fileName, 0);
    }

    @Override
    public void put(String content, String fileName, int mode) throws SftpException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        sftp.put(inputStream, fileName, mode);
    }

    @Override
    public List<String> getFolders() throws SftpException {
        List<String> folders = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> files = sftp.ls(".");
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
        Vector<ChannelSftp.LsEntry> files = sftp.ls(".");
        files.forEach(file -> folders.add(file.getFilename()));
        return folders.stream().filter(folder -> !(folder.equals(".") || folder.equals(".."))).toList();
    }

    @Override
    public void uploadFile(String localPath, String fileName) throws SftpException {
        sftp.put(localPath, fileName);
    }

    @Override
    public void uploadFile(String localPath, String fileName, String remotePath) throws SftpException {
        uploadFile(localPath, String.format("%s/%s", remotePath, fileName));
    }

    @Override
    public String readFileContent(String fileName) throws SftpException, IOException {
        StringBuilder content = new StringBuilder();
        InputStream inputStream = sftp.get(String.format("%s/%s", pwd(), fileName));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }
}
