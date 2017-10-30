package com.dotcms.osgi.publisher;

import com.dotcms.osgi.PluginProperties;
import com.dotmarketing.util.Logger;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.PKCS8KeyFile;
import net.schmizz.sshj.userauth.method.AuthPublickey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class StaticPublisher {
    private List<String> hosts;
    private SSHClient client;
    private AuthPublickey authPublickey;
    private String username;
    private TransferMethod transferMethod = new SCPTransferMethod(); // default transfer method

    public void setTransferMethod(TransferMethod transferMethod) {
        this.transferMethod = transferMethod;
    }

    public void publish(Path localPath, Path remotePath) {
        List<String> hosts = getHosts();
        SSHClient ssh = getSSHClient();
        try {
            ssh.loadKnownHosts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String host : hosts) {
            try {
                ssh.connect(host);
                ssh.auth(getUsername(), getAuthPublickey());

                // Present here to demo algorithm renegotiation - could have just put this before connect()
                // Make sure JZlib is in classpath for this to work
                ssh.useCompression();

                transferMethod.transfer(ssh, localPath, remotePath);
            } catch(IOException e) {
                Logger.error(this, "");

            }finally {
                try {
                    ssh.disconnect();
                } catch(IOException e) {
                    Logger.error(this, "Unable to disconnect from SSH.", e);
                }
            }
        }
    }

    private List<String> getHosts() {
        if(hosts==null) {
            String[] hostsArray = PluginProperties.getPropertyArray("hosts");
            hosts = Arrays.asList(hostsArray);
        }

        return hosts;
    }

    private SSHClient getSSHClient() {
        if(client==null) {
            client = new SSHClient();
        }

        return client;
    }

    private AuthPublickey getAuthPublickey() {
        if(authPublickey==null) {
            PKCS8KeyFile keyFile = new PKCS8KeyFile();
            keyFile.init(new File(PluginProperties.getProperty("key.file.path")));
            authPublickey = new AuthPublickey(keyFile);
        }

        return authPublickey;
    }

    private String getUsername() {
        if(username==null) {
            username = PluginProperties.getProperty("ssh.user");
        }

        return username;
    }

    public static void main(String[] args) throws IOException {
        StaticPublisher publisher = new StaticPublisher();
        publisher.publish(Paths.get("/Users/danielsilva/Documents/dotcms-test-demo.dotcms.com-en-us"),
                Paths.get("dotcms-test-demo.dotcms.com-en-us"));

    }
}
