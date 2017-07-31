package com.adonis.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * Created by oksdud on 08.05.2017.
 */
public class CollectionUtils {

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static Map<String, String> map(String key, String property, String key1, String property1){
        Map<String, String> map = Maps.newHashMap();
        map.put(key, property);
        map.put(key1, property1);
        return map;

    }

    public static Map<String, String> map(Set<String> keys, Properties properties){
        Map<String, String> map = Maps.newHashMap();
        keys.forEach(key->{map.put(key, String.valueOf(properties.get(key)));});
        return map;

    }


    public static List<String> convertIntoList(String commaSeparatedString) {
        if (Strings.isNullOrEmpty(commaSeparatedString)) return Lists.newArrayList();
        List<String> items = Arrays.asList(commaSeparatedString.split("\\s*,\\s*"));
        return items;
    }


    public static String convertIntoCommaSeparatedString(List<String> strings) {
        if (isNullOrEmpty(strings)) return "";
        StringBuffer buffer = new StringBuffer("");
        for (String string : strings) {
            if (!Strings.isNullOrEmpty(buffer.toString())) buffer.append(",");
            buffer.append(string);
        }
        return buffer.toString();
    }

    public static String convertIntegersIntoCommaSeparatedString(List<Integer> integers) {
        if (isNullOrEmpty(integers)) return "";
        StringBuffer buffer = new StringBuffer("");
        for (Integer intValue : integers) {
            if (!Strings.isNullOrEmpty(buffer.toString())) buffer.append(",");
            buffer.append(intValue);
        }
        return buffer.toString();
    }

    public static String convertLongsIntoCommaSeparatedString(List<Long> longs) {
        if (isNullOrEmpty(longs)) return "";
        StringBuffer buffer = new StringBuffer("");
        for (Long longValue : longs) {
            if (!Strings.isNullOrEmpty(buffer.toString())) buffer.append(",");
            buffer.append(longValue);
        }
        return buffer.toString();
    }

}
