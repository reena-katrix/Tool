import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Map;


public class Main {
    
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		  UnzipDARs.displayIt(new File("/Users/reenaya/Downloads/dar"), "/Users/reenaya/Documents/FeaturesFolder");
		  ProcessingDAR.TraverseDARfiles(new File("/Users/reenaya/Documents/FeaturesFolder"));
		  TreeNode<DeviceClass> treeRoot = mdfdataProcessing.mdfdataXMLprocessing(new File("/Users/reenaya/Downloads/mdfdata1.xml"));
		  
		  //Ready for queries
		  SearchDeviceOrDeviceGroup(treeRoot);
 
	}
	
	private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }
	
	
	private static void SearchDeviceOrDeviceGroup(TreeNode<DeviceClass>treeRoot) {
	    try {   
            //define a HTML String Builder
            StringBuilder htmlOutputStringBuilder=new StringBuilder();
            //append html header and title
            htmlOutputStringBuilder.append("<html><head><title>DEVICE PROFILE HIERARCHY</title></head>");
            //append body
            htmlOutputStringBuilder.append("<body>");
            htmlOutputStringBuilder.append("<p><h3>OUTPUT FILE</h3></p>");
            Scanner input = new Scanner(System.in);
            String userInput = "";
            
            //to write json output
            JSONObject mainObj = new JSONObject();
            while(userInput != "1") {
                System.out.println("PLEASE ENTER YOUR QUERY INPUT");
                userInput = input.nextLine();
                int flag = 0 ; 
                for (TreeNode<DeviceClass> element : treeRoot) {
                    
                    List<TreeNode<DeviceClass>> getIndexList = element.getElementsIndex();
                    for(int i = 0 ; i < getIndexList.size() ; i++) {
                        
                        TreeNode<DeviceClass> currentNode = getIndexList.get(i);
                        DeviceClass currentDeviceClass = currentNode.data;
//                        Gson gson = new Gson();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        
                        
                        HashMap<String, HashMap<String, ArrayList<String>>>SYSOIDlist = currentDeviceClass.getDeviceFeatures();
	                  
                        
                        
                        if(currentDeviceClass.getDevicename().equals(userInput)) {
                            if(currentDeviceClass.isDevice == false) {
                                System.out.println("YOU ENTERED DEVICEGROUP NAMED:" + userInput);
                                htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICEGROUP NAMED:" + userInput +"</p>");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                htmlOutputStringBuilder.append("<ol>");
                                JSONObject json = new JSONObject();
                                JSONArray devices = new JSONArray();
                                for (TreeNode<DeviceClass> node : currentNode) {
                                    String indent = createIndent(node.getLevel());
                                    
                                    
                                    if(!node.data.isDevice) {
                                        node.data.printDeviceGroup(htmlOutputStringBuilder , json);
                                        String jsonObj = gson.toJson(node);
                                        System.out.println(jsonObj);
                                    }
                                    else {
                                        node.data.printDevice(htmlOutputStringBuilder, json);
                                        String jsonObj = gson.toJson(node);
                                        System.out.println(jsonObj);
                                    }
                                    
                                    
                                }
                                mainObj.put("devicegroup", json);
                                htmlOutputStringBuilder.append("</ol>");
                            }
                            else {
                                System.out.println("YOU ENTERED DEVICE NAMED:" + userInput);
                                htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICE:" + userInput +"</p>");
                                TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
                                
//                                System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder, mainObj);
                                
//                                System.out.println("******************DEVICE DETAILS******************");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                currentNode.data.printDevice(htmlOutputStringBuilder, mainObj);
                            }
	                      
                            flag = 1;
                            break;
                        }
                        else if(currentDeviceClass.getSeriesNo().equals(userInput)) {
                            if(currentDeviceClass.isDevice == false) {
                                System.out.println("YOU ENTERED DEVICEGROUP PRODUCT SERIES NUMBER:" + userInput);
                                htmlOutputStringBuilder.append("<p>SEARCHING FOR DEVICEGROUP WITH PRODUCT SERIES NUMBER:" + userInput +"</p>");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                for (TreeNode<DeviceClass> node : currentNode) {
                                    String indent = createIndent(node.getLevel());
                                    if(node.data.isDevice)
                                        node.data.printDevice(htmlOutputStringBuilder , mainObj);
                                    else
                                        node.data.printDeviceGroup(htmlOutputStringBuilder, mainObj);
                                }
                            }
                            else {
                                System.out.println("YOU ENTERED DEVICE MDFID:" + userInput);
                                htmlOutputStringBuilder.append("<p>SEARCHING FOR DEVICE WITH MDFID:" + userInput +"</p>");
                                TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
                                
//                                System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARENT DEVICEGROUP DETAILS</b></p>");
                                deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder , mainObj);
                                
//                                System.out.println("******************DEVICE DETAILS******************");
                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                currentNode.data.printDevice(htmlOutputStringBuilder , mainObj);
                            }
	                      
                            flag = 1;
                            break;
                        }
                        else if(!SYSOIDlist.isEmpty()){
                            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : SYSOIDlist.entrySet()) {
                                if(entry.getKey().equals(userInput)) {
                                    System.out.println("YOU ENTERED DEVICE SYSOID:"+ entry.getKey());
                                    htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICE WITH MDFID:" + userInput +"</p>");
                                    
                                    TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
                                    System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
                                    htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARENT DEVICEGROUP DEATILS</b></p>");
                                    deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder , mainObj);
                                    
                                    System.out.println("******************DEVICE DETAILS******************");
                                    htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                    currentNode.data.printDevice(htmlOutputStringBuilder , mainObj);
	                              
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag == 1)
                                break;
                        }
	                 
                    }
                    if(flag == 1)
                        break;
                }
                JsonWriter.func(mainObj);
                if(flag == 0 ) {
                    
                    System.out.println("NO ENTRY FOUND");
                    break;
                }
                System.out.println("\n");
            }
            htmlOutputStringBuilder.append("</body></html>");
            CreateHtmlDocument.WriteToFile(htmlOutputStringBuilder.toString(),"particularOutput.html");

  
	        } catch (IOException e) {
	        e.printStackTrace();
	        }
	    }
    }

