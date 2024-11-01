package org.example.ssh.console;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ssh.EDataTyp;

import java.io.*;
import java.util.List;


public interface IJConsole {

    void loadSetup() throws JSchException;

    void createSession() throws JSchException;

    void createChannels() throws JSchException;

    String cd(String path) throws SftpException;

    String pwd() throws SftpException;

    void mkdir(String path) throws SftpException;

    void rmdir(String folder) throws SftpException;

    boolean existDir(String folder) throws JSchException, SftpException;

    void createFileWithContent(String content, String fileName) throws SftpException, IOException;

    void rename(String oldFileName, String newFileName) throws SftpException;

    void overwriteFile(String content, String fileName) throws SftpException, IOException;

    void appendFile(String content, String fileName) throws SftpException, IOException;

    void uploadFile(String localPath, String fileName) throws SftpException;

    void uploadFile(String localPath, String fileName, String remotePath) throws SftpException;

    String readFileContent(String fileName) throws SftpException, IOException;

    String sendCommand(String command) throws JSchException, IOException;

    List<String> getFolders() throws JSchException, SftpException;

    List<String> getFiles() throws JSchException, SftpException;

    List<String> getFilesByDataTyp(EDataTyp dataTyp) throws JSchException, SftpException;

    void resetSession() throws JSchException;

    void resetChannel() throws JSchException;

    void reset() throws JSchException;
}
