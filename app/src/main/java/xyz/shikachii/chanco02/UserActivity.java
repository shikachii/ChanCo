package xyz.shikachii.chanco02;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button button = (Button)findViewById(R.id.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("onDestroy()");
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
