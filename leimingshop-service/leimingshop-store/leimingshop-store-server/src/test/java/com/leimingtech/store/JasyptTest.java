/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.store;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author lixiang
 * @version V1.0
 * @date 2019/8/1 9:31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class JasyptTest {

    @Resource

    private StringEncryptor encryptor;

    @Test
    public void getPass() {
        String name = encryptor.encrypt("root");
        String password = encryptor.encrypt("Leimingtech1205");
        System.out.println(name);
        System.out.println(password);
    }
}
