package com.steve_md.smartmkulima.utils.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.pos.sdk.printer.param.BitmapPrintItemParam;
import com.pos.sdk.printer.param.MultipleTextPrintItemParam;
import com.pos.sdk.printer.param.PrintItemAlign;
import com.pos.sdk.printer.param.TextPrintItemParam;

import java.io.ByteArrayOutputStream;

public class SlipModelUtils {
    private static final String TAG = "SlipModelUtils";
    private final static String FONT_PATH = "mnt/sdcard/addTestData.ttf";
    private final static String LETTER_FONT_PATH = "mnt/sdcard/NanumGothicCoding.ttf";

    public MultipleTextPrintItemParam createMultipleTextPrintItemParam(float[] scales, TextPrintItemParam[] list) {
        MultipleTextPrintItemParam param = new MultipleTextPrintItemParam(scales, list);
        return param;
    }

    public MultipleTextPrintItemParam addLeftAndRightItem(String left, String right, float[] scales) {
        return addLeftAndRightItem(left, right, -1, -1, scales);
    }


    public MultipleTextPrintItemParam addCenterAndRightItem(String center, String right) {
        float[] scales = new float[]{2, 1};
        TextPrintItemParam leftItem = addTextOnePrint(center, false, -1, PrintItemAlign.RIGHT);
        TextPrintItemParam rightItem = addTextOnePrint(right, false, -1, PrintItemAlign.RIGHT);
        TextPrintItemParam[] list = new TextPrintItemParam[]{leftItem, rightItem};
        return createMultipleTextPrintItemParam(scales, list);
    }


    public TextPrintItemParam addTextOnePrint(String content, boolean isBold, int textSize, PrintItemAlign align) {
        TextPrintItemParam param = new TextPrintItemParam();
//        param.setTypefacePath("mnt/sdcard/Caveat.ttf");
        param.setContent(content);
        if (textSize > 0) {
            param.setTextSize(textSize);
        } else {
            param.setTextSize(16);
            param.setScaleHeight(1.6f);
            param.setScaleWidth(1.2f);
        }
        param.setItemAlign(align);
        param.setBold(isBold);
        return param;
    }

    public MultipleTextPrintItemParam addFourItem(String first, String second, String third, String fourth, float[] scales) {
        if (scales == null) {
            scales = new float[]{1, 3.5f, 1, 1.5f};
        }
        TextPrintItemParam firstItem = addTextOnePrint(first, false, -1, PrintItemAlign.LEFT);
        TextPrintItemParam secondItem = addTextOnePrint(second, false, -1, PrintItemAlign.CENTER);
        TextPrintItemParam thirdItem = addTextOnePrint(third, false, -1, PrintItemAlign.CENTER);
        TextPrintItemParam fourthItem = addTextOnePrint(fourth, false, -1, PrintItemAlign.RIGHT);
        TextPrintItemParam[] list = new TextPrintItemParam[]{firstItem, secondItem, thirdItem, fourthItem};
        return new MultipleTextPrintItemParam(scales, list);
    }


    public BitmapPrintItemParam addBitmapAsset(Context context, String nameInAssets) {
        BitmapPrintItemParam printItemParam = new BitmapPrintItemParam();
        try {
            printItemParam.setBitmap(DataUtil.inputStream2byte(context.getAssets().open("image/" + nameInAssets)));
            printItemParam.setHeight(100);
            printItemParam.setWidth(300);
            printItemParam.setItemAlign(PrintItemAlign.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printItemParam;
    }

    public BitmapPrintItemParam addBitmapItem(Context context, Bitmap bitmap) {
        BitmapPrintItemParam printItemParam = new BitmapPrintItemParam();
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            printItemParam.setBitmap(byteArray);
            printItemParam.setHeight(100);
            printItemParam.setWidth(300);
            printItemParam.setItemAlign(PrintItemAlign.CENTER);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printItemParam;
    }

    public MultipleTextPrintItemParam addLeftAndRightItem(String left, String right, int leftSize, int rightSize, float[] scales) {
        if (scales == null) {
            scales = new float[]{1, 1};
        }
        TextPrintItemParam leftItem = addTextOnePrint(left, false, leftSize, PrintItemAlign.LEFT);
        TextPrintItemParam rightItem = addTextOnePrint(right, false, rightSize, PrintItemAlign.RIGHT);
//        rightItem.setLetterAndNumberTypefacePath("mnt/sdcard/msyhc.ttf");
        TextPrintItemParam[] list = new TextPrintItemParam[]{leftItem, rightItem};
        return createMultipleTextPrintItemParam(scales, list);
    }

}
