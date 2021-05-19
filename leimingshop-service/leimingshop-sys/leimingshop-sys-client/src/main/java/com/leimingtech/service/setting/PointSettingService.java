/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.setting;


import com.leimingtech.dto.setting.reward.RewardSettingDTO;
import com.leimingtech.dto.setting.reward.point.PointUserRuleSetting;
import com.leimingtech.dto.setting.reward.rule.MoreRuleSettingDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */

public interface PointSettingService {


    /**
     * 保存积分设置通用方法
     *
     * @param jsonStr: 奖励设置JSON
     * @param name:    奖励设置数据库字段name
     * @date 2019/12/23 14:22
     * @author lixiangx@leimingtech.com
     **/
    @Transactional(rollbackFor = Exception.class)
    void saveUniversal(@RequestParam("jsonStr") String jsonStr,
                       @RequestParam("name") String name);

    /**
     * 保存积分设置通用方法
     *
     * @param jsonStr: 奖励设置JSON
     * @param name:    奖励设置数据库字段name
     * @date 2019/12/23 14:22
     * @author lixiangx@leimingtech.com
     **/
    @Transactional(rollbackFor = Exception.class)
    boolean updateUniversal(@RequestParam("jsonStr") String jsonStr,
                            @RequestParam("name") String name);

    /**
     * 根据名字查询设置信息
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/

    RewardSettingDTO findRewardByName(@RequestParam("name") String name);

    /**
     * 根据名字查询更多规则信息
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/

    MoreRuleSettingDTO findRuleByName(@RequestParam("name") String name);

    /**
     * 根据名字查询用户使用说明
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/

    PointUserRuleSetting findUserRuleByName(@RequestParam("name") String name);

    /**
     * 获取积分设置JSON（Redis）
     *
     * @param name: 积分设置表中name字段
     * @return 积分设置json
     * @date 2019/12/23 17:21
     * @author lixiangx@leimingtech.com
     **/

    String findFromRedis(String name);
}