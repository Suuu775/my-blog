package org.example.blog.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class PathUtils {

    public static String generaterFilePath(String originalFilename) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());

        String uuid = UUID.randomUUID().toString().replace("-","");
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index);

        return date + "/" +uuid + ext;

    }
}
