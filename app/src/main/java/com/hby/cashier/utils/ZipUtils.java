package com.hby.cashier.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.hby.cashier.app.AppConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/16
 */
public class ZipUtils {

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFilesTest(File zipFile) throws IOException {

        LogUtils.i("unZipFiles   --------begin------");
        String parent = zipFile.getParent();
        File pathFile = new File(parent);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            zip = new ZipFile(zipFile, Charset.forName("GBK"));
        }
        File file = null;
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath =
                    (parent + AppConfig.TEXT_FILE_PATH + "/" + zipEntryName).replaceAll("\\\\"
                            , "/");
            //判断路径是否存在,不存在则创建文件路径
            file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
//            File outPathFile = new File(outPath);
//            if (outPathFile.exists()) {
//                continue;
//            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();

        }
        LogUtils.i("返回的解压后的文件目录" + file.getAbsolutePath());
        LogUtils.i("unZipFiles   --------end------");

        return file.getAbsolutePath();
    }

    //读取json文件
    public static String readJsonFile(String fileName) {
        StringBuilder jsonStr = new StringBuilder();
        File file = new File(fileName);
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStr.append(line);
            }
            return jsonStr.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getTextFile(String fileName) {
        return AppConfig.CASHIER_SAVE_PATH + AppConfig.ZIP_PARENT_PATH + AppConfig.TEXT_FILE_PATH + File.separator + fileName;
    }

}
