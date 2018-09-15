package com.syswin.temail.ps.client.utils;

import com.sun.istack.internal.Nullable;

/**
 * @author 姚华成
 * @date 2018-9-14
 */
public class StringUtil {
  public static boolean isEmpty(@Nullable Object str) {
    return (str == null || "".equals(str));
  }

  public static boolean hasLength(@Nullable CharSequence str) {
    return (str != null && str.length() > 0);
  }

  public static boolean hasLength(@Nullable String str) {
    return (str != null && !str.isEmpty());
  }

  public static boolean hasText(@Nullable CharSequence str) {
    return (str != null && str.length() > 0 && containsText(str));
  }

  public static boolean hasText(@Nullable String str) {
    return (str != null && !str.isEmpty() && containsText(str));
  }

  private static boolean containsText(CharSequence str) {
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }
}
