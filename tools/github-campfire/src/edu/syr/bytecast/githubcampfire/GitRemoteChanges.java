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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GitRemoteChanges {

  public List<GitCommit> parse(String url, File folder){
    List<GitCommit> ret = new ArrayList<GitCommit>();
    
    RunProcess runner = new RunProcess();
    try {
      GitFetch git_fetch = new GitFetch();
      git_fetch.run(url, folder);
      
      runner.exec("git log master", folder);
    } catch(Exception ex){
      ex.printStackTrace();
    }
    
    List<String> lines = runner.getOutput();
    for(int i = 0; i < lines.size(); ++i){
      String curr = lines.get(i);
      int message_line = i + 1;
      int author_line = i + 1;
      String message = "";
      while(message_line < lines.size()){
        message = lines.get(message_line);
        if(message.trim().equals("")){
          ++message_line;
          break;
        }
        ++message_line;
      }
      
      if(message_line < lines.size()){
        message = lines.get(message_line);
      }
      
      String author = "";
      while(author_line < lines.size()){
        author = lines.get(author_line).trim();
        if(author.startsWith("Author: ")){
          String[] tokens = author.split("Author: ");
          String author_token = tokens[1];
          String[] tokens2 = author_token.split("<");
          String email = tokens2[1];
          author = email.substring(0, email.length()-1);
          break;
        }
        author_line++;
      }
      
      String example_commit_line = "commit e0a352588b70f544857aa9cfb1177e301d62bd1d";
      if(curr.length() == example_commit_line.length()){
        if(curr.startsWith("commit")){
          String[] hash_tokens = curr.split(" ");
          String hash = hash_tokens[1].trim();
          message = message.trim();
          ret.add(new GitCommit(hash, message, author));
        }
      }
    }
    
    return ret;
  }
}
