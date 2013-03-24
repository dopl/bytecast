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

public class GitCommit {

  private String m_hash;
  private String m_message;
  private String m_email;
  
  public GitCommit(String hash, String message, String email) {
    m_hash = hash;
    m_message = message;
    m_email = email;
  }
  
  public String getHash(){
    return m_hash;
  }
  
  public String getMessage(){
    return m_message;
  }
  
  public String getEmail(){
    return m_email;
  }

  public String getSmallHash() {
    return m_hash.substring(0, 8);
  }
}
