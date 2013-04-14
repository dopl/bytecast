/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.bytecast.executive.modules;

import com.google.inject.AbstractModule;
import edu.syr.bytecast.amd64.BytecastAmd64;
import edu.syr.bytecast.amd64.api.constants.IBytecastAMD64;
import edu.syr.bytecast.amd64.test.TestBytecastAmd64;
import edu.syr.bytecast.fsys.elf.ElfExeObjParser;
import edu.syr.bytecast.interfaces.fsys.IBytecastFsys;
import edu.syr.bytecast.test.mockups.MockBytecastFsys;

/**
 *
 * @author dhrumin
 */
public class TestModule extends AbstractModule
{

    @Override
    protected void configure() {
        //String input = "";
        bind(IBytecastFsys.class).to(MockBytecastFsys.class);   
        //bind(IBytecastAMD64.class).to()
        //bind(String.class).toInstance(input);   
    }
    
}
