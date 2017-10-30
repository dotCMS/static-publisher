package com.dotcms.osgi.publisher;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.IOException;
import java.nio.file.Path;

public class SFTPTransferMethod implements TransferMethod {

    @Override
    public void transfer(SSHClient client, Path localPath, Path remotePath) throws IOException {
        client.newSFTPClient().put(new FileSystemFile(localPath.toString()), remotePath.toString());
    }
}
