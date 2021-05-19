/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.feedback.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.feedback.FeedbackDao;
import com.leimingtech.dto.feedback.FeedbackDTO;
import com.leimingtech.dto.feedback.SaveFeedbackDTO;
import com.leimingtech.entity.feedback.FeedbackEntity;
import com.leimingtech.service.feedback.FeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Service
@Transactional

public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackDao, FeedbackEntity> implements FeedbackService {
    /**
     * 反馈分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<FeedbackDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FeedbackEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FeedbackDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<FeedbackDTO> list(@RequestParam Map<String, Object> params) {
        List<FeedbackEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FeedbackDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<FeedbackEntity> getWrapper(Map<String, Object> params) {
        String memberId = (String) params.get("memberId");
        String showFlag = (String) params.get("showFlag");
        String memberName = (String) params.get("memberName");

        String mobileNumber = (String) params.get("mobileNumber");
        String feedbackType = (String) params.get("feedbackType");

        String feedbackSource = (String) params.get("feedbackSource");
        String disposeStatus = (String) params.get("disposeStatus");
        String operator = (String) params.get("operator");
        String disposeStartDate = (String) params.get("disposeStartDate");
        String disposeEndDate = (String) params.get("disposeEndDate");
        String createStartDate = (String) params.get("createStartDate");
        String createEndDate = (String) params.get("createEndDate");
        if (StringUtils.isNotBlank(disposeStartDate)) {
            disposeStartDate = disposeStartDate + " 00:00:00";
        }
        if (StringUtils.isNotBlank(disposeEndDate)) {
            disposeEndDate = disposeEndDate + " 23:59:59";
        }

        if (StringUtils.isNotBlank(createStartDate)) {
            createStartDate = createStartDate + " 00:00:00";
        }
        if (StringUtils.isNotBlank(createEndDate)) {
            createEndDate = createEndDate + " 23:59:59";
        }
        String decideType = (String) params.get("decideType");

        QueryWrapper<FeedbackEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        wrapper.eq(StringUtils.isNotBlank(decideType), "decide_type", decideType);
        wrapper.eq(StringUtils.isNotBlank(showFlag), "user_del_flag", showFlag);
        wrapper.eq(StringUtils.isNotBlank(memberName), "member_name", memberName);
        wrapper.eq(StringUtils.isNotBlank(mobileNumber), "mobile_number", mobileNumber);
        wrapper.eq(StringUtils.isNotBlank(feedbackType), "feedback_type", feedbackType);
        wrapper.eq(StringUtils.isNotBlank(feedbackSource), "feedback_source", feedbackSource);
        wrapper.eq(StringUtils.isNotBlank(disposeStatus), "dispose_status", disposeStatus);
        wrapper.ge(StringUtils.isNotBlank(disposeStartDate), "dispose_date", disposeStartDate);
        wrapper.le(StringUtils.isNotBlank(disposeEndDate), "dispose_date", disposeEndDate);
        wrapper.ge(StringUtils.isNotBlank(createStartDate), "create_date", createStartDate);
        wrapper.le(StringUtils.isNotBlank(createEndDate), "create_date", createEndDate);
        wrapper.like(StringUtils.isNotBlank(operator), "operator", operator);
        return wrapper;
    }

    /**
     * 反馈详情
     *
     * @param id
     * @return
     */
    @Override

    public FeedbackDTO get(Long id) {
        FeedbackEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FeedbackDTO.class);
    }

    /**
     * 保存反馈信息
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Long save(@RequestBody SaveFeedbackDTO dto) {
        FeedbackEntity entity = ConvertUtils.sourceToTarget(dto, FeedbackEntity.class);

        insert(entity);
        return entity.getId();
    }

    /**
     * 更新
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody FeedbackDTO dto) {
        FeedbackEntity entity = ConvertUtils.sourceToTarget(dto, FeedbackEntity.class);

        updateById(entity);
    }

    /**
     * 用户删除反馈信息
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("id", ids);
        FeedbackEntity entity = new FeedbackEntity();
        entity.setUserDelFlag(DelFlagEnum.DEL.value());
        baseDao.update(entity, updateWrapper);

    }

}