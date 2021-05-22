package com.xkcoding.files2zip.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class FilesUtils {
    /**
     * @param directoryPaths   文件夹目录集合
     * @param zipFilePath      压缩后文件地址
     * @param keepDirStructure 是否保持原来文件的目录结构(ture：是，false:否)
     * @return
     * @throws IOException
     */
    public static boolean compressDirectories(List<String> directoryPaths, String zipFilePath, boolean keepDirStructure) throws IOException {
        if (CollectionUtils.isEmpty(directoryPaths)) {
            return false;
        }
        if (StringUtils.isEmpty(zipFilePath)) {
            return false;
        }
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        byte[] buf = new byte[1024];
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        for (int i = 0; i < directoryPaths.size(); i++) {
            File directory = new File(directoryPaths.get(i));
            if (!directory.exists() || !directory.isDirectory()) {
                continue;
            }
            File[] files = directory.listFiles();
            for (int n = 0; n < files.length; n++) {
                compressFile(keepDirStructure, buf, zos, files[n]);
            }
        }
        zos.close();
        return true;
    }


    /**
     * @param filePaths        文件目录集合
     * @param zipFilePath      压缩后文件地址
     * @param keepDirStructure 是否保持原来文件的目录结构(ture：是，false:否)
     * @return
     * @throws IOException
     */
    public static boolean compressFiles(List<String> filePaths, String zipFilePath, boolean keepDirStructure) throws IOException {
        if (CollectionUtils.isEmpty(filePaths)) {
            return false;
        }
        if (StringUtils.isEmpty(zipFilePath)) {
            return false;
        }
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        byte[] buf = new byte[1024];
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            compressFile(keepDirStructure, buf, zos, file);
        }
        zos.close();
        return true;
    }

    private static void compressFile(boolean keepDirStructure, byte[] buf, ZipOutputStream zos, File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        String zipEntryFilePath = keepDirStructure ? file.getAbsolutePath() : file.getName();
        zos.putNextEntry(new ZipEntry(zipEntryFilePath));
        int len;
        while ((len = fis.read(buf)) > 0) {
            zos.write(buf, 0, len);
        }
        zos.closeEntry();
        fis.close();
    }

    /**
     * 解压缩压缩文件
     * @param zipFilePath 压缩文件目录位置
     * @param outPutPath 解压缩后存放位置(keepDirStructure为true时不生效)
     * @param keepDirStructure 是否保持原来目录结构(ture：是，false:否)
     * @return
     * @throws IOException
     */
    public static boolean unZipFiles(String zipFilePath, String outPutPath, boolean keepDirStructure) throws IOException {
        if (StringUtils.isEmpty(zipFilePath)){
            return false;
        }
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()){
            return false;
        }
        if (StringUtils.isEmpty(outPutPath)){
            return false;
        }
        //读取压缩文件
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
        //zip文件实体类
        ZipEntry entry;
        //遍历压缩文件内部 文件数量
        while ((entry = in.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String filePath = entry.getName();
                if (!keepDirStructure) {
                    filePath = outPutPath + filePath;
                }
                //文件输出流
                FileOutputStream out = new FileOutputStream(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭`
                bos.close();
                out.close();
            }
        }
        return true;
    }

    /**
     * zip压缩文 解压
     */
    public static void main(String[] args) throws IOException {
        List<String> directoryPaths = new ArrayList<>();
        directoryPaths.add("D:\\工作\\证件照\\af34014a423011e9b6b900163e009f5548807white");
        String zipFilePath = "D:\\工作\\compressDirectoriesTest.zip";
        FilesUtils.compressDirectories(directoryPaths, zipFilePath, false);
    }
}
