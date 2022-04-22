package com.github.academivillage.jcloud.util;

import lombok.val;
import lombok.var;

public class FileUtil {

    public static String getParentDirPath(String path, char separator) {
        int lastSlashIdx = path.lastIndexOf(separator);
        return lastSlashIdx <= 0 ? "" : path.substring(0, lastSlashIdx);
    }

    public static String getParentDirPath(String path) {
        return getParentDirPath(path, '/');
    }

    public static String getFileName(String storagePath) {
        int lastSlashIdx = storagePath.lastIndexOf('/');
        var fileName     = lastSlashIdx <= 0 ? storagePath : storagePath.substring(lastSlashIdx + 1);
        int qmIndex      = fileName.indexOf('?');

        val isUrl             = storagePath.startsWith("https://") || storagePath.startsWith("http://");
        val questionMarkFound = qmIndex > 0;

        return !questionMarkFound || !isUrl ? fileName : fileName.substring(0, qmIndex);
    }

    public static void main(String[] args) {
        var str = "https://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/20220421-103330-184/1a1157f1e4d4d96447c0da6000403dd5.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23&access_token=ya29.c.b0AXv0zTPx0wY7O28rW1PA4JQ9s6FLyQCn51pxvOWeItAtGyYy-0BqD5Dam-UI5fAjZAmfomXUlqUua0Qvy3Su_3_ka_3NY7khWtSf2JpqZZdteVaQfnCANOL";
        System.out.println(FileUtil.getFileName(str));
    }
}
