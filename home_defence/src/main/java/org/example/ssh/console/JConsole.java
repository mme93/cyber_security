package org.example.ssh.console;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ssh.EDataTyp;

import java.io.IOException;
import java.util.List;

public class JConsole extends AbstractJConsole{

    public JConsole(String username, String host, String passwd, int port, boolean withLogging) {
        super(username, host, passwd, port, withLogging);
    }


    @Override
    public void createFileWithContent(String content, String fileName) throws SftpException, IOException {
        super.createFileWithContent(content, fileName);
    }

    @Override
    public void rename(String oldFileName, String newFileName) throws SftpException {
        super.rename(oldFileName, newFileName);
    }

    @Override
    public void overwriteFile(String content, String fileName) throws SftpException, IOException {
        super.overwriteFile(content, fileName);
    }

    @Override
    public void appendFile(String content, String fileName) throws SftpException, IOException {
        super.appendFile(content, fileName);
    }

    @Override
    public void uploadFile(String localPath, String fileName) throws SftpException {
        super.uploadFile(localPath, fileName);
    }

    @Override
    public void uploadFile(String localPath, String fileName, String remotePath) throws SftpException {
        super.uploadFile(localPath, fileName, remotePath);
    }

    @Override
    public String readFileContent(String fileName) throws SftpException, IOException {
        return super.readFileContent(fileName);
    }

    @Override
    public String sendCommand(String command) throws JSchException, IOException {
        return super.sendCommand(command);
    }

    @Override
    public void resetSession() throws JSchException {
        super.resetSession();
    }

    @Override
    public void resetChannel() throws JSchException {
        super.resetChannel();
    }

    @Override
    public void reset() throws JSchException {
        super.reset();
    }

    @Override
    public List<String> getFolders() throws SftpException {
        return super.getFolders();
    }

    @Override
    public List<String> getFiles() throws SftpException {
        return super.getFiles();
    }

    @Override
    public List<String> getFilesByDataTyp(EDataTyp dataTyp) throws SftpException {
        return super.getFilesByDataTyp(dataTyp);
    }
}