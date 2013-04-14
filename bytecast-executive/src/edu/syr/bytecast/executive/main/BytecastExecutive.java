/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.bytecast.executive.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.syr.bytecast.amd64.BytecastAMD64Factory;
import edu.syr.bytecast.amd64.api.constants.IBytecastAMD64;
import edu.syr.bytecast.amd64.api.output.IExecutableFile;
import edu.syr.bytecast.executive.modules.ProductModule;
import edu.syr.bytecast.executive.modules.TestModule;
import edu.syr.bytecast.executive.version.BytecastVersion;
import edu.syr.bytecast.interfaces.fsys.IBytecastFsys;
import edu.syr.bytecast.jimple.api.IJimple;
import edu.syr.bytecast.jimple.impl.Jimple;
import edu.syr.bytecast.util.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhrumin
 */
public class BytecastExecutive {
    
    public BytecastExecutive()
    {
        
    }        
    
    //inputProperty = Property in Paths.  
    //outputFile = path and name of jar.    
    public boolean start(String inputProperty, String outputFile)
    {     
        try {            
            Paths.v().setRoot("../");
            Paths.v().parsePathsFile("bytecast-executive/config/paths.cfg");
            IBytecastFsys fsys = BytecastVersion.getFsys("product");
            //Have to Inject Product and Test Module here.
            IBytecastAMD64 amd64 = new BytecastAMD64Factory().createBytecastAMD64Builder(fsys, Paths.v().getPath(inputProperty));
            IJimple jimple = new Jimple();
            //Should create the jar path here.
            jimple.createJimple(amd64, outputFile);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(BytecastExecutive.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static void main(String[] args) {
        
    }        
}