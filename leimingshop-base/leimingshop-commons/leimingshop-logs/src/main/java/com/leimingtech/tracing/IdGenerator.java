//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.tracing;

import java.util.UUID;

public class IdGenerator {
    public IdGenerator() {
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
