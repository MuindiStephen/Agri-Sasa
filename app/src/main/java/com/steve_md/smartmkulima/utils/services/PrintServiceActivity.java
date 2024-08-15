package com.steve_md.smartmkulima.utils.services;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.SdkResult;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.nexgo.oaf.apiv3.device.printer.Printer;
import com.pos.sdk.printer.PrinterDevice;
import com.steve_md.smartmkulima.model.DeviceType;

import java.util.HashMap;

import vpos.apipackage.PosApiHelper;

public class PrintServiceActivity extends AppCompatActivity implements OnPrintListener {

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private int voltage_level;
    private int BatteryV;



    /**
     * The Preferences.
     */
    SharedPreferences preferences;
    /**
     * The Sp.
     */
    SharedPreferences sp;
    /**
     * The Editor.
     */
    SharedPreferences.Editor editor;

    /**
     * The constant SEPARATOR_LINE.
     */
    public static final String SEPARATOR_LINE = "---------------------------";
    private static final String CENTER_PREFIX = "CENTRE:";

    /**
     * The Ret.
     */
    int ret = -1;
    private boolean m_bThreadFinished = true;

    /**
     * The Handler.
     */
    Handler handler = new Handler();

    //private Pos pos;

    /**
     * The Is working.
     */
    int IsWorking = 0;

    /**
     * The Print service activity tv.
     */
// will enable user to enter any text to be printed
    TextView PrintServiceActivityTV;

    /**
     * The Finish activity on print.
     */
    boolean finishActivityOnPrint = true;
    /**
     * The Encoded image.
     */
    String encodedImage;
    /**
     * The Byte array image.
     */
    byte[] byteArrayImage;
    /**
     * The Bitmap.
     */
    Bitmap bitmap,
    /**
     * The Qr bitmap.
     */
    QRBitmap;
    /**
     * The Print button.
     */
    MaterialButton printButton;

    /**
     * The Params map.
     */
    HashMap<String, String> paramsMap;
    /**
     * The Print text.
     */
    String[] printText;

    private PosApiHelper posApiHelper;
    private String receiptTitle = "";

    private DeviceType deviceType = null;
    private DeviceEngine deviceEngine;

    private Printer printer;
    private PrinterDevice k9printer;







    private String TAG = "PrintServiceActivity";
    public static String HIRE_FARM_EQUIPMENT = "HIRE FARM EQUIPMENT";
    public static String PURCHASE_FARM_INPUTS = "PURCHASE FARM INPUTS";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPrintResult(int resultCode) {
        switch (resultCode) {
            case SdkResult.Success:
                Log.d(TAG, "Printer job finished successfully!");
                break;
            case SdkResult.Printer_Print_Fail:
                Log.e(TAG, "Printer Failed: " + resultCode);
                break;
            case SdkResult.Printer_Busy:
                Log.e(TAG, "Printer is Busy: " + resultCode);
                break;
            case SdkResult.Printer_PaperLack:
                Log.e(TAG, "Printer is out of paper: " + resultCode);
                break;
            case SdkResult.Printer_Fault:
                Log.e(TAG, "Printer fault: " + resultCode);
                break;
            case SdkResult.Printer_TooHot:
                Log.e(TAG, "Printer temperature is too hot: " + resultCode);
                break;
            case SdkResult.Printer_UnFinished:
                Log.w(TAG, "Printer job is unfinished: " + resultCode);
                break;
            case SdkResult.Printer_Other_Error:
                Log.e(TAG, "Printer Other_Error: " + resultCode);
                break;
            default:
                Log.e(TAG, "Generic Fail Error: " + resultCode);
                break;
        }
    }


}
