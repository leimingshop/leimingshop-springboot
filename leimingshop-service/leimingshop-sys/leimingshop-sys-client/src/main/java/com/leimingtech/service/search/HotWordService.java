/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.search;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.hotword.HotWordDTO;
import com.leimingtech.dto.hotword.HotWordSaveDTO;
import com.leimingtech.dto.hotword.HotWordUpdateDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 文章管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */

public interface HotWordService {
    /**
     * 添加搜索词
     *
     * @return
     */

    void saveHotWord(HotWordSaveDTO hotWord);

    /**
     * 删除热搜
     *
     * @return
     */

    void deleteById(Long[] ids);

    /**
     * 编辑回显
     *
     * @param id
     */

    HotWordDTO selectById(Long id);

    /**
     * 编辑热搜
     *
     * @return
     */

    void updateHotWord(HotWordUpdateDTO hotWord);

    /**
     * 分页搜索
     *
     * @return
     */

    PageData<HotWordDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 取出前15条热词数据
     *
     * @return 热词集合
     * @date 2019/12/18 10:13
     * @author lixiangx@leimingtech.com
     **/

    List<HotWordDTO> findHotFixedQuantity();

    /**
     * 从Redis中获取热词信息
     *
     * @return 热词集合
     * @date 2019/12/18 10:22
     * @author lixiangx@leimingtech.com
     **/

    List<HotWordDTO> findDataRedis();
}
