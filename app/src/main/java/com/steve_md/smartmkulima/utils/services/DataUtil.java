package com.steve_md.smartmkulima.utils.services;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtil {

    private static String fingerprintDeviceName = "";
    public static byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /** RGB图片转YUV420数据
     * 宽、高不能为奇数
     * @param pixels 图片像素集合
     * @param width 宽
     * @param height 高
     * @return */
    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        //yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //屏蔽ARGB的透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                //像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                //套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                //调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                //赋值
                yuv[i * width + j] = (byte) y;
                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                yuv[len + +(i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    /*
     * 获取位图的YUV数据
     */
    public static byte[] getYUVByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // byte[] data = convertColorToByte(pixels);
        byte[] data = rgb2YCbCr420(pixels, width, height);

        return data;
    }

    public String getFingerprintDeviceName() {
        return fingerprintDeviceName;
    }

    public static void setFingerprintDeviceName(String mFingerprintDeviceName) {
        fingerprintDeviceName = mFingerprintDeviceName;
    }
}

///**
// * author：huangmin
// * time：3/17/21
// * describe：
// */
//public class DataUtil {
//    public static byte[] inputStream2byte(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] buff = new byte[100];
//        int rc = 0;
//        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
//            byteArrayOutputStream.write(buff, 0, rc);
//        }
//        return byteArrayOutputStream.toByteArray();
//    }
//
//    /** RGB图片转YUV420数据
//     * 宽、高不能为奇数
//     * @param pixels 图片像素集合
//     * @param width 宽
//     * @param height 高
//     * @return */
//    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
//        int len = width * height;
//        //yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
//        byte[] yuv = new byte[len * 3 / 2];
//        int y, u, v;
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                //屏蔽ARGB的透明度值
//                int rgb = pixels[i * width + j] & 0x00FFFFFF;
//                //像素的颜色顺序为bgr，移位运算。
//                int r = rgb & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = (rgb >> 16) & 0xFF;
//                //套用公式
//                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
//                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
//                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
//                //调整
//                y = y < 16 ? 16 : (y > 255 ? 255 : y);
//                u = u < 0 ? 0 : (u > 255 ? 255 : u);
//                v = v < 0 ? 0 : (v > 255 ? 255 : v);
//                //赋值
//                yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + +(i >> 1) * width + (j & ~1) + 1] = (byte) v;
//            }
//        }
//        return yuv;
//    }
//
//    /*
//     * 获取位图的YUV数据
//     */
//    public static byte[] getYUVByBitmap(Bitmap bitmap) {
//        if (bitmap == null) {
//            return null;
//        }
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int size = width * height;
//
//        int pixels[] = new int[size];
//        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//
//        // byte[] data = convertColorToByte(pixels);
//        byte[] data = rgb2YCbCr420(pixels, width, height);
//
//        return data;
//    }
//
//}

