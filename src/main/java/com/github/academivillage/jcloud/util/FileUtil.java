package com.github.academivillage.jcloud.util;

public class FileUtil {

    public static String getParentDirPath(String path, char separator) {
        int lastSlashIdx = path.lastIndexOf(separator);
        return lastSlashIdx <= 0 ? "" : path.substring(0, lastSlashIdx);
    }

    public static String getParentDirPath(String path) {
        return getParentDirPath(path, '/');
    }

    public static String getFileName(String storagePath) {
        int lastSlashIdx = storagePath.lastIndexOf("/");
        return lastSlashIdx <= 0 ? storagePath : storagePath.substring(lastSlashIdx);
    }
}
