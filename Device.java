import com.google.gson.*;
import java.lang.reflect.Type;

public class Device implements JsonSerializer<DeviceClass> {

    @Override
    public JsonElement serialize(DeviceClass node, Type type, JsonSerializationContext jsc) {
        JsonObject jObj = (JsonObject)new GsonBuilder().create().toJsonTree(node);
        if (node.isDevice){
            jObj.remove("DeviceProfileToFeatures");
        }
        return jObj;
    }
}