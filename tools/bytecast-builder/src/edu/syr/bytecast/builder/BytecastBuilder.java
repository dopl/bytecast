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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BytecastBuilder {

  private List<BuildEntry> m_buildEntries;
  
  private BytecastBuilder(){
    m_buildEntries = new ArrayList<BuildEntry>();
  }
  
  public void build(File root){
    String base_path = root.getAbsolutePath()+File.separator;
    m_buildEntries.add(new BuildEntry(base_path+"bytecast-common/bytecast-common/build.xml"));
    m_buildEntries.add(new BuildEntry(base_path+"bytecast-fsys/dev/bytecast.fsys/build.xml"));
    m_buildEntries.add(new BuildEntry(base_path+"bytecast-amd64/bytecast-amd64/build.xml"));
    m_buildEntries.add(new BuildEntry(base_path+"bytecast-jimple/bytecast-jimple/build.xml"));
    
    for(BuildEntry entry : m_buildEntries){
      entry.build();
    }
  }
  
  public static void main(String[] args) {
    BytecastBuilder builder = new BytecastBuilder();
    builder.build(new File("../../"));
  }
}
