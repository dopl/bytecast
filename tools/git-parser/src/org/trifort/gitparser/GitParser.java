/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://trifort.org/
 * 
 * See the file LICENSE for copying permission.
 */

package org.trifort.gitparser;

import edu.syr.bytecast.util.RunProcess;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitParser {

  private Map<String, GitPerson> m_emailToGitPerson;
  
  public GitParser(){
    m_emailToGitPerson = new HashMap<String, GitPerson>();
  }
  
  public List<GitPerson> getGitPeople(List<File> repos){ 
    for(File repo : repos){
      findGitPeople(repo);
    }
    List<GitPerson> ret = new ArrayList<GitPerson>();
    ret.addAll(m_emailToGitPerson.values());
    return ret;
  }

  private void findGitPeople(File repo) {
    RunProcess runner = new RunProcess();
    try {
      System.out.println("finding for: "+repo);
      runner.exec("git log", repo);
    } catch(Exception ex){
      ex.printStackTrace(System.out); 
      System.exit(1);
    }
    List<String> lines = runner.getOutput();
    for(int i = 0; i < lines.size() - 1; ++i){
      String curr = lines.get(i);
      String next = lines.get(i + 1);
      
      String example_commit_line = "commit e0a352588b70f544857aa9cfb1177e301d62bd1d";
      if(curr.length() == example_commit_line.length()){
        if(curr.startsWith("commit ") && next.startsWith("Author: ")){
          addGitCommit(curr, next, repo);
        }
      }
    }
  }

  private void addGitCommit(String curr, String next, File repo) {
    String[] commit_tokens = curr.split(" ");
    String commit = commit_tokens[1].trim();
    String[] name_tokens = next.split("Author: ");
    String name_str = name_tokens[1];
    String[] name_tokens2 = name_str.split("<");
    String full_name = name_tokens2[0].trim();
    String email = name_tokens2[1].trim().replace(">", "");
    
    if(m_emailToGitPerson.containsKey(email)){
      GitPerson person = m_emailToGitPerson.get(email);
      person.addGitCommit(new GitCommit(commit, repo));
    } else {
      GitPerson person = new GitPerson(full_name, email);
      person.addGitCommit(new GitCommit(commit, repo));
      m_emailToGitPerson.put(email, person);
    } 
  }
}
