package edu.syr.bytecast.javadocpublish;

import java.security.PublicKey;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class SimpleSSHClient extends SSHClient {
  
  public SimpleSSHClient(){
    addHostKeyVerifier(new HostKeyVerifier() {
      @Override
      public boolean verify(String h, int p, PublicKey k) {
        return true;
      }
    }); 
  }
}
