package com.mephone.fontello.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
            case '{':
            case '[':
                sb.append(current);
                sb.append('\n');
                indent++;
                addIndentBlank(sb, indent);
                break;
            case '}':
            case ']':
                sb.append('\n');
                indent--;
                addIndentBlank(sb, indent);
                sb.append(current);
                break;
            case ',':
                sb.append(current);
                if (last != '\\') {
                    sb.append('\n');
                    addIndentBlank(sb, indent);
                }
                break;
            default:
                sb.append(current);
            }
        }

        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
    }

    public static JSONObject stringToJSONObject(String source)
            throws JSONException {
        JSONObject jsonObject = JSONObject.parseObject(source);
        return jsonObject;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name)
            throws JSONException {
        return jsonObject.getJSONObject(name);
    }

    public static String getJSONString(JSONObject jsonObject, String name)
            throws JSONException {
        return jsonObject.getString(name);
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String name)
            throws JSONException {
        if (jsonObject.containsKey(name)) {
            return jsonObject.getJSONArray(name);
        }
        return null;
    }

    public static boolean isJson(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        try {
            stringToJSONObject(str);
            return true;
        } catch (JSONException exception) {
        }
        return false;
    }
}
