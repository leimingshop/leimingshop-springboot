/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.comment.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.article.CmsArticleDao;
import com.leimingtech.modules.dao.comment.CmsCommentDao;
import com.leimingtech.modules.dto.comment.CmsCommentDTO;
import com.leimingtech.modules.dto.comment.CmsCommentFrontPageDTO;
import com.leimingtech.modules.dto.comment.CmsCommentSaveDTO;
import com.leimingtech.modules.dto.comment.CmsCommentStatusUpdateDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.remind.CmsRemindMqDTO;
import com.leimingtech.modules.entity.comment.CmsCommentEntity;
import com.leimingtech.modules.enums.comment.CommentErrorCodeEnum;
import com.leimingtech.modules.enums.remind.RemindTypeEnum;
import com.leimingtech.modules.service.comment.CmsCommentService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.stopword.StopWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 评论管理 CmsCommentServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsCommentServiceImpl extends BaseServiceImpl<CmsCommentDao, CmsCommentEntity> implements CmsCommentService {

    @Resource
    private MemberService memberService;

    @Resource
    private CmsArticleDao cmsArticleDao;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private StopWordService stopWordService;

    /**
     * 资讯/圈子评论后台分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsCommentDTO> commentList(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsCommentEntity> page = new Page<>(pageNum, limit);
        List<CmsCommentDTO> commentList = baseDao.selectCommentList(page, params);
        if (!commentList.isEmpty()) {
            commentList.forEach(x -> {
                //给每条评论加头像和用户名
                MemberDTO memberDTO = memberService.getById(x.getArticleCreaterId());
                if (memberDTO != null) {
                    x.setArticleCreator(memberDTO.getNickName());
                }
            });
        }
        return new PageData<>(commentList, page.getTotal());
    }

    /**
     * 删除评论
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id -> {
            CmsCommentEntity comment = baseDao.selectById(id);
            if (comment.getArticleId() != null) {
                //更新评论数量
                cmsArticleDao.deleteArticleCommentNum(comment.getArticleId(), id);
            }
            //逻辑删除
            List<Long> idlist = cmsArticleDao.selectArticleCommentIds(id);
            baseDao.deleteBatchIds(idlist);
        });
    }

    /**
     * 评论状态修改
     *
     * @param dto
     */
    @Override

    public void statusUpdate(@RequestBody CmsCommentStatusUpdateDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsCommentEntity cmsCommentEntity = new CmsCommentEntity();
        cmsCommentEntity.setId(dto.getId());
        cmsCommentEntity.setStatus(dto.getCode());
        cmsCommentEntity.setUpdateDate(date);
        cmsCommentEntity.setUpdater(userName);
        updateById(cmsCommentEntity);
    }

    /**
     * 资讯/圈子前台评论分页查询(文章详情页评论)
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsCommentFrontPageDTO> commentFrontFirstPage(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Long memberId = Long.parseLong(params.get("memberId").toString());
        Page<CmsCommentFrontPageDTO> page = new Page<>(pageNum, limit);
        List<CmsCommentFrontPageDTO> commentList = baseDao.selectCommentFrontFirstList(page, params);
        if (!commentList.isEmpty()) {
            commentList.forEach(x -> {
                //判断是否为该用户发布
                x.setMemberFlag(memberId.equals(x.getCommentCreaterId()) ? 1 : 0);
                //给每条评论加头像和用户名
                MemberDTO memberDTO = memberService.getById(x.getCommentCreaterId());
                if (memberDTO != null) {
                    x.setMemberAvatar(memberDTO.getMemberAvatar());
                    x.setNickName(memberDTO.getNickName());
                }
                //所有二级回复
                List<CmsCommentFrontPageDTO> commentListDto = baseDao.selectCommentSecond(1, x.getId());
                //二级回复条数
                x.setCommentSecondNum(commentListDto.size());
                if (!commentListDto.isEmpty()) {
                    //一条二级回复
                    CmsCommentFrontPageDTO comment = commentListDto.get(0);
                    //给二级回复评论加头像和用户名
                    memberDTO = memberService.getById(comment.getCommentCreaterId());
                    if (memberDTO != null) {
                        comment.setMemberAvatar(memberDTO.getMemberAvatar());
                        comment.setNickName(memberDTO.getNickName());
                    }
                    //给二级回复评论对象加用户名
                    memberDTO = memberService.getById(comment.getCommentedId());
                    if (memberDTO != null) {
                        comment.setCommentedName(memberDTO.getNickName());
                    }
                    x.setCommentFront(comment);
                }

            });
        }
        return new PageData<>(commentList, page.getTotal());
    }

    /**
     * 资讯/圈子前台评论分页查询(二级评论页)
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsCommentFrontPageDTO> commentFrontSecondPage(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Long memberId = Long.parseLong(params.get("memberId").toString());
        Page<CmsCommentEntity> page = new Page<>(pageNum, limit);
        List<CmsCommentFrontPageDTO> commentList = baseDao.selectCommentFrontSecondList(page, params);
        if (!commentList.isEmpty()) {
            commentList.forEach(x -> {
                //判断是否为该用户发布
                x.setMemberFlag(memberId.equals(x.getCommentCreaterId()) ? 1 : 0);
                //给每条评论加头像和用户名
                MemberDTO memberDTO = memberService.getById(x.getCommentCreaterId());
                if (memberDTO != null) {
                    x.setMemberAvatar(memberDTO.getMemberAvatar());
                    x.setNickName(memberDTO.getNickName());
                }
                //给二级回复评论对象加用户名
                memberDTO = memberService.getById(x.getCommentedId());
                if (memberDTO != null) {
                    x.setCommentedName(memberDTO.getNickName());
                }

            });
        }
        return new PageData<>(commentList, page.getTotal());
    }

    /**
     * 新增评论
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override

    public void save(@RequestBody CmsCommentSaveDTO dto) {
        Date date = new Date();
        //评论内容表情处理
        CmsCommentEntity entity = ConvertUtils.sourceToTarget(dto, CmsCommentEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        //更新评论数量
        cmsArticleDao.updateArticleCommentNum(dto.getArticleId());
        // 处理评论内容
        entity.setCommentContent(stopWordService.replaceStopWord(entity.getCommentContent()));
        insert(entity);
        if (StringUtils.isNotBlank(entity.getRemind())) {
            CmsRemindMqDTO cmsRemindMqDTO = new CmsRemindMqDTO();
            cmsRemindMqDTO.setRemindType(RemindTypeEnum.COMMENT_CODE.value());
            cmsRemindMqDTO.setRemindTypeId(entity.getId());
            cmsRemindMqDTO.setMemberIdList(entity.getRemind());
            cmsRemindMqDTO.setCreatorId(entity.getCommentCreaterId());
            String message = JSONObject.toJSONString(cmsRemindMqDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE, message);
        }
    }

    /**
     * 删除评论(前台)
     *
     * @param ids 这个方法现在没有使用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Map<String, Object> deleteFront(@RequestParam(value = "ids") Long[] ids, @RequestParam(value = "memberId") Long memberId) {
        Map<String, Object> res = new HashMap<>(10);
        Arrays.asList(ids).forEach(id -> {
            CmsCommentEntity comment = baseDao.selectById(id);
            if (comment == null || !comment.getCommentCreaterId().equals(memberId)) {
                res.put("code", CommentErrorCodeEnum.DELETE_CLASS_FAIL.value());
                res.put("msg", "删除失败");
            } else {
                //更新评论数量
                cmsArticleDao.cancelArticleCommentNum(comment.getArticleId());
            }
        });
        Object code = res.get("code");
        if (code == null) {
            //逻辑删除
            baseDao.deleteBatchIds(Arrays.asList(ids));
        }
        return res;
    }

}
