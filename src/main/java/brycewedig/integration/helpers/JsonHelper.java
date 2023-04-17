package brycewedig.integration.helpers;

import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHelper {

    // TODO retrieve last successful run time

    // TODO retrieve authentication information for both toggl and gcal - first figure out gcal auth

    // TODO write to json file
    public static void writeToJson(File file, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }

//    public static JSONArray readJson(File file) {
//        // TODO read the file into a JSON object
//    }
}
