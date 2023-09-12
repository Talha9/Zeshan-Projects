package com.earthmap.satellite.map.location.map.Ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.earthmap.satellite.map.location.map.Ads.MyAppAds.canAppOpenShow;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.earthmap.satellite.map.location.map.Utils.MyAppClass;
import com.earthmap.satellite.map.location.map.splashModule.SplashActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;


public class AppOpenAdsManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private static final String LOG_TAG = "AppOpenLog:";
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final MyAppClass myApplication;
    public static boolean isShowingAd = false;
    Activity makerAppCurrentActivity;
    private long adLoadTime = 0;
    private BillingHelper billingHelper;


    public AppOpenAdsManager(MyAppClass myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        billingHelper=new BillingHelper(myApplication);
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        if (!(makerAppCurrentActivity instanceof SplashActivity)) {
            Log.d(LOG_TAG, "onStart: Showing ");
                if (billingHelper.isNotAdPurchased() && canAppOpenShow && MyAppAds.mFirebaseRemoteConfig.getBoolean("app_open_remote_check")) {
                    showAdIfAvailable();
                }
        } else {
            if (billingHelper.isNotAdPurchased() && MyAppAds.mFirebaseRemoteConfig.getBoolean("app_open_remote_check")) {
                Log.d(LOG_TAG, "onStart:Not  Showing because splash");
                fetchAd();
            }
        }
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        // We will implement this below.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenAdsManager.this.appOpenAd = ad;
                        AppOpenAdsManager.this.adLoadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, MyAppAds.app_open_admob_inApp, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Creates and returns ad request.
     */
    public AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.adLoadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenAdsManager.this.appOpenAd = null;
                            isShowingAd = false;
                            if (MyAppAds.shouldShowAppOpen) {
                                fetchAd();
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(makerAppCurrentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            if (MyAppAds.shouldShowAppOpen) {
                fetchAd();
            }
        }
    }

    public void fetchAdForSPlash() {
        // We will implement this below.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenAdsManager.this.appOpenAd = ad;
                        AppOpenAdsManager.this.adLoadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, MyAppAds.app_open_admob_inApp, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }


    public void showAppOpenAdForSplash(AppCompatActivity activity,
                                       FullScreenContentCallback listener) {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            listener.onAdDismissedFullScreenContent();
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenAdsManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            listener.onAdFailedToShowFullScreenContent(adError);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            listener.onAdShowedFullScreenContent();
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(activity);

        } else {

            listener.onAdDismissedFullScreenContent();
        }


    }


    /**
     * ActivityLifecycleCallback methods
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        makerAppCurrentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        makerAppCurrentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        makerAppCurrentActivity = null;
    }

    public void cancelAppOpenAd() {
        Log.d(LOG_TAG, "cancelAppOpenAd: ");
        appOpenAd = null;
    }

}
