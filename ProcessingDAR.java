import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class ProcessingDAR {
    
    
    //series,fetureList
//    public static final HashMap<ArrayList<String>, ArrayList<String> > featureMapping = new HashMap<>(); 
//    public static final HashMap<String, ArrayList<String> > featureMapping = new HashMap<>(); 
    public static final HashMap<String, HashMap<String, ArrayList<String>>> SYSOIDtoFeaturesMapping = new HashMap<>();
    public static final HashMap<String, HashMap<String, ArrayList<String>>>ListOfDeviceProfileToFeaturesMappings = new HashMap<String, HashMap<String, ArrayList<String>>>(); 
    
    public static void XMLprocessing(File DARxmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(DARxmlFile);
        
        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();
        
        //Here comes the root node
        Element root = document.getDocumentElement();
//        System.out.println(root.getNodeName());

        NodeList productSeries = document.getElementsByTagName("param:productSeries");
        NodeList deviceGroupfeatures = document.getElementsByTagName("artifactId");
        NodeList deviceFeatures = document.getElementsByTagName("param:sysObjectId");
//        System.out.println(dependency.getLength()+"============================");
        if((productSeries.getLength() > 0  && deviceGroupfeatures.getLength() > 0) || (deviceFeatures.getLength() > 0  && deviceGroupfeatures.getLength() > 0)) {
//            System.out.println("Features of: " +DARxmlFile.getName()+ "STARTED------------------------------");
            traverseOrderedFeaturesFile(DARxmlFile, productSeries, deviceGroupfeatures, deviceFeatures);
//            System.out.println("Features of: " +DARxmlFile.getName()+ " ENDED--------------------------------");
        }
    }
    
    private static void traverseOrderedFeaturesFile(File DARxmlFile,NodeList productSeries, NodeList features, NodeList SYSOIDtoDeviceFeatures) {
        
//        System.out.println("IM INSIDE TRAVERSE FEATURES " + ListOfDeviceProfileToFeaturesMappings.size() + " "+ featureMapping.size()
//        );
        DeviceClass device = new DeviceClass();
        
        ArrayList<String>seriesList = new ArrayList<String>();
 
//        System.out.println("size:" +productSeries.getLength());
            for(int j = 0; j < productSeries.getLength(); j++) {
//                seriesList.add(series.item(j).getFirstChild().getTextContent());
                seriesList.add(productSeries.item(j).getFirstChild().getTextContent().toString());
//                System.out.println("series val:"+productSeries.item(j).getFirstChild().getTextContent().toString());
            }
//        }
        ArrayList<String>featuresList = new ArrayList<String>();
            for(int j = 0; j < features.getLength(); j++) {
//                System.out.println(features.item(j).getFirstChild().getTextContent());
                featuresList.add(features.item(j).getFirstChild().getTextContent().toString());
//                System.out.println(features.item(j).getFirstChild().getTextContent().toString());
            }
//            System.out.println("------------------------------------------------");

       
        HashMap<String, ArrayList<String>> filenameToFeatures = new HashMap<String, ArrayList<String>>();
        
        
        if(featuresList.size() > 0) {
            filenameToFeatures.put(DARxmlFile.getName() , featuresList);
            
            for(int i = 0 ; i < SYSOIDtoDeviceFeatures.getLength() ; i++) {
                String sysoid = SYSOIDtoDeviceFeatures.item(i).getFirstChild().getTextContent().toString();
                //check for valid SYSOID
                if(sysoid.contains("1.3.6.1.4.1.9.1")) {
                    if(SYSOIDtoFeaturesMapping.containsKey(sysoid)) {
                        HashMap<String, ArrayList<String>>checkSYSOID;
                        checkSYSOID = SYSOIDtoFeaturesMapping.get(sysoid);
                        checkSYSOID.put(DARxmlFile.getName() , featuresList);
                    }
                    else
                        SYSOIDtoFeaturesMapping.put(sysoid,filenameToFeatures);
//                    System.out.println("FOUND FOR:" + sysoid);
                }
            }
//            deviceProfileVSfeatures.add(filenameToFeatures);
        }
        
//        System.out.println("para1:" + deviceProfileVSfeatures.size());
        
//        ArrayList<HashMap<String, ArrayList<String>>>list;
        
        for(int i = 0 ; i < seriesList.size(); i ++) {
            
            String  series = seriesList.get(i);
            if(series.length() == 9) {
              //list of deviceprofile to features mapping
                ArrayList<String>list = new ArrayList<String>();
//                    System.out.println("IM INSIDE IF");
                    if(ListOfDeviceProfileToFeaturesMappings.size() == 0) {
                        ListOfDeviceProfileToFeaturesMappings.put(series,filenameToFeatures);
                    }
                    else if(ListOfDeviceProfileToFeaturesMappings.containsKey(series)){
                        HashMap<String, ArrayList<String>>checkSeries;
                        checkSeries = ListOfDeviceProfileToFeaturesMappings.get(series);
                        checkSeries.put(DARxmlFile.getName() , featuresList);
                            
                    }
                    else if(!ListOfDeviceProfileToFeaturesMappings.containsKey(series)) {
                        HashMap<String, ArrayList<String>>newDARfile = new HashMap<String, ArrayList<String>>();
                        newDARfile.put(DARxmlFile.getName() , featuresList);
                        ListOfDeviceProfileToFeaturesMappings.put(series, newDARfile);
                    }
            }
        }
    }
    

    public static void TraverseDARfiles(File DARfile) throws ParserConfigurationException, SAXException, IOException{
//        System.out.println(DARfile.getName());
            
            if(DARfile.isDirectory() ) { 
                String[] subNote = DARfile.list();
                for(String filename : subNote){
                    TraverseDARfiles(new File(DARfile, filename));
                }
            }
            
//            else {
                if(DARfile.getName().endsWith(".xml") && !DARfile.getName().contains(".orderedFeatures") && DARfile.length() > 0) {
                    
                    XMLprocessing(DARfile);
                }
     }
}

