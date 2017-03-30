package com.mephone.fontello.table;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.EncryptUtils;
import com.mephone.fontello.util.JsonUtils;

/**
 * 后台表对象
 * 
 * @author tony amin
 */
public class AbsTable {
    public static final boolean DEBUG = true;
    public static final Object LOCK = new Object();
    /**
     * 数据库objectId字段
     */
    protected static final String OBJECTID = "objectId";
    private static final String APP_ID = "cf33e2d7dc495b253b95526fdc1da093300a3312fedf8fc61892b804798091cf";
    private static final String REST_KEY = "4f32a61637918e9970c526782fc03ea3200ecc6514f384c2939e722648c0b756";
    private String mTableName;

    public AbsTable() {
        if (!QueryServer.isInit()) {
            String app_id = EncryptUtils.INSTANCE.Decrypt(APP_ID, "mephone",
                    EncryptUtils.ENCRYPT_TYPE_TEA);
            String rest_key = EncryptUtils.INSTANCE.Decrypt(REST_KEY,
                    "mephone", EncryptUtils.ENCRYPT_TYPE_TEA);
            QueryServer.initBmob(app_id, rest_key);
        }
    }

    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
    }

    public String insert(String paramContent) {
        if (mTableName == null) {
        }
        return QueryServer.insert(mTableName, paramContent);
    }

    public String update(String objectId, String paramContent) {
        if (mTableName == null) {
        }

        return QueryServer.update(mTableName, objectId, paramContent);
    }

    /**
     * 查询数据
     * 
     * @param keys
     *            返回列 （例：score,name）
     * @param where
     *            条件JOSN格式
     * @param skip
     *            跳过记录数
     * @param limit
     *            查询记录数（1~1000）
     * @param order
     *            排序字段（例：score,-name）
     * @return JSON格式结果
     * @throws JSONException
     * @throws IOException
     */
    public JSONArray find(String where, String keys, int skip, int limit,
            String order) throws JSONException, IOException {
        synchronized (LOCK) {
            if (mTableName == null) {
            }

            String source = QueryServer.findColumns(mTableName, keys, where,
                    skip, limit, order, 0);

            JSONObject jsonObject = JsonUtils.stringToJSONObject(source);
            JSONArray results = JsonUtils.getJSONArray(jsonObject, "results");
            return results;
        }
    }

    /**
     * 查询一条数据
     * 
     * @param where
     *            条件JOSN格式
     * @return JSON格式结果
     * @throws JSONException
     * @throws IOException
     */
    public JSONObject findOne(String where) throws JSONException, IOException {
        synchronized (LOCK) {
            if (mTableName == null) {
            }

            String source = QueryServer.findColumns(getTableName(), "", where,
                    0, 1, "", SystemConfig.DefalutConfig.CACHEL_EXPIRED_TIME);

            JSONObject jsonObject = JsonUtils.stringToJSONObject(source);
            JSONArray results = JsonUtils.getJSONArray(jsonObject, "results");

            if (results != null && results.size() > 0) {
                JSONObject object = results.getJSONObject(0);
                return object;
            }
            return null;
        }
    }

    /**
     * 获取JSON对象中的objectId值
     * 
     * @param object
     *            JSON对象
     * @return objectId所对应的值
     * @throws JSONException
     */
    public String getJSONObjectId(JSONObject object) throws JSONException {
        if (object.containsKey(OBJECTID)) {
            return object.getString(OBJECTID);
        }
        return "";
    }

    /**
     * 原子计数器
     * 
     * @return 计数字段的JSON对象
     * @throws JSONException
     */
    public JSONObject getIncrementJSON() throws JSONException {
        JSONObject increment = new JSONObject();
        increment.put("__op", "Increment");
        increment.put("amount", 1);
        return increment;
    }

    /**
     * 一对一关联
     * 
     * @param table
     *            关联表
     * @param objectId
     *            关联数据obejctId
     * @return
     * @throws JSONException
     */
    public JSONObject getPointerJSON(String table, String objectId)
            throws JSONException {
        JSONObject pointer = new JSONObject();
        pointer.put("__type", "Pointer");
        pointer.put("className", table);
        pointer.put("objectId", objectId);
        return pointer;
    }

    public JSONArray findBQL(String BQL, String value) throws JSONException {
        synchronized (LOCK) {
            if (mTableName == null) {
            }
            String source = QueryServer.findBQL(BQL, value, 0);
            JSONObject jsonObject = JsonUtils.stringToJSONObject(source);
            JSONArray results = JsonUtils.getJSONArray(jsonObject, "results");
            return results;
        }
    }

    public int findCountBQL(String BQL, String value) throws JSONException {
        synchronized (LOCK) {
            if (mTableName == null) {
            }
            String source = QueryServer.findBQL(BQL, value, 0);
            JSONObject jsonObject = JsonUtils.stringToJSONObject(source);
            if (jsonObject != null && jsonObject.containsKey("count")) {
                return jsonObject.getIntValue("count");
            }
            return 0;
        }
    }

}
