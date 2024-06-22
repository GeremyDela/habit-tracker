package persistence;

import model.Day;
import model.Month;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// CITATION: modified from JSonSerializationDemo

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Month read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMonth(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    private Month parseMonth(JSONObject jsonObject) {
        ArrayList<String> g = new ArrayList<>();
        int x = 0;
        try {
            while (jsonObject.getString("gB " + x) != null) {
                g.add(jsonObject.getString("gB " + x));
                x++;
            }
        } catch (JSONException e) {
        }
        x = 0;

        ArrayList<String> b = new ArrayList<>();
        try {
            while (jsonObject.getString("bB " + x) != null) {
                b.add(jsonObject.getString("bB " + x));
                x++;
            }
        } catch (JSONException e) {
        }
        x = 0;

        ArrayList<String> c = new ArrayList<>();
        try {
            while (jsonObject.getString("cD " + x) != null) {
                c.add(jsonObject.getString("cD " + x));
                x++;
            }
        } catch (JSONException e) {
        }

        return new Month(jsonObject.getString("currMonth"), addDays(jsonObject), g, b, c);
    }

    private ArrayList<Day> addDays(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("days");
        ArrayList<Day> days = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextDay = (JSONObject) json;
            days.add(addDay(nextDay));
        }
        return days;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private Day addDay(JSONObject jsonObject) {
        Day day = new Day(jsonObject.getInt("day"), jsonObject.getString("weekDate"),
                jsonObject.getString("entry"), jsonObject.getInt("gBSize"),
                jsonObject.getInt("bBSize"), jsonObject.getInt("cDSize"), jsonObject.getInt("emotion"));

        int x = 0;
        try {
            while (jsonObject.getString("goodBox " + x) != null) {
                if (Objects.equals(jsonObject.getString("goodBox " + x), "true")) {
                    day.check(x, true, true);
                } else {
                    day.check(x, true, false);
                }
                x++;
            }
        } catch (JSONException e) {
        }
        x = 0;

        try {
            while (jsonObject.getString("badBox " + x) != null) {
                if (Objects.equals(jsonObject.getString("badBox " + x), "true")) {
                    day.check(x, false, true);
                } else {
                    day.check(x, false, false);
                }
                x++;
            }
        } catch (JSONException e) {
        }
        x = 0;

        try {
            while (jsonObject.getString("stat " + x) != null) {
                day.enterData(x, false, jsonObject.getString("stat " + x));
                x++;
            }
        } catch (JSONException e) {
        }

        return day;
    }

}
