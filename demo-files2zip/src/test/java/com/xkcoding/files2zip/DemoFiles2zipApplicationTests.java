package com.xkcoding.files2zip;


import cn.hutool.core.util.ZipUtil;
import com.xkcoding.files2zip.util.FilesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = DemoFiles2zipApplication.class)
public class DemoFiles2zipApplicationTests {

    @Test
    public void compressDirectoriesTest() throws IOException {
        List<String> directoryPaths = new ArrayList<>();
        directoryPaths.add("D:\\工作\\test");
        String zipFilePath = "D:\\工作\\compressDirectoriesTest.zip";
        FilesUtils.compressFiles2Zip(directoryPaths, zipFilePath, false);
    }

    @Test
    public void compressFilesTest() throws IOException {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("D:\\工作\\教程\\FreeMarker\\FreeMarker教程-熊狮虎.pdf");
        filePaths.add("D:\\工作\\面试准备\\jvm相关知识.docx");
        String zipFilePath = "D:\\工作\\compressFilesTest.zip";
        FilesUtils.compressFiles2Zip(filePaths, zipFilePath, false);
    }

    @Test
    public void compressFiles2ZipTest() throws IOException {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("D:\\file test");
        String zipFilePath = "D:\\compressFilesTest01.zip";
        FilesUtils.compressFiles2Zip(filePaths, zipFilePath, true);
    }


    @Test
    public void compressFiles(){
//        String zipFilePath = "D:\\compressFilesTest01.zip";
//        ZipUtil.zip("D:\\file test",zipFilePath,true);

        String zipFilePath = "D:\\工作\\compressFilesTest.zip";
        ZipUtil.zip("D:\\工作\\教程\\FreeMarker\\FreeMarker教程-熊狮虎.pdf", zipFilePath, true);
    }
}
