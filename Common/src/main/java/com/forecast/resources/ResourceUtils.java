package com.forecast.resources;

import java.text.MessageFormat;
import java.util.Locale;

import static java.util.ResourceBundle.getBundle;

public class ResourceUtils {

    private static SecurityManagerTracker SECURITY_MANAGER_INSTANCE = null;

    public static String getString(String key, String... params) {
        String localizedString = getBundle("string", new Locale("ru"), getCallerResourceOwner().getClassLoader()).getString(key);
        return parametrizeMessage(localizedString, params);
    }

    private static Class getCallerResourceOwner() {
        return getTopOuterClass(getCallerClass());
    }

    private static Class getTopOuterClass(Class c) {
        while (c.getDeclaringClass() != null) {
            c = c.getDeclaringClass();
        }
        return c;
    }

    private static Class getCallerClass() {
        try {
            SecurityManagerTracker securityManagerTracker = getSecurityManagerTracker();
            if (securityManagerTracker != null) {
                return securityManagerTracker.getClassContext()[3 + 1];
            }

            StackTraceElement[] stackTrace = getStackTrace();
            int offset = 0;
            String threadClassName = Thread.class.getName();
            while (offset < stackTrace.length) {
                if (!threadClassName.equals(stackTrace[offset].getClassName())) {
                    break;
                }
                offset++;
            }
            return Class.forName(stackTrace[1 + offset + 3].getClassName());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static SecurityManagerTracker getSecurityManagerTracker() {
        try {
            if (SECURITY_MANAGER_INSTANCE == null) {
                SECURITY_MANAGER_INSTANCE = new SecurityManagerTracker();
            }
        } catch (SecurityException ignored) {
        }
        return SECURITY_MANAGER_INSTANCE;
    }

    private static String parametrizeMessage(String message, String... params) {
        try {
            MessageFormat temp = new MessageFormat(message);
            return temp.format(params);
        } catch (Throwable e) {
            return message;
        }
    }

    private static class SecurityManagerTracker extends SecurityManager {
        @Override
        public Class[] getClassContext() {
            return super.getClassContext();
        }
    }

    private static StackTraceElement[] getStackTrace() {
        try {
            return Thread.currentThread().getStackTrace();
        } catch (SecurityException ex) {
            ex.fillInStackTrace();
            return ex.getStackTrace();
        }
    }
}
