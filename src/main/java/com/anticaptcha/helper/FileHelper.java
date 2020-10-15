package com.anticaptcha.helper;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileHelper {

    private FileHelper() {
    }

    /**
     * The resource URL is not working in the JAR.
     * If we try to access a file that is inside a JAR,
     * it throws NoSuchFileException (linux), InvalidPathException (Windows).
     * Resource URL Sample: file:java-io.jar!/json/file1.json.
     * <p>
     * Failed if return new File(resource.getFile())
     * and files have whitespaces or special characters.
     */
    public static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = FileHelper.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!: " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
