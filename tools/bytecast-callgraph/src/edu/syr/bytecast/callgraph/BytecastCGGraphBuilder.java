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

import edu.syr.bytecast.util.WriteStringAsFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import prefuse.data.*;
import prefuse.data.io.*;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.render.*;
import prefuse.util.*;
import prefuse.action.assignment.*;
import prefuse.Constants;
import prefuse.visual.*;
import prefuse.action.*;
import prefuse.activity.*;
import prefuse.action.layout.graph.*;
import prefuse.controls.*;
/**
 *
 * @author adodds
 */
public class BytecastCGGraphBuilder { 
    private String createXmlHeader(String in)
    {
        in+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        in+="<!--  An excerpt of an egocentric Bytecast Call Graph  -->\n";
        in+="<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n";
        in+="<graph edgedefault=\"directed\">\n\n";
        in+="<!-- data schema -->\n";
        in+="<key id=\"name\" for=\"node\" attr.name=\"name\" attr.type=\"string\"/>\n";
        in+="<!-- nodes -->\n";
        return in;
    }
    
    private String createNodeTag(String in, String name, Integer id)
    {
        in += "<node id=\"";
        in += id;
        in += "\">\n<data key=\"name\">";
        in += name;
        in += "</data>\n";
        in += "</node>\n";
        return in;
    }
    
    private String createEdgeTag(String in, Integer src, Integer dest )
    {
        in+="<edge source=\"";
        in+= src;
        in+="\" target=\"";
        in+= dest;
        in+="\"></edge>\n"; 
        return in;
    }
    
    private String createCloseXmlTag(String in)
    {
        in += "   </graph>\n";
        in += "</graphml>";
        return in;
    }
    private Map<String,Integer> numberNodes(Map<String,Set<String>> map)
    {
        Map<String,Integer> ids = new HashMap<String,Integer>();
        Set<String> visited = new HashSet<String>();
        int number = 1;
        
        for(String i : map.keySet())
        {
            if(visited.contains(i) == false)
            {
               visited.add(i);
               ids.put(i,number);
               ++number;
            }
            
            for(String k : map.get(i))
            {
                if(visited.contains(k))
                {
                    continue;
                }
                else
                {
                    visited.add(k);
                    ids.put(k,number);
                    ++number;
                }
            }
        }
        return ids;
    }
    public String getClassFuncName(String in)
    {
        MethodSignatureUtil nodeutil = new MethodSignatureUtil(in);
        String nodeName = new String();
        nodeName += nodeutil.getClassName();
        nodeName += ".";
        nodeName += nodeutil.getMethodName();
        return nodeName;
    }
    
    public void writeGraph(Map<String,Set<String>> map)throws IOException {
        //build up graph. 
        String xmldata = new String();
        
        xmldata = createXmlHeader(xmldata);

        String xmlnodes = new String();
        String xmledges = new String();
        
        Map<String,Integer> ids = numberNodes(map);   
        Set<String> createdNodes = new HashSet<String>();
        //create all nodes for xml file.
        for(String i : map.keySet()) {
            
            xmlnodes = createNodeTag(xmlnodes,getClassFuncName(i),ids.get(i));
            createdNodes.add(i);
            for(String k : map.get(i))
            {
                if(createdNodes.contains(k) == false)
                {
                    createdNodes.add(k);
                    xmlnodes = createNodeTag(xmlnodes,getClassFuncName(k),ids.get(k));
                }
                xmledges = createEdgeTag(xmledges,ids.get(i),ids.get(k));    
            }
        }
              
        xmldata += xmlnodes;
        xmldata += xmledges;
        xmldata = createCloseXmlTag(xmldata);
        WriteStringAsFile fileWriter = new WriteStringAsFile();
        fileWriter.write("xmldata.xml", xmldata);
    }
    
    public void drawGraph() {

        System.out.println("Reading graph");

        Graph graph = null;
        /* graph will contain the core data */
        try {
            graph = new GraphMLReader().readGraph("xmldata.xml");
        /* load the data from an XML file */
        } catch (DataIOException e) {
            e.printStackTrace();
            System.err.println("Error loading graph. Exiting...");
            System.exit(1);
        }

        System.out.println("Done reading graph");

        // 2. prepare the visualization

        Visualization vis = new Visualization();
        /* vis is the main object that will run the visualization */
        vis.add("Bytecast", graph);
        /* add our data to the visualization */

        // 3. setup the renderers and the render factory

        // labels for name
        LabelRenderer nameLabel = new LabelRenderer("name");
        nameLabel.setRoundedCorner(8, 8);
        nameLabel.setHorizontalAlignment(4);
        nameLabel.setVerticalAlignment(4);
        /* nameLabel decribes how to draw the data elements labeled as "name" */

        // create the render factory
        vis.setRendererFactory(new DefaultRendererFactory(nameLabel));

        // 4. process the actions

        // colour palette for nominal data type
        int[] palette = new int[]{ColorLib.rgb(255, 180, 180), ColorLib.rgb(190, 190, 255)};
        /* ColorLib.rgb converts the colour values to integers */


        // map data to colours in the palette
        //ColorAction fill = new ColorAction("socialnet.nodes", VisualItem.FILLCOLOR, ColorLib.rgb(120, 0, 0));
        /* fill describes what colour to draw the graph based on a portion of the data */

        // node text
        ColorAction text = new ColorAction("Bytecast.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
        /* text describes what colour to draw the text */

        // edge
        ColorAction edges = new ColorAction("Bytecast.edges", VisualItem.STROKECOLOR, ColorLib.gray(200));
        /* edge describes what colour to draw the edges */

        // combine the colour assignments into an action list
        ActionList colour = new ActionList();
        //colour.add(fill);
        colour.add(text);
        colour.add(edges);
        vis.putAction("colour", colour);
        /* add the colour actions to the visualization */

        // create a separate action list for the layout
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(new ForceDirectedLayout("Bytecast"));
        /* use a force-directed graph layout with default parameters */

        layout.add(new RepaintAction());
        /* repaint after each movement of the graph nodes */

        vis.putAction("layout", layout);
        /* add the laout actions to the visualization */

        // 5. add interactive controls for visualization

        Display display = new Display(vis);
        display.setSize(700, 700);
        display.pan(350, 350);	// pan to the middle
        display.addControlListener(new DragControl());
        /* allow items to be dragged around */

        display.addControlListener(new PanControl());
        /* allow the display to be panned (moved left/right, up/down) (left-drag)*/

        display.addControlListener(new ZoomControl());
        /* allow the display to be zoomed (right-drag) */

        // 6. launch the visualizer in a JFrame

        JFrame frame = new JFrame("Bytecast");
        /* frame is the main window */

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(display);
        /* add the display (which holds the visualization) to the window */

        frame.pack();
        frame.setVisible(true);

        /* start the visualization working */
        vis.run("colour");
        vis.run("layout");

    }
 
    public static void main(String[] args) throws Exception{
        ByecastCGImportFiles build = new ByecastCGImportFiles();
        BytecastCGGraphBuilder test = new BytecastCGGraphBuilder();
        BytecastCGSoot cgSoot = new BytecastCGSoot();
        BytecastCGBodyTransform bform = new BytecastCGBodyTransform();
        
        try{
          build.initEnviroment();  
        }catch(IOException e) {
           System.err.println("Error Initializing build enviroment. Exiting...");     
        }
        
        
        cgSoot.run("J",bform);
        
        try{
            test.writeGraph(bform.getMap());
            test.drawGraph();
        }catch (IOException e){
            System.err.println("Error Writiing graph. Exiting...");
        }
    }

}
