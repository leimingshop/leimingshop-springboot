/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import com.github.binarywang.java.emoji.EmojiConverter;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/9 17:21
 * @email 1609973595@qq.com
 */
public class EmojiUtils {

    private static EmojiConverter emojiConverter = EmojiConverter.getInstance();

    /**
     * 将emojiStr转为 带有表情的字符
     *
     * @param emojiStr
     * @return
     */
    public static String emojiConverter2Unicode(String emojiStr) {
        String result = null;
        if (null != emojiStr && emojiStr.length() > 0) {
            result = emojiConverter.toUnicode(emojiStr);
        }
        return result;
    }

    /**
     * 带有表情的字符串Emoji转义为关键字，类似:xiao:。
     *
     * @param str
     * @return
     */
    public static String emojiConverter2Alias(String str) {
        String result = null;
        if (null != str && str.length() > 0) {
            result = emojiConverter.toAlias(str);
        }
        return result;
    }

    /**
     * 带有表情的字符串Emoji转义为unicode，类似&#128582;。
     *
     * @param emoji
     * @return
     */
    public static String emojiConverter2Html(String emoji) {
        String result = null;
        if (null != emoji && emoji.length() > 0) {
            result = emojiConverter.toHtml(emoji);
        }
        return result;
    }

}
