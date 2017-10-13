package com.dotcms.osgi.publisher;

import com.dotcms.osgi.PluginProperties;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.PKCS8KeyFile;
import net.schmizz.sshj.userauth.method.AuthPublickey;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SCPPublisher {

    private Path localPath;
    private Path remotePath;

    public SCPPublisher(Path localPath, Path remotePath) {
        this.localPath = localPath;
        this.remotePath = remotePath;
    }

    public void publish() throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect("remote-server");
        try {
            PKCS8KeyFile keyFile = new PKCS8KeyFile();
            keyFile.init(new File(PluginProperties.getProperty("key.file.path")));
            ssh.auth(PluginProperties.getProperty("ssh.user"), new AuthPublickey(keyFile) );

            // Present here to demo algorithm renegotiation - could have just put this before connect()
            // Make sure JZlib is in classpath for this to work
            ssh.useCompression();

            final String src = "";
            final String dest = "";
            ssh.newSCPFileTransfer().upload(new FileSystemFile(src), dest);
        } finally {
            ssh.disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
        SCPPublisher scpPublisher = new SCPPublisher(null, null);
        scpPublisher.publish();

    }
}
