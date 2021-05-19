/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.search.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.search.HotWordDao;
import com.leimingtech.dto.hotword.HotWordDTO;
import com.leimingtech.dto.hotword.HotWordSaveDTO;
import com.leimingtech.dto.hotword.HotWordUpdateDTO;
import com.leimingtech.entity.search.HotWordEntity;
import com.leimingtech.service.search.HotWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 热词搜索
 *
 * @author qin
 * @email 49636174@qq.com
 * @since 1.0.0 2019-12-3
 */
@Slf4j
@Service

public class HotWordServiceImpl extends CrudServiceImpl<HotWordDao, HotWordEntity, HotWordDTO> implements HotWordService {

    @Resource
    private HotWordDao hotWordDao;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 分页
     *
     * @param params
     * @author qin
     */

    @Override
    public PageData<HotWordDTO> page(@RequestParam Map<String, Object> params) {

        IPage<HotWordEntity> page = baseDao.selectPage(
                getPage(params, "sort desc,create_date", false),
                getWrapper(params)
        );

        return getPageData(page, HotWordDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @author qin
     */
    @Override
    public QueryWrapper<HotWordEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<HotWordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        if (params.get("hotWord") != null) {
            wrapper.like("hot_word", params.get("hotWord").toString());
        }

        return wrapper;
    }

    /**
     * 添加搜索词
     *
     * @param hotWord
     * @author qin
     */

    @Override
    public void saveHotWord(@RequestBody HotWordSaveDTO hotWord) {
        HotWordEntity hotWordEntity = ConvertUtils.sourceToTarget(hotWord, HotWordEntity.class);
        hotWordDao.insert(hotWordEntity);
    }

    /**
     * 根据ID删除热搜
     *
     * @param ids
     */

    @Override
    public void deleteById(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 编辑回显
     *
     * @param id
     */

    @Override
    public HotWordDTO selectById(Long id) {
        return super.get(id);
    }

    /**
     * 修改热搜实体
     *
     * @param hotWord
     */

    @Override
    public void updateHotWord(@RequestBody HotWordUpdateDTO hotWord) {
        HotWordEntity hotWordEntity = ConvertUtils.sourceToTarget(hotWord, HotWordEntity.class);
        hotWordDao.updateById(hotWordEntity);
    }

    /**
     * 取出前15条热词数据
     *
     * @return 热词集合
     * @date 2019/12/18 10:13
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public List<HotWordDTO> findHotFixedQuantity() {
        List<HotWordEntity> hotWordEntities = baseDao.selectList(Wrappers.<HotWordEntity>lambdaQuery()
                .orderByDesc(HotWordEntity::getSort, HotWordEntity::getCreateDate)
                .last("limit 15"));
        List<HotWordDTO> hotWordDTOList = ConvertUtils.sourceToTarget(hotWordEntities, HotWordDTO.class);
        return hotWordDTOList;
    }

    /**
     * 从Redis中获取热词信息
     *
     * @return 热词集合
     * @date 2019/12/18 10:22
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public List<HotWordDTO> findDataRedis() {
        Object obj = redisUtils.get(RedisKeys.getFrontHotWords());
        if (obj == null) {
            List<HotWordDTO> hotWordDTOList = this.findHotFixedQuantity();
            if (CollectionUtils.isNotEmpty(hotWordDTOList)) {
                redisUtils.set(RedisKeys.getFrontHotWords(), hotWordDTOList);
            }
        }
        return (List<HotWordDTO>) obj;
    }
}
