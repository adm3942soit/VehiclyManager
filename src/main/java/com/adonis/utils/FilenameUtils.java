package com.adonis.utils;

import org.urllib.*;

import java.io.File;

public class FilenameUtils {
    public static String getName(String sourceUrl) {
        int index = sourceUrl.lastIndexOf(File.separator);
        String fileName = sourceUrl.substring(index + 1);
        return fileName;
    }
    public static String getExtension(String imageName) {
        int index = imageName.lastIndexOf(".");
        if(index!=-1){
            return imageName.substring(index+1);
        }
        else return "";
    }
    public static String renameExtension(String imageName) {
        String newName="";
        int index = imageName.lastIndexOf(".");
        if(index!=-1){
            newName=imageName.substring(0, index-1)+".jpg";
        }
        else newName=imageName+"jpg";
     return newName;
    }

    public static String getNameFileFromUrl(String sourceUrl){
        return Urls.parse(sourceUrl).path().filename();
    }
}
