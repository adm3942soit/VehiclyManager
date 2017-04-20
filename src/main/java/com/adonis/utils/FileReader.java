package com.adonis.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by oksdud on 19.04.2017.
 */
public class FileReader {

    public static String readFromFileFromResources(String fileName){
        ClassLoader classLoader = DatabaseUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        StringBuffer buffer = new StringBuffer("");
        try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {
            stream.forEach(buffer::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
     return buffer.toString();
    }

}
