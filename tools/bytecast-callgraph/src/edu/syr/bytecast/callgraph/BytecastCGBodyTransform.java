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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
/**
 *
 * @author adodds
 */
public class BytecastCGBodyTransform extends BodyTransformer {
  
  private Map<String,Set<String>> m_map;
  
  public BytecastCGBodyTransform(){
      m_map = new HashMap<String,Set<String>>();
  }
  
  public Map<String,Set<String>> getMap(){
      return m_map;
  }
  
  @Override
  protected void internalTransform(Body b, String string, Map map) {
      SootMethod src_method = b.getMethod();
      PatchingChain<Unit> units = b.getUnits();
      Iterator<Unit> iter = units.iterator();
      String src_sig = src_method.getSignature();
      while(iter.hasNext())
      {
          Unit next = iter.next();
          List<ValueBox> boxes = next.getUseAndDefBoxes();
          for(ValueBox box : boxes)
          {
              Value value = box.getValue();
              if(value instanceof InvokeExpr)
              {
                  InvokeExpr expr = (InvokeExpr) value;
                  SootMethod dst_method = expr.getMethod();
                  String dst_sig = dst_method.getSignature();
                  if(m_map.containsKey(src_sig))
                  {
                     Set<String> dests = m_map.get(src_sig);
                     dests.add(dst_sig);
                  }
                  else
                  {
                      Set<String> dests = new HashSet<String>();
                      dests.add(dst_sig);
                      m_map.put(src_sig,dests);
                  }                      
              }
          }
      }
    }
  }

