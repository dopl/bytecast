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

package edu.syr.bytecast.githubcampfire;

import edu.syr.bytecast.util.RunProcess;
import edu.syr.bytecast.util.StreamEater;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GitFetch {
  
  private String m_password;
  
  public GitFetch(){
    ReadFileLine reader = new ReadFileLine();
    m_password = reader.read("private_key_pass.txt");
  }
  
  public void run(File start_dir){
    System.out.println("git fetch: "+start_dir);
    RunProcess runner = new RunProcess();
    try {
      String[] env = {"SSHPASS="+m_password};
      runner.exec("sshpass -e; git fetch", env, start_dir);
    } catch(Exception ex){
      ex.printStackTrace();
    } 
  }
}
