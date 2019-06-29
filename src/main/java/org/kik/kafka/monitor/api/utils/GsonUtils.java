package org.kik.kafka.monitor.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class GsonUtils {

    private static Gson gson = new Gson();

    private GsonUtils() {
        // Static class
    }

    public static <T> T readJsonFromFile(String filename, Class<T> classOfT) {
        T wr = null;
        try (InputStream in = loadJsonFile(filename);
                InputStreamReader isr = new InputStreamReader(in);
                JsonReader reader = new JsonReader(isr);) {
            wr = gson.fromJson(reader, classOfT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wr;
    }

    public static InputStream loadJsonFile(String filename) throws FileNotFoundException {
        InputStream stream = null;
        try {
            if (filename != null && !filename.isEmpty()) {
                stream = new FileInputStream(new File(filename));
            }
        } catch (FileNotFoundException fnf) {
            System.err.println(filename + " not found !");
            stream = GsonUtils.class.getClassLoader().getResourceAsStream(filename);
        }
        return stream;
    }

}
