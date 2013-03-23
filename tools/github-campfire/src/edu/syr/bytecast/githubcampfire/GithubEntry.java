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
import java.util.List;

public class GithubEntry {

  private String m_shortName;
  private File m_path;
  private String m_url;
  private List<GitCommit> m_prevLog;
  
  public GithubEntry(String short_name, File path, String url){
    m_shortName = short_name;
    m_path = path;
    m_url = url;
    m_prevLog = null;
  }
  
  public String getShortName(){
    return m_shortName;
  }
  
  public File getPath(){
    return m_path;
  }
  
  public String getUrl(){
    return m_url;
  }
  
  public List<GitCommit> getPreviousLog(){
    return m_prevLog;
  }
  
  public void setPreviousLog(List<GitCommit> log){
    m_prevLog = log;
  }
}
