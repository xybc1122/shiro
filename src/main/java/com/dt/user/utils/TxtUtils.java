package com.dt.user.utils;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class TxtUtils {

    /**
     * 写入TXT文件
     */
    public static void writeFileTxt(List<List<String>> skuNoList, String path, String uuidName) {
        try {
            FileUtils.mkdirFile(path);
            String filePath = path + uuidName;
            File myFile = new File(filePath);
            myFile.createNewFile();
            try (FileWriter writer = new FileWriter(myFile);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                for (int i = 0; i < skuNoList.size(); i++) {
                    List<String> c = skuNoList.get(i);
                    for (int j = 0; j < c.size(); j++) {
                        out.write(c.get(j) + "\r\n");
                    }
                }
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
