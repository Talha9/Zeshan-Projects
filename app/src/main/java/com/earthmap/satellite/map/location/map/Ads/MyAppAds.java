package com.earthmap.satellite.map.location.map.Ads;

import static com.earthmap.satellite.map.location.map.Utils.ExtFunctionsKt.getAdaptiveAdSize;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.adapters.AppLovinMediationAdapter;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.earthmap.satellite.map.location.map.R;
import com.earthmap.satellite.map.location.map.Utils.MyAppClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.security.cert.Extension;


public class MyAppAds {

    public static String appid_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_app_id);
    public static String interstitial_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_interstitial_ad_id);
    public static String banner_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_banner_ad_id);
    public static String native_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_native_ad_id);
    public static String app_open_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_app_open_ad_id);

    public static String max_banner_id = MyAppClass.Companion.getStr(R.string.max_banner_id);
    public static String max_interstitial_id = MyAppClass.Companion.getStr(R.string.max_interstitial_id);
    public static String max_native_id = MyAppClass.Companion.getStr(R.string.max_native_id);

    public static String mapbox_access_token_1 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_1);
    public static String mapbox_access_token_2 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_2);
    public static String mapbox_access_token_3 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_3);
    public static String mapbox_access_token_4 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_4);
    public static String mapbox_access_token_5 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_5);
    public static String mapbox_access_token_6 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_6);
    public static String mapbox_access_token_7 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_7);
    public static String mapbox_access_token_8 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_8);


    public static boolean shouldShowAdmob = true;
    public static boolean haveGotSnapshot = false;


    public static long loadingAdDialogTime = 1000;


    public static boolean showSplashGoAd = true;
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static boolean canReLoadedAdMob = true;
    public static boolean canReLoadedMax = false;

    public static boolean canAppOpenShow = true;
    public static boolean shouldShowAppOpen = true;


    public static InterstitialAd admobInterstitialAd;

    public static MaxInterstitialAd maxInterstitialAdLiveEarth;
    public static boolean shouldGoForAds = true;

    public static long next_ads_time = 0;
    public static double current_counter = 1;
    public static double interstitial_Counter = 1;

    private static DatabaseReference adsDatabaseReference = FirebaseDatabase.getInstance().getReference("LiveEarthMapNavigation");
    private static final Handler myHandler = new Handler();


    public static void setHandlerForAd() {
        shouldGoForAds = false;
        Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeStart: " + shouldGoForAds);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldGoForAds = true;
                Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeComplete: " + shouldGoForAds);
            }
        }, next_ads_time);
    }

    public static void addModelToFirebase(Context mContext) {
        adsDatabaseReference.child("RelesAds").setValue(new MyAdModel(
                mContext.getString(R.string.admob_banner_ad_id)
                , mContext.getString(R.string.admob_interstitial_ad_id)
                , mContext.getString(R.string.admob_native_ad_id)
                , mContext.getString(R.string.admob_app_open_ad_id)
                , shouldShowAppOpen
                , shouldShowAdmob
                , next_ads_time
                , current_counter
                , showSplashGoAd
        ));

    }

    private static void AdMobBanner(final LinearLayout adContainer, final AdView mAdView, final Context context) {
        try {
            mAdView.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("ConstantAdsLoadAds", "Banner AdMob Loaded");
                try {
                    //  adContainer.removeAllViews();
                    adContainer.addView(mAdView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.destroy();
                Log.d("ConstantAdsLoadAds", "loadAdError : " + loadAdError.getMessage() + " : " + loadAdError.getResponseInfo().getResponseId());
                loadGpsBannerMax(adContainer, context);
            }
        });
    }

    //only fb
    public static void loadEarthMapBannerForMainMediation(final LinearLayout adContainer, final Context context) {
        BillingHelper billingHelper = new BillingHelper(context);
        if (billingHelper.isNotAdPurchased()) {
            if (shouldShowAdmob) {
                AdView adView = new AdView(context);
                adView.setAdUnitId(MyAppAds.banner_admob_inApp);
                adView.setAdSize(getAdaptiveAdSize(adView));
                if (MyAppAds.mFirebaseRemoteConfig.getBoolean("admob_first")) {
                    MyAppAds.AdMobBanner(adContainer, adView, context);
                } else {
                    loadGpsBannerMax(adContainer, context);
                }
            }
        }

    }

    //MAX Banner
    public static void loadGpsBannerMax(final LinearLayout adContainer, final Context context) {

        MaxAdView adView = new MaxAdView(max_banner_id, (Activity) context);
        adView.setListener(new MaxAdViewAdListener() {

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.i("ConstantAdsLoadAds ", "Max onAdLoaded");
                try {
                    adContainer.removeAllViews();
                    adContainer.addView(adView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

            }


            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }
        });
        adView.loadAd();
    }


    public static void preLoadAds(final Context context) {
        BillingHelper billingHelper = new BillingHelper(context);
        //admobeload
        if (billingHelper.isNotAdPurchased()) {
            if (admobInterstitialAd == null) {
                canReLoadedAdMob = false;
                InterstitialAd.load(context, MyAppAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob loaded");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob Faild");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = null;
                    }
                });

            } else {
                Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            }
        }

    }

    public static void preReLoadAdsSplash(final Context context) {
        //admobeload
        if (canReLoadedAdMob) {
            canReLoadedAdMob = false;
            InterstitialAd.load(context, MyAppAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.d("ConstantAdsLoadAds", "splash Admob Reloaded");
                    canReLoadedAdMob = true;
                    admobInterstitialAd = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d("ConstantAdsLoadAds", "splash Admob ReFaild");
                    canReLoadedAdMob = true;
                    admobInterstitialAd = null;
                }
            });

        } else {
            Log.d("ConstantAdsLoadAds", "splash Admob last ad request is in pending");
        }
    }

    public static void preReLoadAds(final Context context) {
        //admobeload
        if (shouldShowAdmob) {
            if (admobInterstitialAd != null) {
                Log.d("ConstantAdsLoadAds", "admobe ReAlReady loaded");
            } else {
                Log.d("ConstantAdsLoadAds", "canReLoadedAdMob " + canReLoadedAdMob);
                if (canReLoadedAdMob) {
                    canReLoadedAdMob = false;
                    InterstitialAd.load(context, MyAppAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            Log.d("ConstantAdsLoadAds", "Admob Reloaded");
                            canReLoadedAdMob = true;
                            admobInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.d("ConstantAdsLoadAds", "Admob ReFaild");
                            canReLoadedAdMob = true;
                            admobInterstitialAd = null;
                        }
                    });

                } else {
                    Log.d("ConstantAdsLoadAds", "Admob last ad request is in pending");
                }

            }
            //maxReLoad
            if (maxInterstitialAdLiveEarth != null && maxInterstitialAdLiveEarth.isReady()) {
                Log.d("ConstantAdsLoadAds", "max ReAlReady loaded");
            } else {
                if (canReLoadedMax) {
                    canReLoadedMax = false;
                    maxInterstitialAdLiveEarth = new MaxInterstitialAd(max_interstitial_id, (Activity) context);
                    maxInterstitialAdLiveEarth.setListener(new MaxAdListener() {

                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            canReLoadedMax = true;
                        }


                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {

                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String s, MaxError maxError) {
                            canReLoadedMax = true;
                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

                        }


                    });
                    maxInterstitialAdLiveEarth.loadAd();
                }
            }
        }

    }

    //maxload
    public static void preloadMax(Activity context) {
        BillingHelper billingHelper = new BillingHelper(context);
        if (billingHelper.isNotAdPurchased()) {
            if (maxInterstitialAdLiveEarth == null) {
                canReLoadedMax = false;
                maxInterstitialAdLiveEarth = new MaxInterstitialAd(max_interstitial_id, context);
                maxInterstitialAdLiveEarth.setListener(new MaxAdListener() {

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        Log.d("MAXLoadTAG", "onAdLoaded: ");
                        canReLoadedMax = true;
                    }


                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String s, MaxError maxError) {
                        canReLoadedMax = true;
                        Log.d("MAXLoadTAG", "onAdLoadFailed: ");
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

                    }


                });

                maxInterstitialAdLiveEarth.loadAd();
            }
        }

    }


}

interface adsCallback {
    void afterAdClick();
}

