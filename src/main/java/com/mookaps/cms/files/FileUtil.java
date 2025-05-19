package com.mookaps.cms.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeJsonToFile(String json, String path) throws IOException {
        File file = new File(path);
        List<Map<String, Object>> existingList = new ArrayList<>();
        if (file.exists() && file.length() > 0) {
            existingList = mapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
            });
        }
        Map<String, Object> newEntry = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        existingList.add(newEntry);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, existingList);
    }

    public static List<Map<String, Object>> readJsonFromFile(String path) throws IOException {
        File file = new File(path);
        List<Map<String, Object>> existingList = new ArrayList<>();
        if (file.exists() && file.length() > 0) {
            existingList = mapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
            });
        }
        return existingList;
    }

}
