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

package edu.syr.bytecast.callgraph;
import java.util.ArrayList;
import java.util.List;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.options.Options;

/**
 *
 * @author adodds
 */
public class BytecastCGSoot {
    
 public void run(String output_format, BytecastCGBodyTransform btform) {
    G.reset();
    
    Options.v().set_prepend_classpath(true);
    List<String> process_dir = new ArrayList<String>();
    process_dir.add("bytecast-all/build/classes/");
    Options.v().set_process_dir(process_dir);
    //need to add library paths to class path.
    Options.v().set_soot_classpath("bytecast-all/build/classes/:../../bytecast-jimple/bytecast-jimple/lib/soot-2.5.0.jar:../../lib/jsch-0.1.49.jar:../../lib/commons-io-1.4.jar:../junit-4.10/junit-4.10.jar");
    Options.v().set_output_format(Options.output_format_J);
   // Options.v().set_include_all(true);
    //Options.v().set_whole_program(true);
    Options.v().allow_phantom_refs();
    Scene.v().loadClassAndSupport("edu.syr.bytecast.fsys.elf.ElfExeObjParser");
    SootClass elf_exe_parser = Scene.v().getSootClass("edu.syr.bytecast.fsys.elf.ElfExeObjParser");
    Scene.v().setMainClass(elf_exe_parser);
    

    Transform tform = new Transform("jtp.BytecastCGBodyTransform",btform);
    PackManager.v().getPack("jtp").add(tform);
    
    String[] args = {
      "-pp",
      "-output-format", output_format,
      "-include-all",
    //  "-w"
    };

    soot.Main.main(args);
  }
  
  public static void main(String[] args) {
    BytecastCGSoot soot_example = new BytecastCGSoot();
    
    BytecastCGBodyTransform btform = new BytecastCGBodyTransform(); 
    
    soot_example.run("J",btform);
    System.out.println("done");
  }
}
