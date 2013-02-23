/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://trifort.org/
 * 
 * See the file LICENSE for copying permission.
 */

package org.trifort.gitparser;

import edu.syr.bytecast.util.RunProcess;
import java.io.File;
import java.util.List;

public class GitCommit {
  
  private String m_hash;
  private File m_repo;
  private int m_linesChanged;
  private boolean m_linesChangedSet;
  
  public GitCommit(String hash, File repo){
    m_hash = hash;
    m_repo = repo;
    m_linesChangedSet = false;
  }
  
  public int getLinesChanged(){
    if(m_linesChangedSet == false){
      findLinesChanged();
      m_linesChangedSet = true;
    }
    return m_linesChanged;
  }

  private void findLinesChanged() {
    RunProcess runner = new RunProcess();
    try {
      runner.exec("git show "+m_hash, m_repo);
    } catch(Exception ex){
      ex.printStackTrace();
      System.exit(1);
    }
    List<String> lines = runner.getOutput();
    boolean inside_java = false;
    for(String line : lines){
      if(line.startsWith("+")){
        if(line.startsWith("+++") == false){
          if(inside_java){
            m_linesChanged++;
          }
        } else {
          if(line.trim().endsWith(".java")){
            inside_java = true;
          } else {
            inside_java = false;
          }
        }
      } else if(line.startsWith("-")){
        if(line.startsWith("---") == false){
          if(inside_java){
            m_linesChanged++;
          }
        }
      }
    }
  }
}
