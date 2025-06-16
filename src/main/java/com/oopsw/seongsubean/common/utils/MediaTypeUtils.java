package com.oopsw.seongsubean.common.utils;

import java.util.Map;

public class MediaTypeUtils {

  private static final Map<String, String> CONTENT_TYPE_MAP;

  static {

    CONTENT_TYPE_MAP = Map.ofEntries(Map.entry("jpg", "image/jpeg"),
        Map.entry("jpeg", "image/jpeg"), Map.entry("png", "image/png"),
        Map.entry("gif", "image/gif"), Map.entry("webp", "image/webp"),
        Map.entry("svg", "image/svg+xml"), Map.entry("bmp", "image/bmp"),
        Map.entry("ico", "image/x-icon"), Map.entry("pdf", "application/pdf"),
        Map.entry("txt", "text/plain"), Map.entry("json", "application/json"));
  }

  /**
   * 파일명으로부터 Content-Type 반환
   */
  public static String getContentType(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return "application/octet-stream";
    }

    String extension = getFileExtension(fileName).toLowerCase();
    return CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream");
  }

  /**
   * 파일 확장자 추출
   */
  public static String getFileExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      return "";
    }
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }

}
