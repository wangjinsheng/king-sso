/**
 * Copyright (c) 2013, Qunar.com Inc. All rights reserved.
 * 
 * File: MiscUtils.java
 * Created on: Oct 15, 2013
 */
package com.king.sso.core.util;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * jinsheng
 */
public final class MiscUtils {
    public static final String EMPTY = "";
    private MiscUtils() {
    }

    public static boolean equals(final Object o1, final Object o2) {
        return (o1 == o2 || (o1 != null && o2 != null && o1.equals(o2)));
    }

    public static boolean isEmpty(final String str) {
        return (str == null || str.length() == 0);
    }

    public static <T> T ifNull(final T original, final T replacement) {
        return (original == null ? replacement : original);
    }

    public static String ifNullOrEmpty(final String original, final String replacement) {
        return (original == null || original.length() == 0 ? replacement : original);
    }

    public static String emptyIfNull(final String original) {
        return (original == null ? "" : original);
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> emptyIfNull(final List<E> original) {
        return (original == null ? Collections.EMPTY_LIST : original);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> original) {
        return (original == null ? Collections.EMPTY_MAP : original);
    }

    @SuppressWarnings("unchecked")
    public static <E> Set<E> emptyIfNull(final Set<E> original) {
        return (original == null ? Collections.EMPTY_SET : original);
    }

    public static String toLowerCase(String var0) {
        return var0.toLowerCase(Locale.US);
    }

    public static String trimToEmpty(String str) {
        return str == null?"":str.trim();
    }
    public static boolean isNotEmpty(String cs) {
        return !isEmpty(cs);
    }
}
