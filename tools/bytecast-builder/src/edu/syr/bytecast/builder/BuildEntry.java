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

import edu.syr.bytecast.util.RunProcess;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class BuildEntry {

  private String m_antFilename;
  private String m_projectName;
  
  public BuildEntry(String project_name, String ant_filename) {
    m_antFilename = ant_filename;
    m_projectName = project_name;
  }

  public void build() {
    System.out.println("building: "+m_projectName);
    String command = "ant -logger org.apache.tools.ant.XmlLogger -f "+m_antFilename;
    RunProcess runner = new RunProcess();
    try {
      runner.exec(command, new File("."));
      List<String> output = runner.getOutput();
      output.remove(0);
      InputStream is = createInputStream(output);
      
      AntXmlHandler handler = new AntXmlHandler();
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      SAXParser saxParser = spf.newSAXParser();
      saxParser.parse(is, handler);
      
      if(handler.getBuildPassed()){
        System.out.println("  passed.");
      } else {
        System.out.println("  failed.");
      } 
      
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }

  private InputStream createInputStream(List<String> output) {
    StringBuilder builder = new StringBuilder();
    for(String str : output){
      builder.append(str);
      builder.append("\n");
    }
    String full_string = builder.toString();
    try {
      return new ByteArrayInputStream(full_string.getBytes("UTF-8"));
    } catch(Exception ex){
      ex.printStackTrace();
      System.exit(0);
      return null;
    }
  }
  
}
