package com.steve_md.smartmkulima.utils.services;

public final class AppConstants {

    private String enabledServices;
    public Boolean isSharedAgency = false;
    private String enabledMenus;

    private String mKey;
    private String sKey;

    public String getBankCaption() {
        return bankCaption;
    }

    public void setBankCaption(String bankCaption) {
        this.bankCaption = bankCaption;
    }

    private String bankCaption;

    // private static UserProfile INSTANCE = new UserProfile();
    private static volatile AppConstants INSTANCE;

    // For Singleton instantiation
    private static final Object LOCK = new Object();


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AppConstants getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new AppConstants();
                }
            }
        }
        return INSTANCE;
    }

    public String getEnabledServices() {
        return enabledServices;
    }

    public void setEnabledServices(String enabledServices) {
        this.enabledServices = enabledServices;
    }

    public String getEnabledMenus() {
        return enabledMenus;
    }

    public void setEnabledMenus(String enabledMenus) {
        this.enabledMenus = enabledMenus;
    }


    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }



}

