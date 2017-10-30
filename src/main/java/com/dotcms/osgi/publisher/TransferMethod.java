package com.dotcms.osgi.publisher;

import net.schmizz.sshj.SSHClient;

import java.io.IOException;
import java.nio.file.Path;

interface TransferMethod {
    void transfer(SSHClient client, Path localPath, Path remotePath) throws IOException;
}
