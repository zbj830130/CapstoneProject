package com.bojinzhang.android.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangbojin on 24/05/17.
 */

public class FileUtil {
    public static void savePicture(Bitmap bitmap, String picName, String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File pic = new File(dir, picName);

        if (pic.exists()) {
            dir.delete();
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(pic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getPircure(String picPath) {
        String pictureName = picPath;
        Bitmap bitmap = null;

        try {
            FileInputStream fis = new FileInputStream(pictureName);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
