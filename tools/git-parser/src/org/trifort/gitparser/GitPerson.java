/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://trifort.org/
 * 
 * See the file LICENSE for copying permission.
 */

package org.trifort.gitparser;

import java.util.ArrayList;
import java.util.List;

public class GitPerson {
  
  private String m_name;
  private String m_email;
  private List<GitCommit> m_commits;
  
  public GitPerson(String name, String email){
    m_name = name;
    m_email = email;
    m_commits = new ArrayList<GitCommit>();
  }
  
  public void addGitCommit(GitCommit commit){
    m_commits.add(commit);
  }
  
  public List<GitCommit> getGitCommits(){
    return m_commits;
  }
  
  public String getName(){
    return m_name;
  }
  
  public String getEmail(){
    return m_email;
  }
  
  public int getLinesChanged(){
    int sum = 0;
    for(GitCommit commit : m_commits){
      sum += commit.getLinesChanged();
    }
    return sum;
  }
}
