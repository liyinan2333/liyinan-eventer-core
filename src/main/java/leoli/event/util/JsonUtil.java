package leoli.event.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author leoli
 * @date 2021/09/21
 */
public class JsonUtil {

    private static final ObjectMapper JSON = new ObjectMapper();

    public static String toJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return JSON.writeValueAsString(obj);
    }

    public static <T> T fromJson(String json, Class<T> clz) throws JsonProcessingException {
        if (json == null || "".equals(json.trim())) {
            return null;
        }
        return JSON.readValue(json, clz);
    }

}
