package brycewedig.integration.helpers;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static java.lang.Long.parseLong;

public class JsonHelper {

    public JsonHelper() {}

    public static String getString(File file, String key) throws IOException {
        JSONObject jsonObject = readJson(file);
        return jsonObject.getString(key);
    }

    public static LocalDateTime getLocalDateTime(File file, String key) throws IOException {
        JSONObject json = readJson(file);
        return LocalDateTime.parse(json.get(key).toString());
    }

    public static long getLong(File file, String key) throws IOException {
        JSONObject json = readJson(file);
        return parseLong(json.getString(key));
    }

    private static JSONObject readJson(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        String jsonTxt = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return new JSONObject(jsonTxt);
    }

    // TODO write to json file, make sure this overwrites the entire file
    public static void writeToJson(File file, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }
}
