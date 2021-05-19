/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.weibo;


import java.util.Map;

/**
 * 微博服务接口API
 *
 * @author zhangtai
 * @date 2020/4/9 18:01
 */

public interface WeiboService {

    /**
     * 功能描述: 根据微博code验证是否绑定微博登录并获取登录信息
     *
     * @param code 微博code
     * @return map登录结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> weiboLogin(String code);

    /**
     * 功能描述: 用户手机号绑定微博登录信息
     *
     * @param code   code
     * @param mobile 手机号
     * @return map
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> weiboBindMobile(String code, String mobile);
}
