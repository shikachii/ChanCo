package xyz.shikachii.chanco02;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Time_line_menu extends Fragment {
    public onClickListener mCallback;

    public interface onClickListener{
        void onTopSelected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_line_menu, container, false);

        v.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTopSelected();
            }
        });
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof onClickListener){
            mCallback = (onClickListener) activity;
        }
    }

    public void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
