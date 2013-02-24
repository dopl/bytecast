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
import java.io.FileReader;

public class CampfirePost {

  private String m_apiKey;
  
  public CampfirePost(){
    m_apiKey = readApiKey();
  }
  
  private String readApiKey(){
    try {
      BufferedReader reader = new BufferedReader(new FileReader("api_key.txt"));
      String ret = reader.readLine();
      reader.close();
      return ret;
    } catch(Exception ex){
      ex.printStackTrace();
      return "";
    }
  }
  
  private void post(String message) {
    
  }
  
  public static void main(String[] args){
    CampfirePost poster = new CampfirePost();
    poster.post("hello world from github-campfire.jar");
  }

}
