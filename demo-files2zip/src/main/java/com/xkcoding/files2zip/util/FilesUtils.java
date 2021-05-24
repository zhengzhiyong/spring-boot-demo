package com.xkcoding.files2zip.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author laobiao
 * @date 2021年5月24日 16点23分
 * @desc 文件压缩
 */
public final class FilesUtils {
    /**
     * @param compressFileDirectories 文件夹目录和文件地址目录
     * @param zipFilePath             压缩后文件地址
     * @param keepDirStructure        是否保持原来文件的目录结构(ture：是，false:否)
     * @return
     * @throws IOException
     */
    public static boolean compressFiles2Zip(List<String> compressFileDirectories, String zipFilePath, boolean keepDirStructure) throws IOException {
        if (CollectionUtils.isEmpty(compressFileDirectories)) {
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
        compressFileOrDirectory(compressFileDirectories, keepDirStructure, buf, zos);
        zos.close();
        return true;
    }

    /**
     * 循环处理文件夹或文件
     *
     * @param compressFileDirectories
     * @param keepDirStructure
     * @param buf
     * @param zos
     * @throws IOException
     */
    private static void compressFileOrDirectory(List<String> compressFileDirectories, boolean keepDirStructure, byte[] buf, ZipOutputStream zos) throws IOException {
        for (int i = 0; i < compressFileDirectories.size(); i++) {
            String fileOrDirectoryPath = compressFileDirectories.get(i);
            //判断路径为文件或文件夹
            File fileDirectory = new File(fileOrDirectoryPath);
            if (!fileDirectory.exists()) {
                continue;
            }
            if (!fileDirectory.isDirectory()) {
                compressFile(keepDirStructure, buf, zos, fileDirectory);
                continue;
            }
            File[] files = fileDirectory.listFiles();
            if (null == files || files.length == 0) {
                continue;
            }
            List<String> filePaths = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
            compressFileOrDirectory(filePaths, keepDirStructure, buf, zos);
        }
    }

    /**
     * 压缩单个文件
     *
     * @param keepDirStructure
     * @param buf
     * @param zos
     * @param file
     * @throws IOException
     */
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
     *
     * @param zipFilePath      压缩文件目录位置
     * @param outPutPath       解压缩后存放位置(keepDirStructure为true时不生效)
     * @param keepDirStructure 是否保持原来目录结构(ture：是，false:否)
     * @return
     * @throws IOException
     */
    public static boolean unZipFiles(String zipFilePath, String outPutPath, boolean keepDirStructure) throws IOException {
        if (StringUtils.isEmpty(zipFilePath)) {
            return false;
        }
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        if (StringUtils.isEmpty(outPutPath)) {
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
}
