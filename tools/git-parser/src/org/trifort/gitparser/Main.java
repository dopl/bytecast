/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://trifort.org/
 * 
 * See the file LICENSE for copying permission.
 */

package org.trifort.gitparser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
  
  private void run() {
    List<File> repos = new ArrayList<File>();
    repos.add(new File("../../"));
    repos.add(new File("../../bytecast-common/"));
    repos.add(new File("../../bytecast-fsys/"));
    repos.add(new File("../../bytecast-amd64/"));
    repos.add(new File("../../bytecast-jimple/"));
    repos.add(new File("../../bytecast-runtime/"));
    repos.add(new File("../../bytecast-documents/"));
    
    GitParser parser = new GitParser();
    List<GitPerson> people = parser.getGitPeople(repos);
    Collections.sort(people, new GitPersonLineCountComparator());
    
    Set<String> ignore_emails = new HashSet<String>();
    ignore_emails.add("Your email");
    ignore_emails.add("dhrumin@dnandola.syr.edu");
    ignore_emails.add("bytecast@debian.syr.edu");
    ignore_emails.add("root@dnandola.syr.edu");
    ignore_emails.add("mandy@kubuntu.(none)");
    ignore_emails.add("root@debian.syr.edu");
    ignore_emails.add("harsh@debian.syr.edu");
    ignore_emails.add("qsameer@gmail.com");
    
    for(GitPerson person : people){
      String email = person.getEmail();
      if(ignore_emails.contains(email)){
        continue;
      }
      System.out.println("email: "+email+" name: "+person.getName()+" lines: "+person.getLinesChanged());
    }
  }
  
  public static void main(String[] args){
    Main main = new Main();
    main.run();
  }
}
