package persistence;

import org.json.JSONObject;

// CITATION: modified from JSonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
