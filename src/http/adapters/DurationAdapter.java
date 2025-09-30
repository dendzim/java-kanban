package http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<java.time.Duration> {
    @Override
    public void write(JsonWriter jsonWriter, java.time.Duration duration) throws IOException {
        jsonWriter.value(duration.toMinutes());
    }

    @Override
    public java.time.Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(jsonReader.nextInt());
    }
}
