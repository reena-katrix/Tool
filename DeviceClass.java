import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DeviceClass {
    
    private String seriesNo;
    private String deviceName;
    public boolean isDevice;
    //fileName->features for deviceGroups
    HashMap<String, ArrayList<String>>DeviceProfileToFeatures;
  //fileName->features for device
    HashMap<String, HashMap<String, ArrayList<String>>>featuresForDevice;    
    public DeviceClass() {
        this.seriesNo = "";
        this.deviceName = "";
        this.isDevice = false;
        this.DeviceProfileToFeatures = new HashMap<String, ArrayList<String>>();
        this.featuresForDevice  = new HashMap<String, HashMap<String, ArrayList<String>>>();
    }
    
    public DeviceClass(String series, String deviceName, HashMap<String, ArrayList<String>>featuresMapping,HashMap<String, HashMap<String, ArrayList<String>>>deviceFeatures, boolean ISaDevice) {
        this.seriesNo = series;
        this.deviceName = deviceName;
        this.DeviceProfileToFeatures = featuresMapping;
        this.featuresForDevice = deviceFeatures;
        this.isDevice = ISaDevice;
    }
    
    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String series) {
        this.seriesNo = series;
    }
    public String getDevicename() {
        return deviceName;
    }

    public void setDeviceName(String name) {
        this.deviceName = name;
    }
    
    public void setDeviceProfileToFeaturesListMapping(HashMap<String, ArrayList<String>>featureList) {
        this.DeviceProfileToFeatures = featureList;
    }
    
    public HashMap<String, HashMap<String, ArrayList<String>>>getDeviceFeatures(){return featuresForDevice;}
    
    
    public void setDeviceFeatures(HashMap<String, HashMap<String, ArrayList<String>>>deviceFeatureList) {
        this.featuresForDevice = deviceFeatureList;
    }
    
    public HashMap<String, ArrayList<String>>getDeviceProfileToFeaturesListMapping(){return DeviceProfileToFeatures;}
    
    
    public void print(int level, StringBuilder htmlBuilder) {
//        htmlBuilder.append("<p><span><h2>DEVICE PROFILE FEATURES LIST FOR DEVICEGROUP</h2>{NAME:"+ this.deviceName+", productSeries: "+ this.seriesNo+"}</span></p>");
//        System.out.println("Name :" + this.deviceName);
//        htmlBuilder.append("<p>Name:  "+ this.deviceName+"</p>");
//        System.out.println("Series Number :" + this.seriesNo);
//        htmlBuilder.append("<p>ProductSeries:  "+ this.seriesNo+"</p>");
        
        if(level > 0) {
            htmlBuilder.append("<p><li><b>Device:  "+ this.deviceName+"</b></li></p>");
            htmlBuilder.append("<p>MDFID:  "+ this.seriesNo+"</p>");
            
            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : featuresForDevice.entrySet()) {
//                System.out.println("FEATURES FOR DEVICE WITH SYSOID:"+ entry.getKey());
                htmlBuilder.append("<p><b>SYSOID:  "+ entry.getKey() +"</b></p><ol>");
                if(entry.getValue().isEmpty()) {
//                    System.out.println("NO FEATURES FOR THIS SYSOID");
//                    htmlBuilder.append("<p>NO DEVICE FEATURES FOR THIS SYSOID</p>");
                }
                else{
                    HashMap<String, ArrayList<String>>fileToFeatures = entry.getValue();
                    int ind = 0;
//                    now traverse the fillist and features
                    for (Map.Entry<String, ArrayList<String> > entry2 : fileToFeatures.entrySet()) {
//                        check if this featuresfile is present in devicegroup
                        if(!DeviceProfileToFeatures.containsKey(entry2.getKey())) {
//                        System.out.println("FROM file:"+ entry2.getKey()+"--------FEATURELIST FOR SYSOID AS FOLLOWED"+entry2.getValue().size());
                            if(ind == 0)
                                htmlBuilder.append("<p>Device specific featutes</p>");
                            ind ++;
                            htmlBuilder.append("<p><li><b>"+ entry2.getKey()+"</b></li></p><ol>");
                            for(int i = 0 ; i < entry2.getValue().size(); i++) {
//                                System.out.println(entry2.getValue().get(i));
                                htmlBuilder.append("<li>"+ entry2.getValue().get(i) +"</li>");
                            }
                            htmlBuilder.append("</ol>");
                        }
                    }
                
                }
                htmlBuilder.append("</ol>");
            }
        }
        if(level == 0 ) {
//            System.out.println("NUMBER OF DAR FILES :"+ DeviceProfileToFeatures.size());
//            htmlBuilder.append("<p><h3>DEVICE PROFILE LIST WITH FEATURES</h3></p>");
            if(DeviceProfileToFeatures.size() > 0 )
                htmlBuilder.append("<p>Common features</p><ol>");
            for (Map.Entry<String, ArrayList<String> > entry : DeviceProfileToFeatures.entrySet()) {
//                System.out.println("FROM DEVICE PROFILE:"+ entry.getKey()+"--------FEATURELIST AS FOLLOWED"+entry.getValue().size());
                htmlBuilder.append("<p><li> "+ entry.getKey()+"</li></p><ol>");
                for(int i = 0 ; i < entry.getValue().size(); i++) {
//                    System.out.println(entry.getValue().get(i));
                    htmlBuilder.append("<li>"+ entry.getValue().get(i) +"</li>");
                }
                htmlBuilder.append("</ol>");
            }
//            to list devices;
            htmlBuilder.append("</ol>");
            htmlBuilder.append("<ol>");
        }
