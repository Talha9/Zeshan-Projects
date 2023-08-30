package com.earthmap.satellite.map.location.map.Ads;

import static com.earthmap.satellite.map.location.map.Ads.MyAppAds.canAppOpenShow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.earthmap.satellite.map.location.map.Utils.dialogs.LoadingDialog;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class MyAppShowAds {

    //for simple backPressed
    public static void mediationBackPressedSimple(
            final Activity context,
            final InterstitialAd mInterstitialAd,
            final MaxInterstitialAd maxInterstitialAd,
            final adsCallback callback) {
        LoadingDialog dialog = new LoadingDialog(context);
        if (MyAppAds.mFirebaseRemoteConfig.getBoolean("admob_first")) {
            if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show(context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    callback.afterAdClick();


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                    callback.afterAdClick();
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(() -> {
                        try {
                            dialog.ld_hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        maxInterstitialAd.setListener(new MaxAdListener() {

                            @Override
                            public void onAdLoaded(MaxAd ad) {

                            }


                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                                canAppOpenShow = false;
                                Log.d("MAXLoadTAG", "onAdDisplayed: ");
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                canAppOpenShow = true;
                                MyAppAds.preReLoadAds(context);
                                callback.afterAdClick();
                                Log.d("MAXLoadTAG", "onAdHidden: ");
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String s, MaxError maxError) {

                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                MyAppAds.preReLoadAds(context);
                                callback.afterAdClick();
                                Log.d("MAXLoadTAG", "onAdDisplayFailed: ");
                            }


                        });
                        maxInterstitialAd.showAd();
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else {
                MyAppAds.preReLoadAds(context);
                callback.afterAdClick();
            }
        } else {
            if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(() -> {
                        try {
                            dialog.ld_hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        maxInterstitialAd.setListener(new MaxAdListener() {

                            @Override
                            public void onAdLoaded(MaxAd ad) {

                            }


                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                                canAppOpenShow = false;
                                Log.d("MAXLoadTAG", "onAdDisplayed: ");
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                canAppOpenShow = true;
                                MyAppAds.preReLoadAds(context);
                                callback.afterAdClick();
                                Log.d("MAXLoadTAG", "onAdHidden: ");
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String s, MaxError maxError) {

                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                MyAppAds.preReLoadAds(context);
                                callback.afterAdClick();
                                Log.d("MAXLoadTAG", "onAdDisplayFailed: ");
                            }


                        });
                        maxInterstitialAd.showAd();
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show(context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    callback.afterAdClick();


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                    callback.afterAdClick();
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else {
                MyAppAds.preReLoadAds(context);
                callback.afterAdClick();
            }
        }


    }


    public static void meidationForClickSimpleLiveStreetView(final Context context,
                                                             final InterstitialAd mInterstitialAd,
                                                             final MaxInterstitialAd maxInterstitialAd,
                                                             final adsCallback callback) {
        LoadingDialog dialog = new LoadingDialog(context);
        if (MyAppAds.mFirebaseRemoteConfig.getBoolean("admob_first")) {
            if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show((Activity) context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    callback.afterAdClick();


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            maxInterstitialAd.setListener(new MaxAdListener() {

                                @Override
                                public void onAdLoaded(MaxAd ad) {

                                }


                                @Override
                                public void onAdDisplayed(MaxAd ad) {
                                    Log.d("MAXLoadTAG", "onAdDisplayed: ");
                                    canAppOpenShow = false;
                                }

                                @Override
                                public void onAdHidden(MaxAd ad) {
                                    canAppOpenShow = true;
                                    MyAppAds.preReLoadAds(context);
                                    callback.afterAdClick();
                                    Log.d("MAXLoadTAG", "onAdHidden: ");
                                }

                                @Override
                                public void onAdClicked(MaxAd ad) {

                                }

                                @Override
                                public void onAdLoadFailed(String s, MaxError maxError) {

                                }

                                @Override
                                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                    MyAppAds.preReLoadAds(context);
                                    callback.afterAdClick();
                                    Log.d("MAXLoadTAG", "onAdDisplayFailed: ");
                                }


                            });
                            maxInterstitialAd.showAd();
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else {
                MyAppAds.preReLoadAds(context);
                callback.afterAdClick();
            }
        } else {
            if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            maxInterstitialAd.setListener(new MaxAdListener() {

                                @Override
                                public void onAdLoaded(MaxAd ad) {

                                }


                                @Override
                                public void onAdDisplayed(MaxAd ad) {
                                    Log.d("MAXLoadTAG", "onAdDisplayed: ");
                                    canAppOpenShow = false;
                                }

                                @Override
                                public void onAdHidden(MaxAd ad) {
                                    canAppOpenShow = true;
                                    MyAppAds.preReLoadAds(context);
                                    callback.afterAdClick();
                                    Log.d("MAXLoadTAG", "onAdHidden: ");
                                }

                                @Override
                                public void onAdClicked(MaxAd ad) {

                                }

                                @Override
                                public void onAdLoadFailed(String s, MaxError maxError) {

                                }

                                @Override
                                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                    MyAppAds.preReLoadAds(context);
                                    callback.afterAdClick();
                                    Log.d("MAXLoadTAG", "onAdDisplayFailed: ");
                                }


                            });
                            maxInterstitialAd.showAd();
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show((Activity) context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    callback.afterAdClick();


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    callback.afterAdClick();
                }
            } else {
                MyAppAds.preReLoadAds(context);
                callback.afterAdClick();
            }
        }


    }


    public static void meidationForClickLiveStreetView(final Context context,
                                                       final InterstitialAd mInterstitialAd,
                                                       final MaxInterstitialAd maxInterstitialAd,
                                                       final Intent intent) {
        LoadingDialog dialog = new LoadingDialog(context);
        if (MyAppAds.mFirebaseRemoteConfig.getBoolean("admob_first")) {
            if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show((Activity) context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    context.startActivity(intent);


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    context.startActivity(intent);
                }
            } else if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            maxInterstitialAd.setListener(new MaxAdListener() {

                                @Override
                                public void onAdLoaded(MaxAd ad) {

                                }


                                @Override
                                public void onAdDisplayed(MaxAd ad) {
                                    canAppOpenShow = false;
                                }

                                @Override
                                public void onAdHidden(MaxAd ad) {
                                    canAppOpenShow = true;
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onAdClicked(MaxAd ad) {

                                }

                                @Override
                                public void onAdLoadFailed(String s, MaxError maxError) {

                                }

                                @Override
                                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }


                            });
                            maxInterstitialAd.showAd();
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    context.startActivity(intent);
                }
            } else {
                MyAppAds.preReLoadAds(context);
                context.startActivity(intent);
            }
        } else {
            if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            maxInterstitialAd.setListener(new MaxAdListener() {

                                @Override
                                public void onAdLoaded(MaxAd ad) {

                                }


                                @Override
                                public void onAdDisplayed(MaxAd ad) {
                                    canAppOpenShow = false;
                                }

                                @Override
                                public void onAdHidden(MaxAd ad) {
                                    canAppOpenShow = true;
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onAdClicked(MaxAd ad) {

                                }

                                @Override
                                public void onAdLoadFailed(String s, MaxError maxError) {

                                }

                                @Override
                                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }


                            });
                            maxInterstitialAd.showAd();
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    context.startActivity(intent);
                }
            } else if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyAppAds.interstitial_Counter = 1;
                            mInterstitialAd.show((Activity) context);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    MyAppAds.admobInterstitialAd = null;
                                    MyAppAds.preReLoadAds(context);
                                    MyAppAds.setHandlerForAd();
                                    canAppOpenShow = true;
                                    context.startActivity(intent);


                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    canAppOpenShow = false;
                                }
                            });
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

                } else {
                    MyAppAds.interstitial_Counter++;
                    context.startActivity(intent);
                }
            } else {
                MyAppAds.preReLoadAds(context);
                context.startActivity(intent);
            }
        }


    }


    public static void meidationForClickLiveStreetViewWithoutCounter(final Context context,
                                                                     final InterstitialAd mInterstitialAd,
                                                                     final MaxInterstitialAd maxInterstitialAd,
                                                                     final Intent intent) {
        LoadingDialog dialog = new LoadingDialog(context);
        if (MyAppAds.mFirebaseRemoteConfig.getBoolean("admob_first")) {
            if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                try {
                    dialog.ld_show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dialog.ld_hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MyAppAds.interstitial_Counter = 1;
                        mInterstitialAd.show((Activity) context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                MyAppAds.admobInterstitialAd = null;
                                MyAppAds.preReLoadAds(context);
                                MyAppAds.setHandlerForAd();
                                canAppOpenShow = true;
                                context.startActivity(intent);


                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                canAppOpenShow = false;
                            }
                        });
                    }
                }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

            } else if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {
                if (MyAppAds.interstitial_Counter / MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_counter") == 1) {
                    try {
                        dialog.ld_show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.ld_hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            maxInterstitialAd.setListener(new MaxAdListener() {

                                @Override
                                public void onAdLoaded(MaxAd ad) {

                                }


                                @Override
                                public void onAdDisplayed(MaxAd ad) {
                                    canAppOpenShow = false;
                                }

                                @Override
                                public void onAdHidden(MaxAd ad) {
                                    canAppOpenShow = true;
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onAdClicked(MaxAd ad) {

                                }

                                @Override
                                public void onAdLoadFailed(String s, MaxError maxError) {

                                }

                                @Override
                                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                    MyAppAds.preReLoadAds(context);
                                    context.startActivity(intent);
                                }


                            });
                            maxInterstitialAd.showAd();
                        }
                    }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
                } else {
                    MyAppAds.interstitial_Counter++;
                    context.startActivity(intent);
                }
            } else {
                MyAppAds.preReLoadAds(context);
                context.startActivity(intent);
            }
        } else {
            if (maxInterstitialAd != null && maxInterstitialAd.isReady() && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {

                try {
                    dialog.ld_show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dialog.ld_hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        maxInterstitialAd.setListener(new MaxAdListener() {

                            @Override
                            public void onAdLoaded(MaxAd ad) {

                            }


                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                                canAppOpenShow = false;
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                canAppOpenShow = true;
                                MyAppAds.preReLoadAds(context);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String s, MaxError maxError) {

                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                MyAppAds.preReLoadAds(context);
                                context.startActivity(intent);
                            }


                        });
                        maxInterstitialAd.showAd();
                    }
                }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));
            } else if (mInterstitialAd != null && MyAppAds.mFirebaseRemoteConfig.getBoolean("interstitial_remote_check")) {

                try {
                    dialog.ld_show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dialog.ld_hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MyAppAds.interstitial_Counter = 1;
                        mInterstitialAd.show((Activity) context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                MyAppAds.admobInterstitialAd = null;
                                MyAppAds.preReLoadAds(context);
                                MyAppAds.setHandlerForAd();
                                canAppOpenShow = true;
                                context.startActivity(intent);


                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                canAppOpenShow = false;
                            }
                        });
                    }
                }, MyAppAds.mFirebaseRemoteConfig.getLong("interstitial_dialog_time"));

            } else {
                MyAppAds.preReLoadAds(context);
                context.startActivity(intent);
            }
        }


    }


    public static void meidationForClickFinishLiveStreetView(final Activity context, final InterstitialAd mInterstitialAd/*, final MoPubInterstitial moPubInterstitial*/, final Intent intent) {
        if (mInterstitialAd != null && MyAppAds.shouldGoForAds) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    MyAppAds.admobInterstitialAd = null;
                    MyAppAds.preReLoadAds(context);
                    MyAppAds.setHandlerForAd();
                    canAppOpenShow = true;
                    context.startActivity(intent);
                    context.finish();

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    canAppOpenShow = false;
                }
            });
        } else {
            MyAppAds.preReLoadAds(context);
            context.startActivity(intent);
            context.finish();
        }

    }

    public static void meidationForClickFragmentLiveStreetView(final Context context, final InterstitialAd mInterstitialAd) {
        if (mInterstitialAd != null && MyAppAds.shouldGoForAds) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    MyAppAds.admobInterstitialAd = null;
                    MyAppAds.preReLoadAds(context);
                    canAppOpenShow = true;
                    MyAppAds.setHandlerForAd();

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    canAppOpenShow = false;
                }
            });
        } else {
            MyAppAds.preReLoadAds(context);
        }

    }


}

