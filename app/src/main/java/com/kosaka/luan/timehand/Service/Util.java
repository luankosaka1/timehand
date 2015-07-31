package com.kosaka.luan.timehand.Service;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Util {

    public static String PHONE_NUMBER = "";

    public static String getSIM(Context Context) {
        TelephonyManager telemamanger = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getSimSerialNumber();
        String getSimNumber = telemamanger.getLine1Number();

        if (getSimNumber.isEmpty()) {
            return getSimSerialNumber;
        }

        return getSimNumber;
    }

    public static String getIMEI(Context Context) {
        TelephonyManager telephonyManager = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getTelefone(Context Context) {
        TelephonyManager tMgr = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();

        if (phoneNumber.isEmpty()) {
            // instancia variavel global do telefone
            if (!PHONE_NUMBER.isEmpty()) {
                phoneNumber = PHONE_NUMBER;
            }
        }

        return phoneNumber;
    }

}
