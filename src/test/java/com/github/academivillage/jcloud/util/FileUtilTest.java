package com.github.academivillage.jcloud.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FileUtilTest {

    private static Stream<Arguments> provideGetFileNameData() {
        return Stream.of(
                arguments("https://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "447c0da60.zip"),
                arguments("http://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "447c0da60.zip"),
                arguments("httpss://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23"),
                arguments("/storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23")
        );
    }

    private static Stream<Arguments> provideGetParentDirData() {
        return Stream.of(
                arguments("https://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "https://storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184"),
                arguments("/storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184/447c0da60.zip?GoogleAccessId=dynamikax-dev@appspot.gserviceaccount.com&Expires=2022-04-23T07:19:47.23", "/storage.googleapis.com/dynamikax-storage-eu/upload/temporary/images/21-103330-184")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetParentDirData")
    void getParentDirPath(String path, String expectedDirPath) {
        assertThat(FileUtil.getParentDirPath(path)).isEqualTo(expectedDirPath);
    }

    @ParameterizedTest
    @MethodSource("provideGetFileNameData")
    void getFileName(String path, String expectedFileName) {
        assertThat(FileUtil.getFileName(path)).isEqualTo(expectedFileName);
    }
}
