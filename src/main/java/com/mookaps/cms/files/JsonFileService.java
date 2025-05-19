package com.mookaps.cms.files;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JsonFileService {
    private static String filename = "blacklist_token.json";

    public void saveJson(String jsonString) {
        try {
            FileUtil.writeJsonToFile(jsonString, filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save JSON", e);
        }
    }

    public List<Map<String, Object>> readJson() {
        try {
            return FileUtil.readJsonFromFile(filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON", e);
        }
    }
}
