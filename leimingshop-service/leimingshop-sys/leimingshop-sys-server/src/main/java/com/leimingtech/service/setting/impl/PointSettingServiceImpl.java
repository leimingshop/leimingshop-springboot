/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.setting.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.dao.setting.PointSettingDao;
import com.leimingtech.dto.setting.reward.RewardSettingDTO;
import com.leimingtech.dto.setting.reward.point.PointUserRuleSetting;
import com.leimingtech.dto.setting.reward.rule.MoreRuleSettingDTO;
import com.leimingtech.entity.setting.PointSettingEntity;
import com.leimingtech.service.setting.PointSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Service
@Transactional

public class PointSettingServiceImpl extends BaseServiceImpl<PointSettingDao, PointSettingEntity> implements PointSettingService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 保存积分设置通用方法
     *
     * @param jsonStr: 奖励设置JSON
     * @param name:    奖励设置数据库字段name
     * @date 2019/12/23 14:22
     * @author lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void saveUniversal(@RequestParam("jsonStr") String jsonStr,
                              @RequestParam("name") String name) {
        PointSettingEntity entity = new PointSettingEntity();
        entity.setValue(jsonStr);
        entity.setName(name);
        insert(entity);
    }


    /**
     * 保存积分设置通用方法
     *
     * @param jsonStr: 奖励设置JSON
     * @param name:    奖励设置数据库字段name
     * @date 2019/12/23 14:22
     * @author lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public boolean updateUniversal(@RequestParam("jsonStr") String jsonStr,
                                   @RequestParam("name") String name) {
        return super.update(Wrappers.<PointSettingEntity>lambdaUpdate()
                .set(PointSettingEntity::getValue, jsonStr)
                .eq(PointSettingEntity::getName, name));
    }

    /**
     * 根据名字查询设置信息
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public RewardSettingDTO findRewardByName(@RequestParam("name") String name) {
        PointSettingEntity settingEntity = Optional.ofNullable(baseDao.selectOne(Wrappers.<PointSettingEntity>lambdaQuery()
                .eq(PointSettingEntity::getName, name))).orElse(new PointSettingEntity());
        return JSONObject.parseObject(settingEntity.getValue(), RewardSettingDTO.class);
    }

    /**
     * 根据名字查询更多规则信息
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public MoreRuleSettingDTO findRuleByName(@RequestParam("name") String name) {
        PointSettingEntity settingEntity = Optional.ofNullable(baseDao.selectOne(Wrappers.<PointSettingEntity>lambdaQuery()
                .eq(PointSettingEntity::getName, name))).orElse(new PointSettingEntity());
        return JSONObject.parseObject(settingEntity.getValue(), MoreRuleSettingDTO.class);
    }

    /**
     * 根据名字查询用户使用说明
     *
     * @param name: 设置对应的key
     * @return 设置JSON
     * @date 2019/12/23 14:49
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public PointUserRuleSetting findUserRuleByName(@RequestParam("name") String name) {
        PointSettingEntity settingEntity = Optional.ofNullable(baseDao.selectOne(Wrappers.<PointSettingEntity>lambdaQuery()
                .eq(PointSettingEntity::getName, name))).orElse(new PointSettingEntity());
        return JSONObject.parseObject(settingEntity.getValue(), PointUserRuleSetting.class);
    }

    /**
     * 获取积分设置JSON（Redis）
     *
     * @param name: 积分设置表中name字段
     * @return 积分设置json
     * @date 2019/12/23 17:21
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public String findFromRedis(String name) {

        // 从Redis中获取
        Object obj = redisUtils.get(RedisKeys.getPointSettingKey(name));
        if (obj != null) {
            return obj.toString();
        }

        // Redis中不存在直接查询数据库并放置Redis
        PointSettingEntity settingEntity = Optional.ofNullable(baseDao.selectOne(Wrappers.<PointSettingEntity>lambdaQuery()
                .eq(PointSettingEntity::getName, name))).orElse(new PointSettingEntity());
        redisUtils.set(RedisKeys.getPointSettingKey(name), settingEntity.getValue());
        return settingEntity.getValue();
    }
}
