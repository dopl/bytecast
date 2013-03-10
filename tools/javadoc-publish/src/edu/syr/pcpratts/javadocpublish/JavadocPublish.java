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

package edu.syr.pcpratts.javadocpublish;

import edu.syr.bytecast.util.CopyFile;
import edu.syr.bytecast.util.RunProcess;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JavadocPublish {

  private Map<File, String> m_rootToFolder;
  
  public JavadocPublish(){
    m_rootToFolder = new HashMap<File, String>();
    String base_path = "../../";
    m_rootToFolder.put(new File(base_path), "bytecast-root");
    m_rootToFolder.put(new File(base_path+"bytecast-common/bytecast-common/"), "bytecast-common");
    m_rootToFolder.put(new File(base_path+"bytecast-fsys/dev/bytecast.fsys/"), "bytecast-fsys");
    m_rootToFolder.put(new File(base_path+"bytecast-amd64/bytecast-amd64/"), "bytecast-amd64");
    m_rootToFolder.put(new File(base_path+"bytecast-jimple/bytecast-jimple/"), "bytecast-jimple");
    //m_rootToFolder.put(new File(base_path+"bytecast-runtime/"), "bytecast-runtime");
    m_rootToFolder.put(new File(base_path+"bytecast-test/"), "bytecast-test");
  }
  
  public void publish() {
    File dest = new File("javadocs");
    if(dest.exists()){
      remove(dest);
    }
    dest.mkdirs();
    for(File file : m_rootToFolder.keySet()){
      String path = file.getAbsolutePath()+File.separator;
      String build_xml = path+"build.xml";
      String ant_command = "ant -f "+build_xml;
      RunProcess runner = new RunProcess();
      try {
        runner.exec(ant_command, new File("."));
      } catch(Exception ex){
        ex.printStackTrace();
      }
      String dest_name = m_rootToFolder.get(file);
      String dest_folder = dest.getAbsolutePath()+File.separator+dest_name+File.separator;
      File dest_file = new File(dest_folder);
      dest_file.mkdirs();
      copyFiles(path+"dist/javadoc/", dest_folder);
    }
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
    throw new UnsupportedOperationException("Not yet implemented");
  }
  
  public static void main(String[] args) {
    JavadocPublish publisher = new JavadocPublish();
    publisher.publish();
  }

}
