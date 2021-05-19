//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.logging;

import org.springframework.util.Assert;

public abstract class AnsiOutput {
    private static final String ENCODE_JOIN = ";";
    private static final String OPERATING_SYSTEM_NAME;
    private static final String ENCODE_START = "\u001b[";
    private static final String ENCODE_END = "m";
    private static final String RESET = "0;39";
    private static AnsiOutput.Enabled enabled;
    private static Boolean consoleAvailable;
    private static Boolean ansiCapable;

    static {
        enabled = AnsiOutput.Enabled.DETECT;
        OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase();
    }

    public AnsiOutput() {
    }

    public static void setConsoleAvailable(Boolean consoleAvailable) {
        AnsiOutput.consoleAvailable = consoleAvailable;
    }

    static AnsiOutput.Enabled getEnabled() {
        return enabled;
    }

    public static String toString(Object... elements) {
        StringBuilder sb = new StringBuilder();
        if (isEnabled()) {
            buildEnabled(sb, elements);
        } else {
            buildDisabled(sb, elements);
        }

        return sb.toString();
    }

    private static void buildEnabled(StringBuilder sb, Object[] elements) {
        boolean writingAnsi = false;
        boolean containsEncoding = false;
        Object[] arr$ = elements;
        int len$ = elements.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Object element = arr$[i$];
            if (element instanceof Integer) {
                containsEncoding = true;
                if (!writingAnsi) {
                    sb.append("\u001b[");
                    writingAnsi = true;
                } else {
                    sb.append(";");
                }
            } else if (writingAnsi) {
                sb.append("m");
                writingAnsi = false;
            }

            sb.append(element);
        }

        if (containsEncoding) {
            sb.append(writingAnsi ? ";" : "\u001b[");
            sb.append("0;39");
            sb.append("m");
        }

    }

    private static void buildDisabled(StringBuilder sb, Object[] elements) {
        Object[] arr$ = elements;
        int len$ = elements.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Object element = arr$[i$];
            if (!(element instanceof Integer) && element != null) {
                sb.append(element);
            }
        }

    }

    private static boolean isEnabled() {
        if (enabled == AnsiOutput.Enabled.DETECT) {
            if (ansiCapable == null) {
                ansiCapable = Boolean.valueOf(detectIfAnsiCapable());
            }

            return ansiCapable.booleanValue();
        } else {
            return enabled == AnsiOutput.Enabled.ALWAYS;
        }
    }

    public static void setEnabled(AnsiOutput.Enabled enabled) {
        Assert.notNull(enabled, "Enabled must not be null");
        AnsiOutput.enabled = enabled;
    }

    private static boolean detectIfAnsiCapable() {
        try {
            return Boolean.FALSE.equals(consoleAvailable) ? false : (consoleAvailable == null && System.console() == null ? false : OPERATING_SYSTEM_NAME.indexOf("win") < 0);
        } catch (Exception e) {
            return false;
        }
    }

    public enum Enabled {
        DETECT,
        ALWAYS,
        NEVER;

        private Enabled() {
        }
    }
}
