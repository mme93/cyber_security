package org.example.ssh.console.channel.sftp;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.util.List;

public interface ISftp {

    void overwriteSession(Session session);

    Session getSession();

    String pwd() throws SftpException;

    void mkdir(String path) throws SftpException;

    void rmdir(String folder) throws SftpException;

    boolean existDir(String folder) throws SftpException;

    void rename(String oldFileName, String newFileName) throws SftpException;

    void put(String content, String fileName) throws SftpException, IOException;

    void put(String content, String fileName, int mode) throws SftpException, IOException;

    List<String> getFolders() throws SftpException;

    List<String> getFiles() throws SftpException;

    void uploadFile(String localPath, String fileName) throws SftpException;

    void uploadFile(String localPath, String fileName, String remotePath) throws SftpException;

    String readFileContent(String fileName) throws SftpException, IOException;

}
