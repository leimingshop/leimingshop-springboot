/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.stopword;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.stopword.AddStopWordDTO;
import com.leimingtech.dto.stopword.StopWordDTO;
import com.leimingtech.dto.stopword.UpdateStopWordDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */

public interface StopWordService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     * @author chengqian
     * @date 2019-08-21
     */

    PageData<StopWordDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 获取结果集
     *
     * @param params
     * @return
     * @author chengqian
     * @date 2019-08-21
     */

    List<StopWordDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     * @author chengqian
     * @date 2019-08-21
     */

    StopWordDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     * @author chengqian
     * @date 2019-08-21
     */

    void save(@RequestBody AddStopWordDTO dto);


    /**
     * 修改
     *
     * @param dto
     * @author chengqian
     * @date 2019-08-21
     */

    void update(@RequestBody UpdateStopWordDTO dto);


    /**
     * 删除
     *
     * @param ids
     * @author chengqian
     * @date 2019-08-21
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导入禁用词
     */
    Boolean importForbiddenInfo(@RequestPart("file") MultipartFile files);

    /**
     * 禁用词处理
     *
     * @param text: 待处理文本
     * @return 处理完文本
     * @date 2020/5/7 14:29
     * @author lixiangx@leimingtech.com
     **/

    String replaceStopWord(@RequestParam(value = "text") String text);
}
