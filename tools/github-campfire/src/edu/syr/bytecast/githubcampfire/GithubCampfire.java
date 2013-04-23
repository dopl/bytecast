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

import edu.syr.bytecast.javadocpublish.JavadocPublish;
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
      String url = repo.getGithubCommitUrl()+curr.getHash();
      String message = "["+repo.getShortName()+"] &lt;"+curr.getEmail()+"&gt; "+curr.getMessage()+" ("+url+")";
      CampfirePostReply reply = m_campfirePost.post(message);
      System.out.println("message: "+message);
      if(reply.getStatusCode() != 201){
        System.out.println("  problem: "+reply.toString());
      }
    }
    
    if(diff != 0){
      System.out.println("publishing javadocs...");
      JavadocPublish publisher = new JavadocPublish();
      try {
        publisher.publish();
      } catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }
  
  private void run(String base_path) {
    GithubEntries entries = new GithubEntries();
    List<GithubEntry> repos = entries.get(base_path);
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
