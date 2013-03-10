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
import java.util.ArrayList;
import java.util.List;

public class JavadocPublish {

  private List<String> m_projects;
  
  public JavadocPublish(){
    m_projects = new ArrayList<String>();
    String base_path = "../../";
    m_projects.add(base_path+"bytecast-common/bytecast-common/src/");
    m_projects.add(base_path+"bytecast-fsys/dev/bytecast.fsys/src/");
    m_projects.add(base_path+"bytecast-amd64/bytecast-amd64/src/");
    m_projects.add(base_path+"bytecast-jimple/bytecast-jimple/src/");
    m_projects.add(base_path+"bytecast-test//src/");
  }
  
  public void publish() {
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
    
    //zip javadocs folder
    
    //copy javadocs folder to remote
    
    //unzip in remote
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
    publisher.publish();
  }

}
