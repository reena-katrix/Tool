import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchDeviceOrDeviceGroup {
    
      private static String createIndent(int depth) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < depth; i++) {
          sb.append(' ');
      }
      return sb.toString();
    }
    public static void Search(TreeNode<DeviceClass>treeRoot, String userInput) {
        try {   
            //define a HTML String Builder
            StringBuilder htmlOutputStringBuilder=new StringBuilder();
            //append html header and title
            htmlOutputStringBuilder.append("<html><head><title>DEVICE PROFILE HIERARCHY</title></head>");
            //append body
            htmlOutputStringBuilder.append("<body>");
            
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

//            while(userInput != "1") {
                
                int flag = 0 ; 
                for (TreeNode<DeviceClass> element : treeRoot) {
                    
                    List<TreeNode<DeviceClass>> getIndexList = element.getElementsIndex();
                    for(int i = 0 ; i < getIndexList.size() ; i++) {
                        
                        TreeNode<DeviceClass> currentNode = getIndexList.get(i);
                        DeviceClass currentDeviceClass = currentNode.data;
                        HashMap<String, HashMap<String, ArrayList<String>>>SYSOIDlist = currentDeviceClass.getDeviceFeatures();
                      
                        
                        JSONObject obj = new JSONObject();
                        if(currentDeviceClass.getDevicename().equals(userInput)) {
                            if(currentDeviceClass.isDevice == false) {
//                                System.out.println("YOU ENTERED DEVICEGROUP NAMED:" + userInput);
//                                htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICEGROUP NAMED:" + userInput +"</p>");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                htmlOutputStringBuilder.append("<ol>");
                                
                                for (TreeNode<DeviceClass> node : currentNode) {
                                    String indent = createIndent(node.getLevel());
                                    
                                    
                                    if(!node.data.isDevice) {
                                        node.data.printDeviceGroup(htmlOutputStringBuilder);
                                        
                                        gson = new GsonBuilder()
                                                .registerTypeAdapter(DeviceClass.class, new Devicegroup())
                                                .create();
                                        String result = gson.toJson(node.data);
                                        
                                        System.out.println(result);
                                    }
                                    else {
                                        node.data.printDevice(htmlOutputStringBuilder);
                                        gson = new GsonBuilder()
                                                .registerTypeAdapter(DeviceClass.class, new Device())
                                                .create();
                                        String result = gson.toJson(node.data);
                                        System.out.println(result);
                                    }
                                    
                                    
                                }
                                htmlOutputStringBuilder.append("</ol>");
                            }
                            else {
//                                System.out.println("YOU ENTERED DEVICE NAMED:" + userInput);
//                                htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICE:" + userInput +"</p>");
                                TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
                                
//                                System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder);
                                gson = new GsonBuilder()
                                        .registerTypeAdapter(DeviceClass.class, new Devicegroup())
                                        .create();
                                String result = gson.toJson(deviceParentNode.data);
                                System.out.println(result);
                                
//                                System.out.println("******************DEVICE DETAILS******************");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                currentNode.data.printDevice(htmlOutputStringBuilder);
                                gson = new GsonBuilder()
                                        .registerTypeAdapter(DeviceClass.class, new Device())
                                        .create();
                                result = gson.toJson(currentNode.data);
                                System.out.println(result);
                            }
                          
                            flag = 1;
                            break;
                        }
                        else if(currentDeviceClass.getSeriesNo().equals(userInput)) {
                            if(currentDeviceClass.isDevice == false) {
//                                System.out.println("YOU ENTERED DEVICEGROUP PRODUCT SERIES NUMBER:" + userInput);
//                                htmlOutputStringBuilder.append("<p>SEARCHING FOR DEVICEGROUP WITH PRODUCT SERIES NUMBER:" + userInput +"</p>");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED DEVICEGROUP DETAILS</b></p>");
                                for (TreeNode<DeviceClass> node : currentNode) {
                                    String indent = createIndent(node.getLevel());
//                                    if(node.data.isDevice)
//                                        node.data.printDevice(htmlOutputStringBuilder , mainObj);
//                                    else
//                                        node.data.printDeviceGroup(htmlOutputStringBuilder, mainObj);
                                    if(!node.data.isDevice) {
                                        node.data.printDeviceGroup(htmlOutputStringBuilder);
                                        
                                        gson = new GsonBuilder()
                                                .registerTypeAdapter(DeviceClass.class, new Devicegroup())
                                                .create();
                                        String result = gson.toJson(node.data);
                                        System.out.println(result);
                                    }
                                    else {
                                        node.data.printDevice(htmlOutputStringBuilder);
                                        gson = new GsonBuilder()
                                                .registerTypeAdapter(DeviceClass.class, new Device())
                                                .create();
                                        String result = gson.toJson(node.data);
                                        System.out.println(result);
                                    }
                                }
                            }
                            else {
//                                System.out.println("YOU ENTERED DEVICE MDFID:" + userInput);
//                                htmlOutputStringBuilder.append("<p>SEARCHING FOR DEVICE WITH MDFID:" + userInput +"</p>");
                                TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
                                
//                                System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARENT DEVICEGROUP DETAILS</b></p>");
                                deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder);
                                gson = new GsonBuilder()
                                        .registerTypeAdapter(DeviceClass.class, new Devicegroup())
                                        .create();
                                String result = gson.toJson(deviceParentNode.data);
                                System.out.println(result);
//                                System.out.println("******************DEVICE DETAILS******************");
//                                htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                currentNode.data.printDevice(htmlOutputStringBuilder);
                                gson = new GsonBuilder()
                                        .registerTypeAdapter(DeviceClass.class, new Device())
                                        .create();
                                result = gson.toJson(currentNode.data);
                                System.out.println(result);
                            }
                          
                            flag = 1;
                            break;
                        }
                        else if(!SYSOIDlist.isEmpty()){
                            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : SYSOIDlist.entrySet()) {
                                if(entry.getKey().equals(userInput)) {
//                                    System.out.println("YOU ENTERED DEVICE SYSOID:"+ entry.getKey());
//                                    htmlOutputStringBuilder.append("<p>SEARCHING OUTPUT FOR DEVICE WITH MDFID:" + userInput +"</p>");
                                    
                                    TreeNode<DeviceClass> deviceParentNode = currentNode.parent;
//                                    System.out.println("******************PARENT DEVICEGROUP DETAILS******************");
//                                    htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARENT DEVICEGROUP DEATILS</b></p>");
                                    deviceParentNode.data.printDeviceGroup(htmlOutputStringBuilder);
                                    gson = new GsonBuilder()
                                            .registerTypeAdapter(DeviceClass.class, new Devicegroup())
                                            .create();
                                    String result = gson.toJson(deviceParentNode.data);
                                    System.out.println(result);
                                    
//                                    System.out.println("******************DEVICE DETAILS******************");
//                                    htmlOutputStringBuilder.append("<p><b>IDENTIFIED PARTICULAR DEVICE DETAILS</b></p>");
                                    currentNode.data.printDevice(htmlOutputStringBuilder);
                                    gson = new GsonBuilder()
                                            .registerTypeAdapter(DeviceClass.class, new Device())
                                            .create();
                                    result = gson.toJson(currentNode.data);
                                    System.out.println(result);
                                    
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
                if(flag == 0 ) //{
                    
                    System.out.println("NO ENTRY FOUND");
//                    break;
//                }
//                System.out.println("\n");
//            }
            htmlOutputStringBuilder.append("</body></html>");
            CreateHtmlDocument.WriteToFile(htmlOutputStringBuilder.toString(),"queryOutput.html");

  
            } catch (IOException e) {
            e.printStackTrace();
            }
        }

}
