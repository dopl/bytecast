/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.bytecast.githubcampfire;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GithubEntries {
  
  public List<GithubEntry> get(String base_path){
    List<GithubEntry> repos = new ArrayList<GithubEntry>();
    repos.add(new GithubEntry("bytecast-root", new File(base_path), "git://github.com/dopl/bytecast.git", "https://github.com/dopl/bytecast/commit/"));
    repos.add(new GithubEntry("bytecast-common", new File(base_path+"bytecast-common/"), "git://github.com/dopl/bytecast-common.git", "https://github.com/dopl/bytecast-common/commit/"));
    repos.add(new GithubEntry("bytecast-fsys", new File(base_path+"bytecast-fsys/"), "git://github.com/dopl/bytecast-fsys.git", "https://github.com/dopl/bytecast-fsys/commit/"));
    repos.add(new GithubEntry("bytecast-amd64", new File(base_path+"bytecast-amd64/"), "git://github.com/dopl/bytecast-amd64.git", "https://github.com/dopl/bytecast-amd64/commit/"));
    repos.add(new GithubEntry("bytecast-jimple", new File(base_path+"bytecast-jimple/"), "git://github.com/dopl/bytecast-jimple.git", "https://github.com/dopl/bytecast-jimple/commit/"));
    repos.add(new GithubEntry("bytecast-runtime", new File(base_path+"bytecast-runtime/"), "git://github.com/dopl/bytecast-runtime.git", "https://github.com/dopl/bytecast-runtime/commit/"));
    repos.add(new GithubEntry("bytecast-documents", new File(base_path+"bytecast-documents/"), "git://github.com/dopl/bytecast-documents.git", "https://github.com/dopl/bytecast-documents/commit/"));
    repos.add(new GithubEntry("bytecast-test", new File(base_path+"bytecast-test/"), "git://github.com/dopl/bytecast-test.git", "https://github.com/dopl/bytecast-test/commit/"));
    repos.add(new GithubEntry("bytecast-interp", new File(base_path+"bytecast-interp/"), "git://github.com/dopl/bytecast-interp.git", "https://github.com/dopl/bytecast-interp/commit/"));
    repos.add(new GithubEntry("bytecast-exec", new File(base_path+"bytecast-exec/"), "git://github.com/dopl/bytecast-exec.git", "https://github.com/dopl/bytecast-exec/commit/"));
    return repos;
  }
}
