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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class CampfirePost {

  private String m_apiKey;
  private int m_room;
  
  public CampfirePost(int room){
    m_apiKey = readApiKey();
    m_room = room;
  }
  
  private String readApiKey(){
    ReadFileLine reader = new ReadFileLine();
    return reader.read("api_key.txt");
  }
  
  public CampfirePostReply post(String message) {
    //see: http://stackoverflow.com/questions/2603691/android-httpclient-and-https
    try {
      String xml_message = "<message><type>TextMessage</type><body>"+message+"</body></message>";
      
      SchemeRegistry schemeRegistry = new SchemeRegistry();   
      schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

      BasicSchemeFactory factory = new BasicSchemeFactory();
      
      HttpParams params = new BasicHttpParams();
      params.setParameter("realm", "https://trifort.campfirenow.com/");
      
      SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);
      DefaultHttpClient client = new DefaultHttpClient(mgr, params);
      String url = "https://trifort.campfirenow.com/room/"+m_room+"/speak.xml";
      HttpPost post = new HttpPost(url);
      post.setHeader("Content-type", "application/xml");
      
      AuthScheme scheme = factory.newInstance(params);
      Header header = scheme.authenticate(new UsernamePasswordCredentials(m_apiKey), post);
      
      HttpEntity entity = new StringEntity(xml_message);
      post.setEntity(entity);
      post.setHeader(header);
      HttpResponse response = client.execute(post);
      
      return new CampfirePostReply(response);
    } catch(Exception ex){//post.setEntity;
      ex.printStackTrace();
      return null;
    }
  }
  
  public static void main(String[] args){
    CampfirePost poster = new CampfirePost(551294);
    poster.post("testing CampfirePost.java");
  }

}
