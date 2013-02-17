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
package edu.syr.bytecast.builder;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AntXmlHandler extends DefaultHandler {
  
  private boolean m_buildPassed;
  
  public AntXmlHandler(){
    m_buildPassed = true;
  }
  
  @Override
  public void startElement(String namespaceURI, String localName, String qName, 
    Attributes atts) throws SAXException {
    
    if(localName.equals("build")){
      for(int i = 0; i < atts.getLength(); ++i){
        String name = atts.getLocalName(i);
        if(name.equals("error")){
          m_buildPassed = false;
        }
      }
    }
  }
  
  public boolean getBuildPassed(){
    return m_buildPassed;
  }
}
