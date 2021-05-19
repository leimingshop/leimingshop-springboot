/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 敏感词过滤工具类 DFA算法
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/7 11:32
 **/
@Slf4j
public class SensitiveWordUtil {

    /**
     * 敏感词库
     */
    public static Map sensitiveWordMap = null;

    /**
     * 只过滤最小敏感词 比如：孙悟空,孙悟 过滤：我是孙悟空   过滤结果:我是[孙悟]空
     */
    public static int minMatchTYpe = 1;

    /**
     * 过滤所有敏感词 比如：孙悟空,孙悟 过滤：我是孙悟空   过滤结果:我是[孙悟空]
     */
    public static int maxMatchType = 2;

    /**
     * 过滤替换的标识
     */
    public static String replaceCharStar = "*";

    public SensitiveWordUtil() {

    }

    /**
     * 构造方法
     *
     * @param sensitiveWordMap: 敏感词map集合
     * @date 2020/5/7 11:38
     * @author lixiangx@leimingtech.com
     **/
    public static void initKeyWord(Map sensitiveWordMap) {
        sensitiveWordMap = sensitiveWordMap;
    }

    /**
     * 初始化敏感词 将敏感词用DFA算法的原理封装到敏感词库中，敏感词库采用HashMap保存
     *
     * @param sensitiveWords: 敏感词集合
     * @return 敏感词map(每个词单独拆分 eg : 敏感词白空马 处理完成为 { 白 = { 龙 = { 马 = { isEnd = 1 }, isEnd = 0 }, isEnd = 0 } })
     * @date 2020/5/7 11:39
     * @author lixiangx@leimingtech.com
     **/
    public static void initKeyWord(Set<String> sensitiveWords) {
        try {
            // 从敏感词集合对象中取出敏感词并封装到Set集合中
            Set<String> keyWordSet = new HashSet<String>();
            for (String s : sensitiveWords) {
                keyWordSet.add(s.trim());
            }
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            log.error("处理敏感词:【{}】出现异常:{}", sensitiveWords, e);
        }
    }

    /**
     * 封装敏感词库 设置标志位isEnd = 1，否则设置标志位isEnd = 0
     *
     * @param keyWordSet
     */
    private static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        // 初始化HashMap对象并控制容器的大小
        sensitiveWordMap = new HashMap(keyWordSet.size());
        // 敏感词
        String key = null;
        // 用来按照相应的格式保存敏感词库数据
        Map nowMap = null;
        // 用来辅助构建敏感词库
        Map<String, String> newWorMap = null;
        // 使用一个迭代器来循环敏感词集合
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            // 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                // 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
                char keyChar = key.charAt(i);

                // 判断这个字是否存在于敏感词库中
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                // 如果该字是当前敏感词的最后一个字，则标识为结尾字
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
                //log.info("{}封装敏感词库过程："+sensitiveWordMap);
            }
//            log.debug("{}查看敏感词库数据:", sensitiveWordMap);
        }
    }

    /**
     * 敏感词库敏感词数量
     *
     * @return 敏感词数量
     */
    public static int getWordSize() {
        return sensitiveWordMap == null ? 0 : sensitiveWordMap.size();
    }


    /**
     * 是否包含敏感词
     *
     * @param txt:       待检查文字
     * @param matchType: 敏感词过滤类型
     * @return 是否包含敏感词结果
     * @date 2020/5/7 11:45
     * @author lixiangx@leimingtech.com
     **/
    public static boolean isContaintSensitiveWord(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取敏感词内容
     *
     * @param txt:       待处理的敏感词
     * @param matchType: 敏感词处理类型 1只过滤最小敏感词  2过滤所有敏感词
     * @return 敏感词内容
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();

        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {
                // 将检测出的敏感词保存到集合中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感词
     *
     * @param txt:         待处理的敏感词
     * @param matchType:   敏感词处理类型   1只过滤最小敏感词  2过滤所有敏感词
     * @param replaceChar: 替换成什么字段
     * @return 处理完成的敏感词
     */
    public static String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感词内容
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查敏感词数量
     *
     * @param txt:        待处理的文本
     * @param beginIndex: 文本文字的位数
     * @param matchType:  敏感词处理类型 1只过滤最小敏感词  2过滤所有敏感词
     * @return 包含敏感词的数量
     */
    public static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;
        // 记录敏感词数量
        int matchFlag = 0;
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            // 判断该字是否存在于敏感词库中
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                // 判断是否是敏感词的结尾字，如果是结尾字则判断是否继续检测
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    // 判断过滤类型，如果是小过滤则跳出循环，否则继续循环
                    if (SensitiveWordUtil.minMatchTYpe == matchType) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }


    public static void main(String[] args) {
        Set<String> list = new HashSet<>();
        list.add("孙悟空");
        list.add("沙悟净");
        list.add("猪八");
        list.add("猪八戒");
        list.add("唐僧");
        list.add("白龙马");
        SensitiveWordUtil.initKeyWord(list);
        String string = "唐僧让沙悟净告诉孙悟空把猪八戒的白龙马卖掉";
        String a = SensitiveWordUtil.replaceSensitiveWord(string, 1, "*");
        System.out.println("敏感词库的数量" + SensitiveWordUtil.getWordSize());
        System.out.println(a);

        Set<String> sensitiveWord = SensitiveWordUtil.getSensitiveWord(string, SensitiveWordUtil.minMatchTYpe);
        System.out.println(sensitiveWord);

    }
}