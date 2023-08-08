package com.xzl.csdn.service;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @author gll
 * 2020/1/8 18:13
 */
public abstract class AbsTokenService {
    Map<String, String> cache = new HashMap<>(4);

    String getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(calendar.getTime());
    }

    String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    String getTokenKey(String tokenSuffix) {
        return getTokenKeyPrefix() + tokenSuffix;
    }

    String getTokenFromCache() {
        return cache.get(getTokenKey(getCurrentDate()));
    }

    void removeLastDayToken() {
        cache.remove(getTokenKey(getLastDate()));
    }

    public void resetTokenTemplate() {
        cache.remove(getTokenKey(getCurrentDate()));
    }

    void cacheToken(String token) {
        cache.put(getTokenKey(getCurrentDate()), token);
    }


    public String getTokenTemplate() {
        String token = getTokenFromCache();
        Lock lock = getLock();
        if (Strings.isNullOrEmpty(token)) {
            try {
                lock.lock();
                //移除前一天的token
                removeLastDayToken();
                token = getTokenFromRemote();
                if (!Strings.isNullOrEmpty(token)) {
                    cacheToken(token);
                }
            } finally {
                lock.unlock();
            }

        }
        return token;
    }

    /**
     * 从远程获取token
     *
     * @return
     */
    protected abstract String getTokenFromRemote();

    /**
     * 获取token前缀
     *
     * @return
     */
    protected abstract String getTokenKeyPrefix();

    /**
     * 获取锁
     *
     * @return
     */
    protected abstract Lock getLock();
}
