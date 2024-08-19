package com.steve_md.smartmkulima.utils.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Config {
    /**
     * The constant MSKEY.
     */
    public static String MSKEY = null;
    /**
     * The constant PINKEY.
     */
    public static String PINKEY = null;
    /**
     * The constant DEBUG_MODE.
     */
    public static boolean DEBUG_MODE = false; // true = test, false = live
    /**
     * The constant APP_VERSION_HASHSUM.
     */
    public static final String APP_VERSION_HASHSUM = "0123456789"; // same for similar app version


    /**
     * The constant isTempAdminAuth.
     */
    public static boolean isTempAdminAuth = false; // let user see settings page

    /**
     * The constant CASH_PAYMENT.
     */
    public static final String QR_CODE = "qr_code";

    /**
     * The constant CASH_PAYMENT.
     */
    public static final String CASH_PAYMENT = "CASH";
    /**
     * The constant CARD_PAYMENT.
     */
    public static final String CARD_PAYMENT = "CARD";
    /**
     * The constant DIRECT_SCHOOL_FEES.
     */
    public static final String DIRECT_SCHOOL_FEES = "DIRECT";
    /**
     * The constant REPRINT_RECEIPT_REQUEST.
     */
    public static final String REPRINT_RECEIPT_REQUEST = "REPRINT_RECEIPT";
    /**
     * The constant INDIRECT_SCHOOL_FEES.
     */
    public static final String INDIRECT_SCHOOL_FEES = "INDIRECT";
    /**
     * The constant PAYMENT_METHOD.
     */
    public static String PAYMENT_METHOD = null;
    /**
     * The constant SCHOOL_FEES_TYPE.
     */
    public static String SCHOOL_FEES_TYPE = null;
    /**
     * The constant AGENT_NAME.
     */
    public static String AGENT_NAME = null;

    public static String OPERATOR_ID=null;
    /**
     * The constant TERMINAL_NO.
     */
    public static String TERMINAL_NO = null;
    /**
     * The constant banks.
     */
    public static List<SpinnerItem> banks = new ArrayList<>();
    /**
     * The constant bankDetailName.
     */
    public static List<SpinnerItem> bankDetailName = new ArrayList<>();

    /**
     * The constant PERMISSIONS_CAMERA.
     */
    public static final int PERMISSIONS_CAMERA = 54;
    /**
     * The constant PERMISSIONS_PHOTO_GALLERY.
     */
    public static final int PERMISSIONS_PHOTO_GALLERY = 69;

    /**
     * The constant SAO_ACCOUNTS.
     */
    public static final String SAO_ACCOUNTS = "soaAccounts";
    /**
     * The constant BRANCHES_LOOK_UP.
     */
    public static final String BRANCHES_LOOK_UP = "lookUpBranches";
    /**
     * The constant SCHOOLS_LOOK_UP.
     */
    public static final String SCHOOLS_LOOK_UP = "lookUpSchools";

    /**
     * The constant PHOTO_GALLERY_REQUEST.
     */
    public static final int PHOTO_GALLERY_REQUEST = 10;
    /**
     * The constant CAMERA_REQUEST.
     */
    public static final int CAMERA_REQUEST = 20;
    /**
     * The constant AGENT_BANK.
     */
    public static String AGENT_BANK;
    /**
     * The constant AGENT_BANK_STREET.
     */
    public static String AGENT_BANK_STREET;
    /**
     * The constant LATEST_APK_VERSION.
     */
    public static String LATEST_APK_VERSION = "v1.0.0";
    /**
     * The constant AUTH_TYPE.
     */
    public static String AUTH_TYPE;
    /**
     * The constant AGENT_FINGERPRINT.
     */
    public static String AGENT_FINGERPRINT;

    public static String POS_OPERATOR_NAME;

    /**
     * The constant CURRENCY_CODE.
     */
    public static final String CURRENCY_CODE = "KES";

    /**
     * Gets all banks.
     *
     * @return the all banks
     */
    public static List<SpinnerItem> getAllBanks() {
        //  return banks;
        return bankDetailName;
    }

    /**
     * The constant TITLE_EXTRA.
     */
    public static final String TITLE_EXTRA = "com.eclectics.dss.agencybanking.title_extra";
    /**
     * The constant SELECTED_MENU_EXTRA.
     */
    public static final String SELECTED_MENU_EXTRA = "com.eclectics.dss.agencybanking.menu_extra";
    /**
     * The constant REGISTRATION_TYPE_EXTRA.
     */
    public static final String REGISTRATION_TYPE_EXTRA = "com.eclectics.dss.agencybanking.registration_type_extra";
    /**
     * The constant PAN_EXTRA.
     */
    public static final String PAN_EXTRA = "com.eclectics.dss.agencybanking.pan";
    /**
     * The constant TRACK2_EXTRA.
     */
//public static final String ACCOUNT_NAME_EXTRA ="com.eclectics.dss.agencybanking.accountName";
    public static final String TRACK2_EXTRA = "com.eclectics.dss.agencybanking.track2Data";
    /**
     * The constant TOPUP_CARD.
     */
    public static final String TOPUP_CARD = "com.eclectics.dss.agencybanking.topup_card";
    /**
     * The constant BILL_CARD.
     */
    public static final String BILL_CARD = "com.eclectics.dss.agencybanking.bill_card";


    /**
     * The type Request.
     */
    public static class REQUEST {
        /**
         * The constant FAO.
         */
        public static final String FAO = "";
        /**
         * The constant ACTIVATION.
         */
        public static final String ACTIVATION = "";
        /**
         * The constant CASH_DEPOSIT.
         */
        public static final String CASH_DEPOSIT = "CDP";
        /**
         * The constant CASH_WITHDRAWAL.
         */
        public static final String CASH_WITHDRAWAL = "CWD";
        /**
         * The constant CASH_WITHDRAWAL_BIOMETRIC.
         */
        public static final String CASH_WITHDRAWAL_BIOMETRIC = "CWD_BIOMETRIC";
        /**
         * The constant CARDLESS_FULFILLMENT.
         */
        public static final String CARDLESS_FULFILLMENT = "CLF";
        /**
         * The constant CARDLESS_FULFILLMENT_DEPOSIT.
         */
        public static final String CARDLESS_FULFILLMENT_DEPOSIT = "CLFD";
        /**
         * The constant AIRTIME_TOPUP_CASH.
         */
        public static final String AIRTIME_TOPUP_CASH = "TPCS";
        /**
         * The constant AIRTIME_TOPUP_CARD.
         */
        public static final String AIRTIME_TOPUP_CARD = "TPCR";
        /**
         * The constant BILL_PAYMENT_CASH.
         */
        public static final String BILL_PAYMENT_CASH = "BPCS";
        /**
         * The constant BILL_PAYMENT_CARD.
         */
        public static final String BILL_PAYMENT_CARD = "BPCR";
        /**
         * The constant COLLECTIONS_CASH.
         */
        public static final String COLLECTIONS_CASH = "COCS";
        /**
         * The constant COLLECTIONS_CARD.
         */
        public static final String COLLECTIONS_CARD = "COCR";
        /**
         * The constant BALANCE_INQUIRY.
         */
        public static final String BALANCE_INQUIRY = "BI";
        /**
         * The constant MINISTATEMENT.
         */
        public static final String MINISTATEMENT = "MINI";
        /**
         * The constant AGENT_FLOAT_TRANSFER.
         */
        public static final String AGENT_FLOAT_TRANSFER = "FLOAT_PURCHASE";
        /**
         * The constant SUMMARY_REPORTS.
         */
        public static final String SUMMARY_REPORTS = "";
        /**
         * The constant AGENT_FLOAT_REQUEST.
         */
        public static final String AGENT_FLOAT_REQUEST = "FLOAT_REQUEST";


        public static final String FUNDS_TRANSFER= "FT";


        /**
         * The constant FEES_PAYMENT.
         */
        public static final String FEES_PAYMENT_CARD= "FEES_PAYMENT_CARD";
    }

    /**
     * The constant JWT_TOKEN.
     */
    public static String JWT_TOKEN = "";
    /**
     * The constant TRANSACTION_TYPE.
     */
    public static String TRANSACTION_TYPE = "transactionType";
    /**
     * The constant AGENT_ID.
     */
    public static String AGENT_ID = "";
    /**
     * The constant AGENT_CODE.
     */
    public static String AGENT_CODE = "";
    /**
     * The constant bankDetails.
     */
    public static JSONArray bankDetails = null;
    /**
     * The constant PASSWORD.
     */
    public static String PASSWORD = "";
    /**
     * The constant billAccount.
     */
    public static String billAccount = "";
    /**
     * The constant billPan.
     */
    public static String billPan = "";
    /**
     * The constant billRegion.
     */
    public static String billRegion = "";
    /**
     * The constant billPhone.
     */
    public static String billPhone = "";
    /**
     * The constant billNarration.
     */
    public static String billNarration = "";
    /**
     * The constant billCustMobile.
     */
    public static String billCustMobile = "";
    /**
     * The constant billAmt.
     */
    public static String billAmt = "";
    /**
     * The constant tpPhone.
     */
    public static String tpPhone = "";
    /**
     * The constant tpAmt.
     */
    public static String tpAmt = "";
    /**
     * The constant tpcarrierName.
     */
    public static String tpcarrierName = "";
    /**
     * The constant billTrack2Data.
     */
    public static String billTrack2Data = "";
    /**
     * The constant bankCode.
     */
    public static String bankCode = "";
    /**
     * The constant cwdAmount.
     */
    public static String cwdAmount = "";
    /**
     * The constant ftAmount.
     */
    public static String ftAmount = "";
    /**
     * The constant cfAmount.
     */
    public static String cfAmount = "";
    /**
     * The constant cfphone.
     */
    public static String cfphone = "";
    /**
     * The constant cfpin.
     */
    public static String cfpin = "";
    /**
     * The constant ftAccountTo.
     */
    public static String ftAccountTo = "";
    /**
     * The constant encryptedPinBlock.
     */
    public static String encryptedPinBlock = "";
    /**
     * The constant encryptedTrack2data.
     */
    public static String encryptedTrack2data = "";
    /**
     * The constant currentKSN.
     */
    public static String currentKSN = "";
    /**
     * The constant FIRST_NAME_EXTRA.
     */
    public static final String FIRST_NAME_EXTRA = "com.eclectics.dss.agencybanking.first_name";
    /**
     * The constant LAST_NAME_EXTRA.
     */
    public static final String LAST_NAME_EXTRA = "com.eclectics.dss.agencybanking.last_name";
    /**
     * The constant MIDDLE_NAME_EXTRA.
     */
    public static final String MIDDLE_NAME_EXTRA = "com.eclectics.dss.agencybanking.middle_name";
    /**
     * The constant BANK_SORT_CODE_EXTRA.
     */
    public static final String BANK_SORT_CODE_EXTRA = "com.eclectics.dss.agencybanking.bank_sort_code";
    /**
     * The constant ACCOUNT_NO_EXTRA.
     */
    public static final String ACCOUNT_NO_EXTRA = "com.eclectics.dss.agencybanking.account_no";
    /**
     * The constant ACCOUNT_NAME_EXTRA.
     */
    public static final String ACCOUNT_NAME_EXTRA = "com.eclectics.dss.agencybanking.account_name";
    /**
     * The constant NARRATION_EXTRA.
     */
    public static final String NARRATION_EXTRA = "com.eclectics.dss.agencybanking.narration";
    /**
     * The constant TXN_REF_EXTRA.
     */
    public static final String TXN_REF_EXTRA = "com.eclectics.dss.agencybanking.trans_ref";
    /**
     * The constant MESSAGE_EXTRA.
     */
    public static final String MESSAGE_EXTRA = "com.eclectics.dss.agencybanking.message";
    /**
     * The constant AMOUNT_EXTRA.
     */
    public static final String AMOUNT_EXTRA = "com.eclectics.dss.agencybanking.amount";
    /**
     * The constant FTCHARGE_EXTRA.
     */
    public static final String FTCHARGE_EXTRA = "com.eclectics.dss.agencybanking.ftcharge";
    /**
     * The constant FTTAX_EXTRA.
     */
    public static final String FTTAX_EXTRA = "com.eclectics.dss.agencybanking.fttax";

    /**
     * The constant BALANCE_ENQUIRIES.
     */
    public static final int BALANCE_ENQUIRIES = 2;
    /**
     * The constant CASH_DEPOSIT.
     */
    public static final int CASH_DEPOSIT = 3;
    /**
     * The constant CASH_WITHDRAWAL.
     */
    public static final int CASH_WITHDRAWAL = 4;
    /**
     * The constant BILL_PAYMENT.
     */
    public static final int BILL_PAYMENT = 5;
    /**
     * The constant COLLECTIONS.
     */
    public static final int COLLECTIONS = 1;
    /**
     * The constant AIRTIME_TOPUP.
     */
    public static final int CARD_TOPUP = 6;
    /**
     * The constant REPORTS.
     */
    public static final int REPORTS = 7;
    /**
     * The constant PIN_CHANGE.
     */
    public static final int PIN_CHANGE = 8;
    /**
     * The constant LEAD_GENERATION.
     */
    public static final int LEAD_GENERATION = 17;
    /**
     * The constant MINISTATEMENT.
     */
    public static final int MINISTATEMENT = 9;
    /**
     * The constant FUNDS_TRANSFER.
     */
    public static final int FUNDS_TRANSFER = 10;
    /**
     * The constant CARDLESS_FULFILLMENT.
     */
    public static final int NATCASH = 11;
    /**
     * The constant FAO.
     */
    public static final int SAO = 12;
    /**
     * The constant ACCOUNT_ACTIVATION.
     */
    public static final int ACCOUNT_ACTIVATION = 13;
    /**
     * The constant AGENTFLOAT.
     */
    public static final int AGENTFLOAT = 14;
    /**
     * The constant REPRINT_RECEIPT.
     */
    public static final int REPRINT_RECEIPT = 15;
    /**
     * The constant CARDLESS_FULFILLMENT_DEPOSIT.
     */
    public static final int CARDLESS_FULFILLMENT_DEPOSIT = 16;


    /**
     * The constant FEES_PAYMENT.
     */
    public static final int FEES_PAYMENT= 21;

    /**
     * The constant LATEST_APK_URL.
     */
    public static String LATEST_APK_URL = "";


    public static String getBaseUrl() {
        return BASE_URL;
    }

//    public static void setBaseUrl(String baseUrl) {
//        BASE_URL =  formatURL(baseUrl);
//    }

    /**
     * The constant BASE_URL.
     */

    public static  String BASE_URL;

    /**
     * The constant RETROFIT_MEDIA_TYPE.
     */
    public static String RETROFIT_MEDIA_TYPE = "application/json; charset=utf-8";

    public static Bitmap SignatureBitmap;
    /**
     * The constant OK.
     */
    public static final int OK = 1;
    /**
     * The constant FAIL.
     */
    public static final int FAIL = 0;
    /**
     * The constant UNAUTHORIZED.
     */
    public static final int UNAUTHORIZED = 404;




    /**
     * Get absolute url.
     *
     * @param relativeUrl the relative url
     * @return absolute url
     */
    public static String RELATIVEURL = "";

    /**
     * Gets absolute url.
     *
     * @param relativeUrl the relative url
     * @return the absolute url
     */
    public static String getRelativeUrl(String relativeUrl) {
        RELATIVEURL = relativeUrl;
        return relativeUrl;
    }


    /**
     * Clear login.
     *
     * @param context the context
     */
    public static void clearLogin(Context context) {
        Config.AGENT_ID = null;
        Config.AGENT_CODE = null;
        Config.AGENT_NAME = null;
        Config.TERMINAL_NO = null;
        Config.bankDetails = null;
        Config.JWT_TOKEN = null;
        Config.bankDetails = null;
        Config.bankDetailName = new ArrayList<>();
        Config.MSKEY = null;
        Config.PINKEY = null;

        // remove keys from shared preferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove("sMasterKey");
        editor.remove("sPinKey");
        editor.commit();

        // Intent intent = new Intent(context, SignInActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // context.startActivity(intent);
      //  UserIdleTimer.getInstance(context).cancel();
    }

//    private static String formatURL(String rawURL) {
//        String yek;
//        byte[] data = NumberUtil.decodeData(rawURL);
//        yek = new String(data, StandardCharsets.UTF_8);
//        return yek;
//    }

}

