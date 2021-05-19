//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.system;

import java.lang.management.ManagementFactory;

public class ApplicationPid {
    private final String pid;

    public ApplicationPid() {
        this.pid = this.getPid();
    }

    protected ApplicationPid(String pid) {
        this.pid = pid;
    }

    public static void main(String[] args) {
        System.out.println(new ApplicationPid().toString());
    }

    private String getPid() {
        try {
            String ex = ManagementFactory.getRuntimeMXBean().getName();
            return ex.split("@")[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.pid == null ? "???" : this.pid;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.pid);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ? true : (obj != null && obj instanceof ApplicationPid ? ObjectUtils.nullSafeEquals(this.pid, ((ApplicationPid) obj).pid) : false);
    }
}
