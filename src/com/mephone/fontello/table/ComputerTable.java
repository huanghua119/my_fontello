package com.mephone.fontello.table;

import java.io.IOException;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mephone.fontello.cache.CacheManager;
import com.mephone.fontello.util.JsonUtils;

public class ComputerTable extends AbsTable {

    public ComputerTable() {
        setTableName("Computer");
    }

    public boolean isEffectiveComputer(String mac) {
        JSONObject where = new JSONObject();
        where.put("macAdd", mac);
        try {
            JSONObject result = findOne(where.toString());
            if (result != null) {
                String address = JsonUtils.getJSONString(result, "macAdd");
                if (mac.equals(address)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CacheManager.getInstance().clearAllData();
        return false;
    }
}
