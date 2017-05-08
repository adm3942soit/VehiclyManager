package com.adonis.utils;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by oksdud on 08.05.2017.
 */
public class CollectionUtils {
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

}
