package xyz.shikachii.chanco02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.util.TimeSpanConverter;

public class StatusLayout extends RelativeLayout{

    private onImageClickListener mCallback_image;
    public interface onImageClickListener{
        void onImageSelected(Status item);
    }

    //name
    TextView mNameView;
    //screen_name
    TextView mScreenNameView;
    //icon
    SmartImageView mIconView;
    //text
    TextView mTextView;
    //time
    TextView mTimeView;
    //via
    TextView mViaView;
    //rt_by
    TextView mRt_byView;
    //rt_icon
    SmartImageView mRt_iconView;
    //key
    TextView mKey_iconView;
    //RT
    TextView mRtView;
    //image
    SmartImageView mImage_View[] = new SmartImageView[4];
    private int RT_count;
    //FAV
    TextView mFavView;
    private int FAV_count;
    private int r,g,b;

    public StatusLayout(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected  void onFinishInflate(){
        super.onFinishInflate();
        mNameView = (TextView) findViewById(R.id.name);
        mScreenNameView = (TextView) findViewById(R.id.screen_name);
        mIconView = (SmartImageView) findViewById(R.id.icon);
        mTextView = (TextView) findViewById(R.id.text);
        mTimeView = (TextView) findViewById(R.id.time);
        mViaView = (TextView) findViewById(R.id.via);
        mRt_byView = (TextView) findViewById(R.id.rtby);
        mRt_iconView = (SmartImageView) findViewById(R.id.rtIcon);
        mKey_iconView = (TextView) findViewById(R.id.key);
        mRtView = (TextView) findViewById(R.id.rt);
        mFavView = (TextView) findViewById(R.id.fav);
        mImage_View[0] = (SmartImageView) findViewById(R.id.image1);
        mImage_View[1] = (SmartImageView) findViewById(R.id.image2);
        mImage_View[2] = (SmartImageView) findViewById(R.id.image3);
        mImage_View[3] = (SmartImageView) findViewById(R.id.image4);


    }

    public void TweetChecker(final Status status){
        int index,width= 450,height = 150;
        String tweet;
        URLEntity[] entities;
        MediaEntity[] mediaEntities;
        if(status.isRetweet()) {
            tweet = status.getRetweetedStatus().getText();
            entities = status.getRetweetedStatus().getURLEntities();
            mediaEntities = status.getRetweetedStatus().getExtendedMediaEntities();
        }else{
            tweet = status.getText();
            entities = status.getURLEntities();
            mediaEntities = status.getExtendedMediaEntities();
        }

        for (URLEntity entity : entities) {
            String ex_url = entity.getExpandedURL();
            String tco = entity.getURL();

            Pattern p = Pattern.compile(tco);
            Matcher m = p.matcher(tweet);
            tweet = m.replaceAll(ex_url);
        }

        switch (mediaEntities.length){
            case 1 :
                width = 480;
                break;
            case 2 :
                width = 240;
                break;
            case 3 :
                width = 160;
                break;
            case 4 :
                width = 120;
                break;
            default: break;
        }
        index = 0;
        //System.out.println(mediaEntities.length);
        for(MediaEntity mediaEntity : mediaEntities){
            //System.out.println(mediaEntities.length);
            mImage_View[index].setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(mediaEntity.getMediaURLHttps()).resize(width,height).centerCrop().into(mImage_View[index]);
            index += 1;
            //Picasso.with(getContext()).load(mediaEntity.getMediaURLHttps()).into(mImage_View);
            //System.out.println(mediaEntity.getMediaURL());
        }
        mTextView.setText(tweet);

        mImage_View[0].setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        System.out.println(mCallback_image);
                        //mCallback_image.onImageSelected(status);
                    }
                }
        );
        mImage_View[1].setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("画像2がクリックされました");
                    }
                }
        );
        mImage_View[2].setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("画像3がクリックされました");
                    }
                }
        );
        mImage_View[3].setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("画像4がクリックされました");
                    }
                }
        );
    }

    public void bindView(Status status){
        TimeSpanConverter created = new TimeSpanConverter();
        mKey_iconView.setVisibility(View.GONE);
        mRtView.setVisibility(View.GONE);
        mFavView.setVisibility(View.GONE);
        mRt_iconView.setVisibility(View.GONE);
        mRt_byView.setVisibility(View.GONE);
        mImage_View[0].setVisibility(View.GONE);
        mImage_View[1].setVisibility(View.GONE);
        mImage_View[2].setVisibility(View.GONE);
        mImage_View[3].setVisibility(View.GONE);
        TweetChecker(status);
        if(status.isRetweet()){
            RT_count=status.getRetweetedStatus().getRetweetCount();
            FAV_count = status.getRetweetedStatus().getFavoriteCount();
            //色:緑っぽい
            r=30;g=150;b=30;
            //RTされた人↓
            mNameView.setText(status.getRetweetedStatus().getUser().getName());
            mScreenNameView.setText("@" + status.getRetweetedStatus().getUser().getScreenName());
            Picasso.with(getContext()).load(status.getRetweetedStatus().getUser().getProfileImageURL()).into(mIconView);
            //mTextView.setText(status.getRetweetedStatus().getText());
            //TextView.setText(tweet);
            mTimeView.setText(created.toTimeSpanString(status.getRetweetedStatus().getCreatedAt()));
            mViaView.setText("via "+remTag(status.getRetweetedStatus().getSource()));
            //RTした人↓
            mRt_byView.setVisibility(View.VISIBLE);
            mRt_byView.setText("RT : " + status.getUser().getScreenName());
            mRt_iconView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(status.getUser().getProfileImageURL()).into(mRt_iconView);
        }else{
            //TODO:お気に入りユーザーは全体的に色を変える(赤とか)

            RT_count = status.getRetweetCount();
            FAV_count = status.getFavoriteCount();

            //色:黒っぽい
            r = 30;
            g = 30;
            b = 30;

            if(status.getUser().isProtected()){
                //mKey_iconView.setVisibility(View.VISIBLE);
                //mKey_iconView.setTextColor(Color.argb(160,200,200,200));
                r = 150; g = 30;b = 30;
            }

            mNameView.setText(status.getUser().getName());
            mScreenNameView.setText("@" + status.getUser().getScreenName());
            Picasso.with(getContext()).load(status.getUser().getProfileImageURL()).into(mIconView);
            //mTextView.setText(status.getText());
            //mTextView.setText(tweet);
            mTimeView.setText(created.toTimeSpanString(status.getCreatedAt()));
            mViaView.setText("via "+remTag(status.getSource()));
        }
        if(RT_count != 0){
            mRtView.setVisibility(View.VISIBLE);
            mRtView.setText("RT:" + RT_count);
            mRtView.setTextColor(Color.rgb(0,128,0));
        }
        if(FAV_count != 0){
            mFavView.setVisibility(View.VISIBLE);
            mFavView.setText(" FAV:" + FAV_count);
            mFavView.setTextColor(Color.rgb(220,180,0));
        }
        mNameView.setTextColor(Color.rgb(r, g, b));
        mScreenNameView.setTextColor(Color.rgb(r, g, b));
        mTextView.setTextColor(Color.rgb(r, g, b));
        mViaView.setTextColor(Color.rgb(r, g, b));
        mTimeView.setTextColor(Color.rgb(r, g, b));

        //デバッグ用
        // Picasso.with(getContext()).setIndicatorsEnabled(true);
    }

    private static String remTag(String string){ return string.replaceAll("<.+?>",""); }

}
