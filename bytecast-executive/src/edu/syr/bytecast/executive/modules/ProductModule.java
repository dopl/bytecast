/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.bytecast.executive.modules;

import com.google.inject.AbstractModule;
import edu.syr.bytecast.amd64.BytecastAMD64Factory;
import edu.syr.bytecast.amd64.BytecastAmd64;
import edu.syr.bytecast.amd64.api.constants.IBytecastAMD64;
import edu.syr.bytecast.fsys.elf.ElfExeObjParser;
import edu.syr.bytecast.interfaces.fsys.IBytecastFsys;

/**
 *
 * @author dhrumin
 */
public class ProductModule extends AbstractModule
{

    @Override
    protected void configure() {
        bind(IBytecastFsys.class).to(ElfExeObjParser.class);
        //bind(IBytecastAMD64.class).toProvider()       
    }
    
}
