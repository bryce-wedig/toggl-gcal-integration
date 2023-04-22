package brycewedig.integration.helpers;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class JsonHelper {

    public JsonHelper() {}

    private static JSONObject readJson(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        String jsonTxt = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return new JSONObject(jsonTxt);
    }

    public static LocalDateTime getLastSuccessfulRunTime(File file) throws IOException {
        JSONObject json = readJson(file);
        return LocalDateTime.parse(json.get("last_successful_run_time").toString());
    }

    public static String getTogglToken(File file) throws IOException {
        JSONObject json = readJson(file);
        return json.get("toggl_token").toString();
    }

    // TODO get gcal auth information


    // TODO write to json file, make sure this overwrites the entire file
    public static void writeToJson(File file, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }
}
