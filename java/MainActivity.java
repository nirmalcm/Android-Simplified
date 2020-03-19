package com.digiapt.kritterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.digiapt.kritteradinterface.listener.AdCallback;
import com.digiapt.kritteradinterface.model.AdResponseInfo;
import com.digiapt.kritteradinterface.model.NativeAd;
import com.digiapt.kritteradinterface.model.RequestInfo;
import com.digiapt.kritteradinterface.view.KritterBannerAdView;
import com.digiapt.kritteradinterface.view.KritterInterstitialAdView;
import com.digiapt.kritteradinterface.view.KritterVideoAdView;
import com.digiapt.kritteradinterface.viewmodel.KritterViewModel;
import com.digiapt.krittersdk.listener.KritterPlayBackListener;
import com.digiapt.krittersdk.network.RequestFailureReason;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.digiapt.kritterapp.RequestInfoProvider.getInterstitialAdRequestInfo;
import static com.digiapt.kritterapp.RequestInfoProvider.getNativeContentStreamAdRequestInfo;
import static com.digiapt.kritterapp.RequestInfoProvider.getVideoAdRequestInfo;
import static com.digiapt.kritterapp.RequestInfoProvider.getWebviewAdRequestInfo;

public class MainActivity extends AppCompatActivity implements KritterPlayBackListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initViewModel();

        initRecyclerView();
    }


    List<View> mViews;
    LinearLayout mHomeScreen;
    private void initViews() {

        mHomeScreen = findViewById(R.id.id_home_screen);

        mBannerAd = findViewById(R.id.id_banner_ad);
        mVideoAd = findViewById(R.id.id_video_ad);
        mInterstitialAd = findViewById(R.id.id_interstitial_ad);
        mNativeContentStreamAd = findViewById(R.id.id_native_content_stream);

        mViews = Arrays.asList(mHomeScreen,mBannerAd,mVideoAd,mInterstitialAd,mNativeContentStreamAd);
    }

    private KritterViewModel mKritterViewModel;
    private void initViewModel() {
        mKritterViewModel = ViewModelProviders.of(this).get(KritterViewModel.class);
        mKritterViewModel.prepare(this);
    }

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.id_recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new Adapter(this, getRecyclerViewContents());
        mAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter.setOnItemClickListener((view, pos, requestType) -> {

            if (requestType == RequestInfo.RequestType.BANNER_AD){

                fetchBannerAd();

            } else if (requestType == RequestInfo.RequestType.VIDEO_AD){

                fetchVideoAd();

            } else if (requestType == RequestInfo.RequestType.INTERSTITIAL_AD){

                fetchInterstitialAd();

            } else if (requestType == RequestInfo.RequestType.NATIVE_CONTENT_STREAM_AD){

                fetchNativeContentStreamAd();

            }

        });
    }

    private View mBannerAd;
    private void fetchBannerAd() {

        show(mBannerAd);

        //Fetching Banner Ad
        KritterBannerAdView kritterBannerAdView = findViewById(R.id.id_kritter_web_view);
        mKritterViewModel.fetchAd(getWebviewAdRequestInfo(this),kritterBannerAdView);

        //Close banner ad
        ImageView closeBannerAd = findViewById(R.id.id_close_banner_ad);
        closeBannerAd.setOnClickListener(view -> show(mHomeScreen));
    }

    private View mVideoAd;
    private void fetchVideoAd() {

        show(mVideoAd);

        //Fetching Video Ad
        KritterVideoAdView kritterVideoAdView = findViewById(R.id.id_kritter_video_view);
        kritterVideoAdView.setOnPlayBackListener(MainActivity.this)
                .enableAutoMute(false)
                .enableAutoPlay(false);
        mKritterViewModel.fetchAd(getVideoAdRequestInfo(this), kritterVideoAdView);

        //Close Video Ad
        ImageButton closeVideoAd = findViewById(R.id.id_close_video_ad);
        closeVideoAd.setOnClickListener(view -> show(mHomeScreen));
    }

    private View mInterstitialAd;
    private void fetchInterstitialAd() {

        show(mInterstitialAd);

        //Fetching Interstitial Ad
        KritterInterstitialAdView kritterInterstitialAdView = findViewById(R.id.id_kritter_interstitial_view);
        mKritterViewModel.fetchAd(getInterstitialAdRequestInfo(this),kritterInterstitialAdView);

        //Close Interstitial Ad
        ImageButton closeInterstitialAd = findViewById(R.id.id_close_interstitial_ad);
        closeInterstitialAd.setOnClickListener(view -> show(mHomeScreen));
    }

    private View mNativeContentStreamAd;
    private void fetchNativeContentStreamAd() {

        show(mNativeContentStreamAd);

        //Fetching Native Content Stream Ad

        Button cta = findViewById(R.id.cta);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView info = findViewById(R.id.info);
        ImageView icon = findViewById(R.id.icon);
        ImageView main = findViewById(R.id.main);
        RatingBar rating = findViewById(R.id.ratingBar);

        NativeAd nativeAdDisplayObject = new  NativeAd(this);
        nativeAdDisplayObject.setDescription(description)
                .setIconImage(icon)
                .setTitle(title)
                .setCtaButton(cta)
                .setRatingBar(rating)
                .setMainImage(main)
                .setInfo(info);

        mKritterViewModel.fetchAd(getNativeContentStreamAdRequestInfo(this), new AdCallback() {
            @Override
            public void handleAdResponse(AdResponseInfo adResponseInfo) {
                nativeAdDisplayObject.handleAdResponseInfo(adResponseInfo);
            }

            @Override
            public void handleAdResponses(AdResponseInfo[] adResponseInfos) {

            }

            @Override
            public void handleAdResponseFailed(RequestFailureReason reason) {

            }

            @Override
            public void handleNoFillResponse() {

            }
        });

        //Close Native Content Stream Ad
        ImageButton closeNativeContentStreamAd = findViewById(R.id.id_close_native_content_stream_ad);
        closeNativeContentStreamAd.setOnClickListener(view -> show(mHomeScreen));
    }

    private List<String> getRecyclerViewContents() {
        return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.recyclerViewContents)));
    }

    private void show(View inputView){
        for (View view : mViews){
            if (view == inputView)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed()
    {
        show(mHomeScreen);
    }

    @Override
    public void onPlayEvent() {

    }

    @Override
    public void onPauseEvent() {

    }

    @Override
    public void onCompletedEvent() {

    }
}