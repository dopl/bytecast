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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

public class CampfirePostReply {
  
  private int m_statusCode;
  private String m_reason;
  private List<String> m_entity;
  
  public CampfirePostReply(HttpResponse response){
    StatusLine line = response.getStatusLine();
    m_statusCode = line.getStatusCode();
    m_reason = line.getReasonPhrase();
    m_entity = new ArrayList<String>();
   
    try {
      HttpEntity resp_entity = response.getEntity();
      BufferedReader reader = new BufferedReader(new InputStreamReader(resp_entity.getContent()));
      while(true){
        String curr_line = reader.readLine();
        if(curr_line != null){
          m_entity.add(curr_line);
        } else {
          break;
        }
      } 
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  public int getStatusCode(){
    return m_statusCode;
  }
  
  public String getReason(){
    return m_reason;
  }
  
  public List<String> getEntity(){
    return m_entity;
  }
  
  @Override
  public String toString(){
    String ret = "["+m_statusCode+": "+m_reason+"]";
    return ret;
  }
}
