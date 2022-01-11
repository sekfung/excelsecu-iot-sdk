package com.excelsecu.iot.util;

/**
 * @author sekfung
 * @date 2022/1/2
 */
public class StringsUtil {
    /**
     * 判断字符串是否都不为空
     * @param values
     * @return
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            return false;
        }
        for (String value : values) {
            result &= !isEmpty(value);
        }
        return result;
    }

    /**
     * 判断字符串为空
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
      return value == null || value.isEmpty();
    }
}
