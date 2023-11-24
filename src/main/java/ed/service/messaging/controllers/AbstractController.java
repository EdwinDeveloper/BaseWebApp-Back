package ed.service.messaging.controllers;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link AbstractController}
 */
public abstract class AbstractController {

    public Map<String, Object> ok(Object object){
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", object);
        return response;
    }

    public Map<String, Object> ok(int code, Object object){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("data", object);
        return response;
    }

    public Map<String, Object> notOk(int code, Object object){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("data", object);
        return response;
    }

    public Map<String, Object> badRequest(Object error) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 400);
        response.put("data", error);
        return response;
    }

}
