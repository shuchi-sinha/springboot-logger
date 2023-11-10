package com.shuchi.springboot.demo.mycoolapp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FunRestController {
    private static Logger log = LoggerFactory.getLogger(FunRestController.class);
    // expose a new endpoint for "whitelisting URLs"

    @GetMapping("/workout/{url}")
    public String getDailyWorkout(@PathVariable("url") String url) {
        return isWhiteListed(url) ? "whitelisted URL page displays" : "blacklisted page";
    }

    @GetMapping(value = "/{name}")
    public String greet(@PathVariable("name") String name) {
        log.info("in greet method");
        var greet = "Hi %s";
        try {
            return String.format(greet, name) + "\n" + jsonBlock().toString();
        } catch (Exception e) {
            log.atError();
            return greet + " error in parsing json";
        }
    }

    // expose a new endpoint for "fortune"

    @GetMapping("/fortune")
    public String getDailyFortune() {
        return "Fortune favors the brave";
    }

    private static JsonObject jsonBlock() throws Exception {
        String text = """
                {
                  "name": "John Doe",
                  "age": 45,
                  "address": "Doe Street, 23, Java Town"
                }""";

        return checkJsonSyntax(text);
    }

    public static JsonObject checkJsonSyntax(String inputJson) throws Exception {
        JsonObject jsonObject = null;

        try {
            System.out.println("in checkJsonSyntax method");

            jsonObject = JsonParser.parseString(inputJson).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new Exception("input is not a json file content");
        }
        return jsonObject;
    }

    public static JsonObject setJsonProperty(JsonObject jsonobject, String newKey, Object newValue) {
        if (newValue != null) {
            boolean isKeyExists = isKeyExists(jsonobject, newKey, (JsonObject) newValue);
            if (!isKeyExists) {
                dataTypeWrapper(jsonobject, newKey, newValue);
            }
        }
        return jsonobject;
    }

    public static boolean isKeyExists(JsonObject jo, String searchKey, JsonObject newValue) {
        boolean exists = jo.has(searchKey);
        if (!exists) {
            for (String key : jo.keySet()) {
                exists = isKeyExists((JsonObject) jo.get(key), searchKey, newValue);
            }
        }
        if (exists) {
            dataTypeWrapper(jo, searchKey, newValue);
        }
        return exists;
    }

    // update the value if key exists
    public static void dataTypeWrapper(JsonObject jsonObject, String key, Object newValue) {

        if (newValue instanceof String)
            jsonObject.addProperty(key, (String) newValue);
        if (newValue instanceof Boolean)
            jsonObject.addProperty(key, (Boolean) newValue);
        if (newValue instanceof Double || newValue instanceof Integer || newValue instanceof Long)
            jsonObject.addProperty(key, (Number) newValue);
    }

    public static boolean isWhiteListed(String url) {
        return isWhiteListed(Pattern.compile("([a-zA-Z_0-9]*)"), url);
    }

    private static boolean isWhiteListed(Pattern p, String url) {
        log.info("url is whitelisted if contains other than alphanumeric or _ ");
        boolean result = false;
        if (!url.isEmpty()) {
            Matcher matcher = p.matcher(url);
            if (matcher.find())
                return url.length() == matcher.group(1).length();
        } else {
            result = true;
        }
        return result;
    }
}