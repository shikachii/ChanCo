package xyz.shikachii.chanco02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import twitter4j.Status;

public class MainActivity extends FragmentActivity implements Time_line_menu.onClickListener,
        TweetListFragment.onListClickListener,
        StatusLayout.onImageClickListener,
        TweetFragment.onMenuClickListener,
        TweetFragment.onImageMenuClickListener{

    LinearLayout imageFragment;

    @Override
    public void onTopSelected(){
        TweetListFragment fragment = (TweetListFragment) getFragmentManager().findFragmentById(R.id.list);
        if(fragment == null){
            fragment = new TweetListFragment();
            getFragmentManager().beginTransaction().replace(R.id.list,fragment).commit();
        }

        fragment.set();
    }

    @Override
    public void onImageSelected(Status status){
        showImageView();
        ImageFragment imageFragment = (ImageFragment) getFragmentManager().findFragmentById(R.id.image_view);
        imageFragment.viewImage(status,0);
    }

    @Override
    public void onListSelected(Status status){
        TweetFragment tweetFragment = (TweetFragment) getFragmentManager().findFragmentById(R.id.tweet);
        tweetFragment.viewBox(status);
    }

    @Override
    public void onMenuSelected(){
        TweetFragment tweetFragment = (TweetFragment) getFragmentManager().findFragmentById(R.id.tweet);
        tweetFragment.getId();
    }

    @Override
    public void onImageMenuClicked(Status status,int index){
        ImageFragment imageFragment = (ImageFragment) getFragmentManager().findFragmentById(R.id.image_view);
        //showToast(status.getText());
        imageFragment.viewImage(status,index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.content_main);
        hideImageView();

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // キーコード表示
        showToast("KeyCode:"+e.getKeyCode());

        return super.dispatchKeyEvent(e);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("onStop()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        System.out.println("onSave");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestore");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("onRestart()");
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return false;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    private void hideImageView(){
        imageFragment = (LinearLayout)findViewById(R.id.image_view);
        imageFragment.setVisibility(View.GONE);
    }

    private void showImageView(){
        imageFragment = (LinearLayout)findViewById(R.id.image_view);
        imageFragment.setVisibility(View.VISIBLE);
    }

    private void stopUst(){
            onPause();
            showToast("UserStream停止");
            showToast("更新ボタンでTLを更新できます");
    }

    private void restartUst(){
        onRestart();
        showToast("UserStream開始");
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
