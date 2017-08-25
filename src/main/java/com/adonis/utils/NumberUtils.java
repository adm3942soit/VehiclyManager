package com.adonis.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    public static Double round(Double val) {
        return new BigDecimal(val.toString()).setScale(0, RoundingMode.HALF_UP).doubleValue();
    }

}
