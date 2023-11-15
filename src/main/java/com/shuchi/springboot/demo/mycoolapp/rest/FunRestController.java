package com.shuchi.springboot.demo.mycoolapp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

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

    @GetMapping("/encryptMe/{password}")
    public String getDailyFortune(@PathVariable String password) {
        String encrypted = new PwdEncryptDecrypt().encryptPassword(password);
        return password + " encrypted : " + encrypted + "\n decrypted : "
                + new PwdEncryptDecrypt().decryptPassword(encrypted);

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

    // post HTTP response
    public JsonObject postResponse(String endpoint, Object request){
        try {
            JsonObject responseObject=null;
            RestTemplate resTemplate=new RestTemplate();
            resTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<String> response=resTemplate.postForEntity(endpoint, request,String.class);
            Gson gson=new Gson();
            responseObject=gson.fromJson(response.getBody(),JsonObject.class);
            return responseObject;
        } catch(HttpStatusCodeException hce){
            log.error("error in response "+hce.getResponseBodyAsString());
            throw hce;
        }
    }

    JSONObject loadSchemaToJson(String schemaPath) {
        JSONObject schemaJson = null;
        BufferedReader streamReader = null;
        StringBuilder responseStringBuilder = new StringBuilder();
        InputStreamReader input = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(schemaPath)));
        streamReader = new BufferedReader(input);
        String inputStr;
        try {
            while ((inputStr = streamReader.readLine()) != null) {
                responseStringBuilder.append(inputStr);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        schemaJson = new JSONObject(responseStringBuilder.toString());
        return schemaJson;

    }
    public List<String> getTokensWithCollection(String str, String delimiter){
        return Collections.list(new StringTokenizer(str,delimiter)).stream().map(token -> (String) token).collect(Collectors.toList());
    }
}