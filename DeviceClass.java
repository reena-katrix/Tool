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
            htmlBuilder.append("<p><li><b>DeviceName:  "+ this.deviceName+"</b></li></p>");
            htmlBuilder.append("<p>MDFID:  "+ this.seriesNo+"</p>");
            
            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : featuresForDevice.entrySet()) {
//                System.out.println("FEATURES FOR DEVICE WITH SYSOID:"+ entry.getKey());
                htmlBuilder.append("<p><b>SYSOID:  "+ entry.getKey() +"</b></p>");
                if(entry.getValue().isEmpty()) {
//                    System.out.println("NO FEATURES FOR THIS SYSOID");
//                    htmlBuilder.append("<p>NO DEVICE FEATURES FOR THIS SYSOID</p>");
                }
                else{
                    HashMap<String, ArrayList<String>>fileToFeatures = entry.getValue();
                    
//                    now traverse the fillist and features
                    for (Map.Entry<String, ArrayList<String> > entry2 : fileToFeatures.entrySet()) {
//                        check if this featuresfile is present in devicegroup
                        if(!DeviceProfileToFeatures.containsKey(entry2.getKey())) {
//                        System.out.println("FROM file:"+ entry2.getKey()+"--------FEATURELIST FOR SYSOID AS FOLLOWED"+entry2.getValue().size());
                        
                            htmlBuilder.append("<p>DEVICE SPECIFIC FEATURES For ABOVE SYSOID FROM "+ entry2.getKey()+"</p><ol>");
                            for(int i = 0 ; i < entry2.getValue().size(); i++) {
//                                System.out.println(entry2.getValue().get(i));
                                htmlBuilder.append("<li>"+ entry2.getValue().get(i) +"</li>");
                            }
                            htmlBuilder.append("</ol>");
                        }
                    }
                
                }
            }
        }
        if(level == 0 ) {
//            System.out.println("NUMBER OF DAR FILES :"+ DeviceProfileToFeatures.size());
//            htmlBuilder.append("<p><h3>DEVICE PROFILE LIST WITH FEATURES</h3></p>");
            for (Map.Entry<String, ArrayList<String> > entry : DeviceProfileToFeatures.entrySet()) {
//                System.out.println("FROM DEVICE PROFILE:"+ entry.getKey()+"--------FEATURELIST AS FOLLOWED"+entry.getValue().size());
                htmlBuilder.append("<p><h3>FEATURES FROM "+ entry.getKey()+"</h3></p><ol>");
                for(int i = 0 ; i < entry.getValue().size(); i++) {
//                    System.out.println(entry.getValue().get(i));
                    htmlBuilder.append("<li>"+ entry.getValue().get(i) +"</li>");
                }
                htmlBuilder.append("</ol>");
            }
//            to list devices;
            htmlBuilder.append("<ol>");
        }
//        System.out.println("============================");
    
    }
    
    
    public void printDeviceGroup(StringBuilder htmlBuilder,  JSONObject mainObj) {
//        System.out.println("Parent DEVICEGROUP Name :" + this.deviceName);
//        System.out.println("ProductSeries Number :" + this.seriesNo);
        JSONObject devicegroup = new JSONObject();
        devicegroup.put("Devicegroup" , this.deviceName);
        devicegroup.put("Product Series", this.seriesNo);
        
        JSONArray deviceProfile = new JSONArray();
        
        htmlBuilder.append("<p><b>DeviceGroup Name:</b>  "+ this.deviceName+"</p>");
        htmlBuilder.append("<p><b>Product Series Number:</b> "+ this.seriesNo+"</p>");
        for (Map.Entry<String, ArrayList<String> > entry : DeviceProfileToFeatures.entrySet()) {
//            System.out.println("FROM DEVICE PROFILE:"+ entry.getKey()+"--------FEATURELIST AS FOLLOWED"+entry.getValue().size());
            htmlBuilder.append("<p><b>FEATURES FROM "+ entry.getKey()+"</b></p><ol>");
            JSONObject deviceprofileObj = new JSONObject();
            deviceprofileObj.put("deviceProfile", entry.getKey());
            JSONArray features = new JSONArray();
            for(int i = 0 ; i < entry.getValue().size(); i++) {
//                System.out.println(entry.getValue().get(i));
                htmlBuilder.append("<li>"+ entry.getValue().get(i) +"</li>");
                features.add(entry.getValue().get(i));
            }
            deviceprofileObj.put("features", features);
            deviceProfile.add(deviceprofileObj);
            htmlBuilder.append("</ol>");
        }
        devicegroup.put("deviceProfileFeatures" , deviceProfile);
        mainObj.put("devicegroup", devicegroup);
    }
    public void printDevice(StringBuilder htmlBuilder, JSONObject mainObj) {
        JSONObject device = new JSONObject();
        
        device.put("DEVICE", this.deviceName );
        device.put("MDFID", this.seriesNo);
        
        JSONArray sysoid = new JSONArray();
        htmlBuilder.append("<p><li><b>DeviceName:</b>  "+ this.deviceName+"</li></p>");
        htmlBuilder.append("<p><b>MDFID:</b> "+ this.seriesNo+"</p>");
       
        for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : featuresForDevice.entrySet()) {
//            System.out.println("FEATURES FOR DEVICE WITH SYSOID:"+ entry.getKey());
            htmlBuilder.append("<p><b>SYSOID: </b>"+ entry.getKey() +"</p>");
            
          //json object to store all sysoid info
            JSONObject sysoidObj = new JSONObject();
            sysoidObj.put("SYSOID", entry.getKey());
            
            //to store all deviceprofile features for this sysoid
            JSONArray deviceProfile = new JSONArray();
            
            if(entry.getValue().isEmpty()) {
//                System.out.println("NO FEATURES FOR THIS SYSOID");
//                htmlBuilder.append("<p>NO DEVICE FEATURES FOR THIS SYSOID</p>");
            }
            else{
                HashMap<String, ArrayList<String>>fileToFeatures = entry.getValue();
                
                
//                now traverse the filelist and features
                for (Map.Entry<String, ArrayList<String> > entry2 : fileToFeatures.entrySet()) {
//                    check if this featuresfile is present in devicegroup
                    if(!DeviceProfileToFeatures.containsKey(entry2.getKey())) {
//                    System.out.println("FROM file:"+ entry2.getKey()+"--------FEATURELIST FOR SYSOID AS FOLLOWED"+entry2.getValue().size());
                        JSONObject deviceprofile = new JSONObject();
                        deviceprofile.put("DeviceProfile", entry2.getKey());
                        
                        htmlBuilder.append("<p>DEVICE SPECIFIC FEATURES For ABOVE SYSOID FROM "+ entry2.getKey()+"</p><ol>");
                        
                        JSONArray featureList = new JSONArray();
                        
                        for(int i = 0 ; i < entry2.getValue().size(); i++) {
//                            System.out.println(entry2.getValue().get(i));
                            htmlBuilder.append("<li>"+ entry2.getValue().get(i) +"</li>");
                            featureList.add(entry2.getValue().get(i));
                        }
                        htmlBuilder.append("</ol>");
                        deviceprofile.put("feautures", featureList);
                        deviceProfile.add(deviceprofile);
                    }
                    
                }
                sysoidObj.put("deviceProfile" , deviceProfile);
            
            }
            sysoid.add(sysoidObj);
        }
//        deviceGroup.put("DEVICE" , device);
        device.put("SYSOID", sysoid);
        mainObj.put("device", device);
    }
}
