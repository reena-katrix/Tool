import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class mdfdataProcessing {
    
    //seriesNo,deviceClass
    public static final HashMap<String, DeviceClass> seriesNumberToDeviceMapping = new HashMap<String, DeviceClass>();
    
    
    public static TreeNode<DeviceClass>mdfdataXMLprocessing(File mdfdataFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Build Document
        Document document = builder.parse(mdfdataFile);
        
        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();
        //Here comes the root node
        Element IFMMDF = document.getDocumentElement();
//        NodeList deviceGroups = IFMMDF.getChildNodes();
        
        DeviceClass rootDevice = new DeviceClass();
        //make root of tree
        rootDevice.setDeviceName("IFMMDF");
        TreeNode<DeviceClass> root = new TreeNode<DeviceClass>(rootDevice);
        NodeList deviceGroups = document.getElementsByTagName("DEVICEGROUP");
        try {   
                //define a HTML String Builder
                StringBuilder htmlStringBuilder=new StringBuilder();
                //append html header and title
                htmlStringBuilder.append("<html><head><title>DEVICE PROFILE HIERARCHY</title></head>");
                //append body
                htmlStringBuilder.append("<body>");
                
                htmlStringBuilder.append("<p><b>Note:</b><ol></p>");
                htmlStringBuilder.append("<li>This report contains information of Device Profile Features for a particular DeviceGroup which may have a set of DeviceGroups or Devices.");
                htmlStringBuilder.append("<li>These Features are extracted from .orderedFeatures file inside DAR folders by mapping ProductSeries number.");
                htmlStringBuilder.append("<li>All Devices and DeviceGroups under parent DeviceGroup will have same set of features.");
                htmlStringBuilder.append("<li>SYSOID is unique for a device so features specific to a device are identified for particular SYSOID and listed under it.");
                htmlStringBuilder.append("</ol>");
                htmlStringBuilder.append("<ol>");
                for (int i = 0; i < deviceGroups.getLength(); i++) {
                    HashMap<String, ArrayList<String>>deviceGroupFeatures = new HashMap<String, ArrayList<String>>();
                    String mdfid = deviceGroups.item(i).getAttributes().getNamedItem("MDFID").getNodeValue() ;
                    
                    if(mdfid != null && ProcessingDAR.ListOfDeviceProfileToFeaturesMappings.containsKey(mdfid)) {
//                        System.out.println(mdfid);
                        GetParticularDEVICEGROUP(deviceGroups.item(i),0, ProcessingDAR.ListOfDeviceProfileToFeaturesMappings.get(mdfid) ,root,htmlStringBuilder);
//                        to close device lists
                        htmlStringBuilder.append("</ol>");
//                        System.out.println("---------------------------------");
                    }
                }
                htmlStringBuilder.append("</ol>");
                htmlStringBuilder.append("</body></html>");
                CreateHtmlDocument.WriteToFile(htmlStringBuilder.toString(),"completeOutput.html");

        
        } catch (IOException e) {
            e.printStackTrace();
           }
        return root;
    }
//    
private static TreeNode<DeviceClass> GetParticularDEVICEGROUP(Node deviceGroup,int lev, HashMap<String, ArrayList<String>>deviceGroupFeatures, TreeNode<DeviceClass>root ,StringBuilder htmlStringBuilder) {
    //make this node a treenode
//    System.out.println("IM INNNNNN");
    DeviceClass deviceObj = new DeviceClass();
    String seriesNo = "",deviceName = "";
    ArrayList<String>SYSOIDs = new ArrayList<String>();
    NodeList DeviceGroupChildren = deviceGroup.getChildNodes();
    
    if(deviceGroup.getNodeName().equals("DEVICE")){
        deviceObj.isDevice = true;
        for (int temp = 0; temp < DeviceGroupChildren.getLength(); temp++)
        {
           Node child = DeviceGroupChildren.item(temp);
           
           if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("SYSOID")) {
               SYSOIDs.add(child.getAttributes().getNamedItem("OID").getNodeValue());
           }
        }
    }
    else
        deviceObj.isDevice = false;
        
    if (deviceGroup.hasAttributes()) {
        // get attributes names and values
        NamedNodeMap nodeMap = deviceGroup.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++)
        {
            Node tempNode = nodeMap.item(i);
            if(tempNode.getNodeName().equals("MDFID")) { 
                seriesNo = tempNode.getNodeValue();   
            }
            if(tempNode.getNodeName().equals("NAME")) {
                deviceName = tempNode.getNodeValue();
            }            
        }  
        
    }
    
      
      deviceObj.setDeviceName(deviceName);
      deviceObj.setSeriesNo(seriesNo);
      deviceObj.setDeviceProfileToFeaturesListMapping(deviceGroupFeatures);
      
      if(SYSOIDs.size() > 0 ) {
          for(int i = 0 ; i < SYSOIDs.size(); i++) {
              String sysoid = SYSOIDs.get(i);
              HashMap<String, ArrayList<String>>map = new HashMap<String, ArrayList<String>>();
              HashMap<String, HashMap<String, ArrayList<String>>>sysoMap = new HashMap<String, HashMap<String, ArrayList<String>>>();
              if(ProcessingDAR.SYSOIDtoFeaturesMapping.containsKey(sysoid)) {
                  map = ProcessingDAR.SYSOIDtoFeaturesMapping.get(sysoid);
                  sysoMap.put(sysoid, map);
                  deviceObj.setDeviceFeatures(sysoMap);
              }
              else {
                  HashMap<String, ArrayList<String>>empty = new HashMap<String, ArrayList<String>>();
                  sysoMap.put(sysoid, empty);
                  deviceObj.setDeviceFeatures(sysoMap);
              }
          }
      }
      

      //for device group 
      if(lev == 0)
          htmlStringBuilder.append("<p><li><span><h2>DEVICE PROFILE FEATURES LIST FOR DEVICEGROUP</h2>{DeviceGroupName:"+deviceName+", MDFID: "+ seriesNo+"}</h2></span></li></p>");
      deviceObj.print(lev,htmlStringBuilder);
//      htmlStringBuilder.append("</ol>");
      lev++;
      TreeNode<DeviceClass> deviceObjToTreeNode= root.addChild(deviceObj);
      //add device
      seriesNumberToDeviceMapping.put(seriesNo,deviceObj );
      
    for (int temp = 0; temp < DeviceGroupChildren.getLength(); temp++) {
       Node child = DeviceGroupChildren.item(temp);
       
       if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("SYSOID"))
       {
//           System.out.println("IM IN SYSOID");
       }
       else if(child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("SYSOID") == false)
           GetParticularDEVICEGROUP(child, lev,deviceGroupFeatures,deviceObjToTreeNode,htmlStringBuilder);
       
    }
    return root;
    }


}

