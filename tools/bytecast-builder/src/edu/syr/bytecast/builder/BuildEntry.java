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
import java.io.File;
import java.util.List;

public class BuildEntry {

  private String m_antFilename;
  
  public BuildEntry(String ant_filename) {
    m_antFilename = ant_filename;
  }

  public void build() {
    String command = "ant -logger org.apache.tools.ant.XmlLogger -f "+m_antFilename;
    RunProcess runner = new RunProcess();
    try {
      runner.exec(command, new File("."));
      List<String> output = runner.getOutput();
      for(String line : output){
        System.out.println(line);
      }
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
}
