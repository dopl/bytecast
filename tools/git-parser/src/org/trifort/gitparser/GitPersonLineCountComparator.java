/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://trifort.org/
 * 
 * See the file LICENSE for copying permission.
 */

package org.trifort.gitparser;

import java.util.Comparator;

public class GitPersonLineCountComparator implements Comparator<GitPerson> {

  @Override
  public int compare(GitPerson o1, GitPerson o2) {
    return Integer.valueOf(o1.getLinesChanged()).compareTo(Integer.valueOf(o2.getLinesChanged()));
  }
}
