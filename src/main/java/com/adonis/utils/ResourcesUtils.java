package com.adonis.utils;

import java.io.IOException;
import java.io.InputStream;
/**
 * Created by oksdud on 29.05.2017.
 */
public class ResourcesUtils {

    public static InputStream getFileFromResources(String fileName){
        return ResourcesUtils.class.getClassLoader().getResourceAsStream(fileName);
    }

    public static String getFileContentFromResources(String fileName){
        InputStream inputStream = ResourcesUtils.class.getClassLoader().getResourceAsStream(fileName);
        String result = null;
        try {
            result = org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }


}
