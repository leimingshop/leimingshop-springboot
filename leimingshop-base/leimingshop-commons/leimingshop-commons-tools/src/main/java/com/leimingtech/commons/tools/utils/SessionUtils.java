/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Session 工具类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/8/8 22:01
 **/
public class SessionUtils {

    /**
     * 重置sessionid，原session中的数据自动转存到新session中  * @param request
     */
    public static void reGenerateSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        //首先将原session中的数据转移至一临时map中
        Map<String, Object> tempMap = new HashMap();
        Enumeration<String> sessionNames = session.getAttributeNames();
        while (sessionNames.hasMoreElements()) {
            String sessionName = sessionNames.nextElement();
            tempMap.put(sessionName, session.getAttribute(sessionName));
        }
        //注销原session，为的是重置sessionId      session.invalidate();
        // 将临时map中的数据转移至新session
        session = request.getSession();
        for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
