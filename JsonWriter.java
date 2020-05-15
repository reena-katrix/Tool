import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 
public class JsonWriter {
    private static FileWriter file;
 
    @SuppressWarnings("unchecked")
    public static void func(JSONObject obj) {
 
        // JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
//        JSONObject obj = new JSONObject();
//        obj.put("Name", "Crunchify.com");
//        obj.put("Author", "App Shah");
// 
//        JSONArray company = new JSONArray();
//        company.add("Company: Facebook");
//        company.add("Company: PayPal");
//        company.add("Company: Google");
//        obj.put("Company List", company);
        
        try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter("/Users/reenaya/Documents/json.json");
            file.write(obj.toJSONString());
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
 
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}