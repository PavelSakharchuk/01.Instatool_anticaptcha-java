package com.anticaptcha.helper;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.nio.file.Files;

public class StringHelper {

    private StringHelper() {
    }


    public static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";

        for (String part : parts) {
            camelCaseString += part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }

        return camelCaseString.substring(0, 1).toLowerCase() + camelCaseString.substring(1);
    }

    public static String imageFileToBase64String(File file) {
        try {
            return DatatypeConverter.printBase64Binary(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            return null;
        }
    }
}
