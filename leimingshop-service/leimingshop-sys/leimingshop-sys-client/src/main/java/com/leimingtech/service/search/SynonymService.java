/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.search;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.synonym.SynonymDTO;
import com.leimingtech.dto.synonym.SynonymSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 同义词Service
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:50
 **/

public interface SynonymService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */

    PageData<SynonymDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */

    List<SynonymDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询全部同义词数据
     *
     * @return 全部同义词数据集合
     * @date 2019/12/10 10:36
     * @author lixiangx@leimingtech.com
     **/

    List<SynonymDTO> listForEs();

    /*
     * 获取实体信息
     * @param id
     * @return
     */

    SynonymDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody SynonymSaveDTO dto);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody SynonymSaveDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * <修改同义词启用停用状态>
     *
     * @param state 0:停用，1：启用
     * @param id    主键id
     * @date 2020/3/18 11:07
     * @author weixianchun
     **/

    int updateStatus(@RequestParam("state") Integer state, @RequestParam("id") Long id);

    /**
     * 功能描述  批量修改
     *
     * @param synonymSaveDTOList:
     * @author lishuo
     * @date 2020/8/11 14:54
     * @return: void
     **/

    void updateBatch(@RequestBody List<SynonymSaveDTO> synonymSaveDTOList);
}
