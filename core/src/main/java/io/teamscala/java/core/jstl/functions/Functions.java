package io.teamscala.java.core.jstl.functions;

import io.teamscala.java.core.json.jsonlib.util.JSONUtils;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Functions tag library.
 */
public class Functions {

    /**
     * Get
     *
     * @param object the object
     * @param index  the index
     * @return the object
     */
    public static Object get(Object object, int index) {
        try {
            return CollectionUtils.get(object, index);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Index of
     *
     * @param target    the target
     * @param searchObj the search object
     * @return the index
     */
    public static int indexOf(Object target, Object searchObj) {
        if (target == null) return -1;

        if (target.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(target); i++) {
                if (Array.get(target, i).equals(searchObj)) return i;
            }
            return -1;
        }
        if (target instanceof Collection) {
            int i = 0;
            for (Object obj : (Collection<?>) target) {
                if (obj.equals(searchObj)) return i;
                i++;
            }
            return -1;
        }
        return target.toString().indexOf(searchObj != null ? searchObj.toString() : "");
    }

    /**
     * Contains
     *
     * @param target    the target
     * @param searchObj the search object
     * @return true or false
     */
    public static boolean contains(Object target, Object searchObj) {
        return indexOf(target, searchObj) > -1;
    }

    /**
     * Matches
     *
     * @param regex the regex
     * @param input the input
     * @return true or false
     */
    public static boolean matches(String regex, String input) {
        return input != null && Pattern.matches(regex, input);
    }

    /**
     * Looking at
     *
     * @param regex the regex
     * @param input the input
     * @return true or false
     */
    public static boolean lookingAt(String regex, String input) {
        return input != null && Pattern.compile(regex).matcher(input).lookingAt();
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 반환한다.
     *
     * @param target the target object
     * @return JSON 형식의 문자열
     */
    public static String toJSON(Object target) {
        return JSONUtils.toJSONString(target);
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 변경후 Escape 하여 반환한다.
     *
     * @param target the target object
     * @return Escape 된 JSON 형식의 문자열
     */
    public static String toJSONAndEscape(Object target) {
        return JavaScriptUtils.javaScriptEscape(toJSON(target));
    }

    /**
     * 국내 사업자 번호 형식으로 변환하여 반환한다.
     *
     * @param input the input
     * @return string
     */
    public static String formatBrnKO(Object input) {
        if (input == null) return "";

        String brn = input.toString().replace("-", "");
        if (brn.length() != 10 || !NumberUtils.isDigits(brn)) return brn;
        return brn.substring(0, 3) + "-" + brn.substring(3, 5) + "-" + brn.substring(5);
    }
}
