package com.dt.user.utils;

import com.dt.user.model.UserInfo;
import com.dt.user.model.UserUpload;
import com.sun.deploy.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

public class FileUtils {
    /**
     * 上传文件
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws IOException {
        BufferedOutputStream out = null;
        File targetFile = new File(filePath);
        try {
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            out = new BufferedOutputStream(new FileOutputStream(filePath + fileName));
            out.write(file);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 通过记录用户上传信息操作
     * @param siteId
     * @param shopId
     * @param fileName
     * @param saveFilePath
     * @param user
     * @return
     */
    public static UserUpload uploadOperating(Integer siteId, Integer shopId, String fileName, String saveFilePath, UserInfo user) {
        UserUpload userUpload = new UserUpload();
        //存入文件名字
        userUpload.setName(fileName);
        //存入上传时间
        userUpload.setCreateDate(new Date().getTime());
        //用户ID
        userUpload.setUid(user.getUid());
        //上传服务器路径
        userUpload.setFilePath(saveFilePath);
        //站点ID
        userUpload.setSiteId(siteId.longValue());
        //店铺ID
        userUpload.setShopId(shopId.longValue());
        return userUpload;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 下载文件
     */
    public static void downloadFile(String path, HttpServletResponse response, HttpServletRequest request) throws IOException {
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            // path是指欲下载的文件的路径。
            File downloadFile = new File(path);
            // 取得文件名。
            String filename = downloadFile.getName();
            filename = URLEncoder.encode(filename, "utf-8");
            // 取得文件的后缀名。
//           String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            // 设置response的Header
            // System.out.println("Download DocFile's ORGIN----" + request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
            response.addHeader("Content-Length", "" + downloadFile.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (toClient != null) {
                toClient.close();
            }

        }
    }
}
