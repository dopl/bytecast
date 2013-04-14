/*
 * This file is part of Bytecast.
 *
 * Bytecast is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bytecast is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bytecast.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.syr.bytecast.javadocpublish;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.SftpProgressMonitor;
import edu.syr.bytecast.util.ZipFolder;
import edu.syr.bytecast.util.CopyFile;
import edu.syr.bytecast.util.RunProcess;
import edu.syr.bytecast.util.SecureCopy;
import edu.syr.bytecast.util.SshExec;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.Identity;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.userauth.keyprovider.KeyPairWrapper;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.userauth.keyprovider.OpenSSHKeyFile;
import org.bouncycastle.jce.provider.JDKKeyStore;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

public class JavadocPublish {

  private List<String> m_projects;
  
  public JavadocPublish(){
    m_projects = new ArrayList<String>();
    String base_path = "../../";
    m_projects.add(base_path+"bytecast-common/bytecast-common/src/");
    m_projects.add(base_path+"bytecast-fsys/dev/bytecast.fsys/src/");
    m_projects.add(base_path+"bytecast-amd64/bytecast-amd64/src/");
    m_projects.add(base_path+"bytecast-jimple/bytecast-jimple/src/");
    m_projects.add(base_path+"bytecast-test/src/");
    m_projects.add(base_path+"bytecast-interp/src/bytecast-interp/src/");
    m_projects.add(base_path+"bytecast-exec/");
  }
  
  public void publish() throws Exception {
    File dest = new File("bytecast-all");
    File dest_src = new File("bytecast-all/src");
    if(dest.exists()){
      remove(dest_src);
    }
    dest_src.mkdirs();
    String dest_folder = dest_src.getAbsolutePath()+File.separator;
    for(String project : m_projects){
      copyFiles(project, dest_folder);
    }
    
    String build_xml = dest+File.separator+"build.xml";
    String ant_command = "ant javadoc -f "+build_xml;
    RunProcess runner = new RunProcess();
    try {
      runner.exec(ant_command, new File("."));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    
    ZipFolder zipper = new ZipFolder();
    try {
      zipper.zip("bytecast-all/dist/javadoc", "bytecast-javadoc.zip");
    } catch(Exception ex){
      ex.printStackTrace();
    }
    
    SecureCopy secure_copy = new SecureCopy("/home/pcpratts/.ssh/id_rsa", 
      readPassword("id_rsa_passwd"), "/home/pcpratts/.ssh/known_hosts", 
      "pcpratts", "trifort.org");
    
    secure_copy.copy("bytecast-javadoc.zip", "bytecast-javadoc.zip");
    
    SshExec ssh_exec = new SshExec("/home/pcpratts/.ssh/id_rsa", 
      readPassword("id_rsa_passwd"), "/home/pcpratts/.ssh/known_hosts", 
      "pcpratts", "trifort.org");
    
    ssh_exec.exec("rm -rf /home/pcpratts/code/trifort_www/bytecast/javadoc");
    ssh_exec.exec("unzip bytecast-javadoc.zip");
    ssh_exec.exec("mv javadoc /home/pcpratts/code/trifort_www/bytecast/");
  }
  
  private byte[] readPassword(String filename) throws Exception {
    InputStream fin = new FileInputStream(filename);
    byte[] buffer = new byte[4096];
    int len = fin.read(buffer);
    fin.close();
    byte[] ret = new byte[len-1];
    for(int i = 0; i < len - 1; ++i){
      ret[i] = buffer[i];
    }
    return ret;
  }
  
  private void copyFiles(String src, String dest) {
    File src_file = new File(src);
    File[] children = src_file.listFiles();
    for(File child : children){
      if(child.isDirectory()){
        copyFiles(child.getAbsolutePath(), dest+File.separator+child.getName());
      } else {
        File dest_file = new File(dest);
        dest_file.mkdirs();
        CopyFile copier = new CopyFile();
        try {
          copier.copy(child.getAbsolutePath(), dest+File.separator+child.getName());
        } catch(Exception ex){
          ex.printStackTrace();
        }
      }
    }
  }
  
  private void remove(File dest) {
    if(dest.exists() == false){
      return;
    }
    File[] children = dest.listFiles();
    for(File child : children){
      if(child.isDirectory()){
        remove(child);
      } else {
        child.delete();
      }
    }
    dest.delete();
  }
  
  public static void main(String[] args) {
    JavadocPublish publisher = new JavadocPublish();
    try {
      publisher.publish();
      System.exit(0);
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }
}
