/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.group.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.group.GroupMemberDao;
import com.leimingtech.modules.dto.group.GroupMemberDTO;
import com.leimingtech.modules.entity.group.GroupMemberEntity;
import com.leimingtech.modules.service.group.GroupMemberService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼团记录用户管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Service
@Transactional

@Log4j
public class GroupMemberServiceImpl extends BaseServiceImpl<GroupMemberDao, GroupMemberEntity> implements GroupMemberService {

    @Override

    public PageData<GroupMemberDTO> page(@RequestParam Map<String, Object> params) {
        IPage<GroupMemberEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, GroupMemberDTO.class);
    }

    @Override

    public List<GroupMemberDTO> list(Map<String, Object> params) {
        List<GroupMemberEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GroupMemberDTO.class);
    }

    private QueryWrapper<GroupMemberEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String groupRecordId = (String) params.get("groupRecordId");

        QueryWrapper<GroupMemberEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(groupRecordId), "group_record_id", groupRecordId);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public GroupMemberDTO get(Long id) {
        GroupMemberEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, GroupMemberDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody GroupMemberDTO dto) {
        GroupMemberEntity entity = ConvertUtils.sourceToTarget(dto, GroupMemberEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody GroupMemberDTO dto) {
        GroupMemberEntity entity = ConvertUtils.sourceToTarget(dto, GroupMemberEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, GroupMemberEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询拼团记录id关联的会员列表
     *
     * @param id 拼团记录id
     * @return
     * @date 2020-03-30 17:39
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public List<GroupMemberDTO> recordList(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupRecordId", id.toString());
        List<GroupMemberEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GroupMemberDTO.class);
    }

    /**
     * 拼团失败删除会员记录
     *
     * @param memberId      会员id
     * @param groupRecordId 拼团记录id
     * @return
     * @date 2020-04-15 15:09
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void deleteMember(@RequestParam("memberId") Long memberId, @RequestParam("groupRecordId") Long groupRecordId) {
        log.info("移除头像");
        UpdateWrapper<GroupMemberEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("member_id", memberId)
                .eq("group_record_id", groupRecordId);
        baseDao.delete(updateWrapper);
    }


}
