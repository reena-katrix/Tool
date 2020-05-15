import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import com.google.gson.GsonBuilder;

public class Main {
    
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		  UnzipDARs.displayIt(new File("/Users/reenaya/Desktop/dar"), "/Users/reenaya/Desktop/FeaturesFolder");
		  ProcessingDAR.TraverseDARfiles(new File("/Users/reenaya/Desktop/FeaturesFolder"));
		  TreeNode<DeviceClass> treeRoot = mdfdataProcessing.mdfdataXMLprocessing(new File("/Users/reenaya/Desktop/mdfdata.xml"));
		  
		  //Ready for queries
		  Scanner input = new Scanner(System.in);
          String userInput = "";
          System.out.println("PLEASE ENTER YOUR QUERY INPUT");
          userInput = input.nextLine();
		  SearchDeviceOrDeviceGroup.Search(treeRoot, userInput);
	}
}

