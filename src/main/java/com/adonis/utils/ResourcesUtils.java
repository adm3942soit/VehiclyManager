package com.adonis.utils;

import java.io.*;
import java.net.URL;

/**
 * Created by oksdud on 29.05.2017.
 */
public class ResourcesUtils {

    public static InputStream getFileFromResourcesAsStream(String fileName){
        return ResourcesUtils.class.getClassLoader().getResourceAsStream(fileName);
    }

    public static String getFileContent(String fileName){
        File file = new File(fileName);
        String results = "";
        try {
            InputStream inputStream = new FileInputStream(file);
            results = org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
     return results;
    }

    public static String getFileContentFromResources(String fileName){
        InputStream inputStream = getFileFromResourcesAsStream(fileName);
        String result = null;
        try {
            result = org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    public static File getFileFromResources(String fileName){
        URL resourceURL = ResourcesUtils.class.getClassLoader().getResource(fileName);
        return new File(resourceURL.getFile());
    }

}
