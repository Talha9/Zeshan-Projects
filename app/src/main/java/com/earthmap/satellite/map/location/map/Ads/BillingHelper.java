package com.earthmap.satellite.map.location.map.Ads;

import android.content.Context;
import android.content.SharedPreferences;

public class BillingHelper {
    private SharedPreferences billingPreferences;

    public BillingHelper(Context content) {
        billingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE);
    }
    public boolean isNotAdPurchased() {
        //Add ! for Show Ads behind the billingPreferences Word
        return billingPreferences.getBoolean("ads_purchase", false);
    }
    public void setBillingPreferences(boolean status) {
        billingPreferences.edit().putBoolean("ads_purchase", status).apply();
    }

}
