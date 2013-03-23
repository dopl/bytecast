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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GithubCampfire {
  
  private CampfirePost m_campfirePost;

  public GithubCampfire(){
    m_campfirePost = new CampfirePost(551294);  
  }
  
  private void runOne(GithubEntry repo) {
    List<GitCommit> prev = repo.getPreviousLog();
    GitRemoteChanges remote_changes = new GitRemoteChanges();
    List<GitCommit> next = remote_changes.parse(repo.getUrl(), repo.getPath());
    
    repo.setPreviousLog(next);
    if(prev == null){
      return;
    }
    
    int prev_size = prev.size();
    int next_size = next.size();
    
    int diff = next_size - prev_size;
    for(int i = 0; i < diff; ++i){
      GitCommit curr = next.get(i);
      String message = "["+repo.getShortName()+"] "+curr.getSmallHash()+": "+curr.getMessage();
      CampfirePostReply reply = m_campfirePost.post(message);
      System.out.println("message: "+message);
      if(reply.getStatusCode() != 201){
        System.out.println("  problem: "+reply.toString());
      }
    }
    
  }
  
  private void run(String base_path) {
    List<GithubEntry> repos = new ArrayList<GithubEntry>();
    repos.add(new GithubEntry("bytecast-root", new File(base_path), "git://github.com/dopl/bytecast.git"));
    repos.add(new GithubEntry("bytecast-common", new File(base_path+"bytecast-common/"), "git://github.com/dopl/bytecast-common.git"));
    repos.add(new GithubEntry("bytecast-fsys", new File(base_path+"bytecast-fsys/"), "git://github.com/dopl/bytecast-fsys.git"));
    repos.add(new GithubEntry("bytecast-amd64", new File(base_path+"bytecast-amd64/"), "git://github.com/dopl/bytecast-amd64.git"));
    repos.add(new GithubEntry("bytecast-jimple", new File(base_path+"bytecast-jimple/"), "git://github.com/dopl/bytecast-jimple.git"));
    repos.add(new GithubEntry("bytecast-runtime", new File(base_path+"bytecast-runtime/"), "git://github.com/dopl/bytecast-runtime.git"));
    repos.add(new GithubEntry("bytecast-documents", new File(base_path+"bytecast-documents/"), "git://github.com/dopl/bytecast-documents.git"));
    repos.add(new GithubEntry("bytecast-test", new File(base_path+"bytecast-test/"), "git://github.com/dopl/bytecast-test.git"));
    
    while(true){
      for(GithubEntry repo : repos){
        runOne(repo);
      }
      try {
        Thread.sleep(1000);
      } catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args) {
    GithubCampfire program = new GithubCampfire();
    program.run("../../");
  }
}