//        System.out.println("============================");
    
    }
    
    
    public void printDeviceGroup(StringBuilder htmlBuilder) {
        htmlBuilder.append("<p><b>DeviceGroup:</b>  "+ this.deviceName+"</p>");
        htmlBuilder.append("<p><b>Product Series Number:</b> "+ this.seriesNo+"</p>");
        if(DeviceProfileToFeatures.size() > 0)
            htmlBuilder.append("<p>Common features</p><ol>");
        for (Map.Entry<String, ArrayList<String> > entry : DeviceProfileToFeatures.entrySet()) {
//            System.out.println("FROM DEVICE PROFILE:"+ entry.getKey()+"--------FEATURELIST AS FOLLOWED"+entry.getValue().size());
            htmlBuilder.append("<p><li><b>"+ entry.getKey()+"</b></li></p><ol>");
            for(int i = 0 ; i < entry.getValue().size(); i++) {
//                System.out.println(entry.getValue().get(i));
                htmlBuilder.append("<li>"+ entry.getValue().get(i) +"</li>");
            } 
            htmlBuilder.append("</ol>");
        }
        htmlBuilder.append("</ol>");
    }
    public void printDevice(StringBuilder htmlBuilder) {
        
        htmlBuilder.append("<p><li><b>Device:</b>  "+ this.deviceName+"</li></p>");
        htmlBuilder.append("<p><b>MDFID:</b> "+ this.seriesNo+"</p>");
       
        for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : featuresForDevice.entrySet()) {
//            System.out.println("FEATURES FOR DEVICE WITH SYSOID:"+ entry.getKey());
            htmlBuilder.append("<p><b>SYSOID: </b>"+ entry.getKey() +"</p><ol>");
           
            if(entry.getValue().isEmpty()) {
//                System.out.println("NO FEATURES FOR THIS SYSOID");
//                htmlBuilder.append("<p>NO DEVICE FEATURES FOR THIS SYSOID</p>");
            }
            else{
                HashMap<String, ArrayList<String>>fileToFeatures = entry.getValue();
                
                int ind = 0;
//                now traverse the filelist and features
                for (Map.Entry<String, ArrayList<String> > entry2 : fileToFeatures.entrySet()) {
//                    check if this featuresfile is present in devicegroup
                    if(!DeviceProfileToFeatures.containsKey(entry2.getKey())) {
//                    System.out.println("FROM file:"+ entry2.getKey()+"--------FEATURELIST FOR SYSOID AS FOLLOWED"+entry2.getValue().size());
                        if(ind == 0)
                            htmlBuilder.append("<p>Device specific features</p>");
                        ind++;
                        htmlBuilder.append("<p><b><li>"+ entry2.getKey()+"</li></b></p><ol>");
                        
                        for(int i = 0 ; i < entry2.getValue().size(); i++) {
//                            System.out.println(entry2.getValue().get(i));
                            htmlBuilder.append("<li>"+ entry2.getValue().get(i) +"</li>");
                        }
                        htmlBuilder.append("</ol>");
                        
                    }
                    
                }
            }
            htmlBuilder.append("</ol>");
        }
    }
}
