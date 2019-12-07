package com.dragon.boot.mail.tools;

import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @ClassName FileTool
 * @Author pengl
 * @Date 2019-06-04 15:06
 * @Description TODO
 * @Version 1.0
 */
public class FileTool {

    public static File multipartFileToFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        InputStream ins = file.getInputStream();
        File toFile = new File(file.getOriginalFilename());
        inputStreamToFile(ins, toFile);
        ins.close();
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            IoUtil.copy(ins, os);
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
