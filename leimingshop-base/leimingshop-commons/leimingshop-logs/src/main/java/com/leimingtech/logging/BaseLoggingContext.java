//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.logging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseLoggingContext implements Map<String, String> {
    public static final String KEY_RESPONSE_TIME = "responseTime";
    public static final String KEY_BO_NO = "OrderNo";
    public static final String KEY_MOBILE = "phoneNo";
    public static final String KEY_ID_CARD_NO = "IdNo";
    public static final String KEY_ACCOUNT_NO = "Acctid";
    public static final String KEY_CARD_NO = "CardNo";
    public static final String KEY_NAME = "Name";
    public static final String KEY_CARD_TYPE = "CardType";
    public static final String KEY_PRODUCT_NO = "ProductNo";
    public static final String KEY_SEQ_NO = "SeqNo";
    public static final String KEY_SEQ_ID = "SEQ-ID";
    public static final String KEY_BODY_ID = "BASY-ID";
    private Map<String, String> internal = new HashMap();

    public BaseLoggingContext() {
    }

    public static String getSeqNo() {
        return "SeqNo";
    }

    public static String getSeqId() {
        return "SEQ-ID";
    }

    public static String getBodyId() {
        return "BASY-ID";
    }

    public String setCardNo(String CardNo) {
        return this.put("CardNo", CardNo);
    }

    public String getCardNo() {
        return this.get("CardNo");
    }

    public String setName(String Name) {
        return this.put("Name", Name);
    }

    public String getName() {
        return this.get("Name");
    }

    public String setCardType(String CardType) {
        return this.put("CardType", CardType);
    }

    public String getCardType() {
        return this.get("CardType");
    }

    public String setProductNo(String ProductNo) {
        return this.put("ProductNo", ProductNo);
    }

    public String getProductNo() {
        return this.get("ProductNo");
    }

    public String setResponseTime(String responseTime) {
        return this.put("responseTime", responseTime);
    }

    public String getResponseTime() {
        return this.get("responseTime");
    }

    public String getBoNo() {
        return this.get("OrderNo");
    }

    public String setBoNo(String boNo) {
        return this.put("OrderNo", boNo);
    }

    public String getMobile() {
        return this.get("phoneNo");
    }

    public String setMobile(String mobile) {
        return this.put("phoneNo", mobile);
    }

    public String getIdCardNo() {
        return this.get("IdNo");
    }

    public String setIdCardNo(String idCardNo) {
        return this.put("IdNo", idCardNo);
    }

    public String getAccountNo() {
        return this.get("Acctid");
    }

    public String setAccountNo(String accountNo) {
        return this.put("Acctid", accountNo);
    }

    public String setSeqNo(String seqNo) {
        return this.put("SeqNo", seqNo);
    }

    public String setSeqId(String seqId) {
        return this.put("SEQ-ID", seqId);
    }

    public String setBodyId(String bodyId) {
        return this.put("BASY-ID", bodyId);
    }

    @Override
    public int size() {
        return this.internal.size();
    }

    @Override
    public boolean isEmpty() {
        return this.internal.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.internal.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return (String) this.internal.get(key);
    }

    @Override
    public String put(String key, String value) {
        return (String) this.internal.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return (String) this.internal.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        this.internal.putAll(m);
    }

    @Override
    public void clear() {
        this.internal.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.internal.keySet();
    }

    @Override
    public Collection<String> values() {
        return this.internal.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return this.internal.entrySet();
    }
}
