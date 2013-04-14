/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.bytecast.executive.version;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.syr.bytecast.amd64.api.constants.IBytecastAMD64;
import edu.syr.bytecast.executive.modules.ProductModule;
import edu.syr.bytecast.executive.modules.TestModule;
import edu.syr.bytecast.interfaces.fsys.IBytecastFsys;

/**
 *
 * @author dhrumin
 */
public class BytecastVersion {
    
    private static Injector injectTest = Guice.createInjector(new TestModule());
    private static Injector injectProduct = Guice.createInjector(new ProductModule());
    
      
    public static IBytecastFsys getFsys(String version)
    {
        if(version.equalsIgnoreCase("test"))
            return injectTest.getInstance(IBytecastFsys.class);
        
        if(version.equalsIgnoreCase("product"))
            return injectProduct.getInstance(IBytecastFsys.class);
        
        return null;
    }
    
    public IBytecastAMD64 getamd64(String version)
    {
        if(version.equalsIgnoreCase("test"))
            return injectTest.getInstance(IBytecastAMD64.class);
        
        if(version.equalsIgnoreCase("product"))
            return injectProduct.getInstance(IBytecastAMD64.class);
        
        return null;
    }       
    
}
