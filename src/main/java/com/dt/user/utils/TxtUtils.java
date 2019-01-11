package com.dt.user.utils;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class TxtUtils {

    /**
     * 写入TXT文件
     */
    public static void writeFileTxt(List<List<String>> skuNoList, String path, String fileName) {
        try {
            String filePath = path + "NO" + fileName;
            File writeName = new File(filePath); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
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
