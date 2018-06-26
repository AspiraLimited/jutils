package com.aspiralimited.jutils;

import com.aspiralimited.jutils.logger.AbbLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;

public class ConfigLoader {
    private static final AbbLogger logger = new AbbLogger();

    public static <T> T loadConfig(Class<T> clazz, String fileName) throws IOException {
        fileName = getProperty("user.dir") + fileName;

        String json = FileUtils.readFileToString(new File(fileName), "UTF-8");

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Error by loadConfig file {}", fileName, e);
            throw e;
        }
    }
}
