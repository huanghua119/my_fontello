package com.mephone.fontello.cache;

import java.io.File;

import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.CommonUtils;
import com.mephone.fontello.util.MD5Utils;

/**
 * 缓存管理器
 * 
 * @author huanghua
 */
public class CacheManager {

    /**
     * 缓存文件路径
     */
    private static String sAPP_CACHE_PATH = SystemConfig.FileSystem.CACHE_PATH;

    private static CacheManager cacheManager;

    private CacheManager() {
        initCacheDir();
    }

    /**
     * 获取CacheManager实例
     * 
     * @return CacheManager
     */
    public static synchronized CacheManager getInstance() {
        if (CacheManager.cacheManager == null) {
            CacheManager.cacheManager = new CacheManager();
        }
        return CacheManager.cacheManager;
    }

    /**
     * 从文件缓存中取出缓存，没有则返回空
     * 
     * @param key
     *            关键字
     * @return 缓存数据
     */
    public String getFileCache(final String key) {
        String md5Key = MD5Utils.md5(key);
        if (contains(md5Key)) {
            final CacheItem item = getFromCache(md5Key);
            if (item != null) {
                return item.getData();
            }
        }
        return null;
    }

    /**
     * API data 缓存到文件
     * 
     * @param key
     *            关键字
     * @param data
     *            缓存数据
     * @param expiredTime
     *            超时时间
     */
    public void putFileCache(final String key, final String data,
            long expiredTime) {
        String md5Key = MD5Utils.md5(key);

        final CacheItem item = new CacheItem(md5Key, data, expiredTime);
        putIntoCache(item);
    }

    /**
     * 查询是否有key对应的缓存文件
     * 
     * @param key
     *            关键字
     * @return 是否有缓存，True：有缓存，False：没有缓存
     */
    public boolean contains(final String key) {
        final File file = new File(sAPP_CACHE_PATH + key);
        return file.exists();
    }

    /**
     * 初始化缓存目录
     */
    public void initCacheDir() {
        final File dir = new File(sAPP_CACHE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 将CacheItem从磁盘读取出来
     * 
     * @param key
     *            关键字
     * @return
     */
    synchronized CacheItem getFromCache(final String key) {
        CacheItem cacheItem = null;
        Object findItem = CommonUtils.restoreObject(sAPP_CACHE_PATH + key);
        if (findItem != null) {
            cacheItem = (CacheItem) findItem;
        }

        // 缓存不存在
        if (cacheItem == null)
            return null;

        if (System.currentTimeMillis() > cacheItem.getTimeStamp()) {
            File f = new File(sAPP_CACHE_PATH + key);
            if (f.exists()) {
                f.delete();
            }
            return null;
        }

        return cacheItem;
    }

    /**
     * 将CacheItem缓存到磁盘
     * 
     * @param item
     * @return 是否缓存，True：缓存成功，False：不能缓存
     */

    synchronized boolean putIntoCache(final CacheItem item) {
        CommonUtils.saveObject(sAPP_CACHE_PATH + item.getKey(), item);
        return true;
    }

    /**
     * 清除缓存文件
     */
    public synchronized void clearAllData() {
        File file = new File(sAPP_CACHE_PATH);
        File[]  files = file.listFiles();
        if (files != null) {
            for (final File file2 : files) {
                file2.delete();
            }
        }
    }
}
