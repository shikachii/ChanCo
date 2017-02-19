package xyz.shikachii.chanco02;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class ImageFragment extends Fragment {
    private RelativeLayout image_fragment;
    private SmartImageView image;
    private Status item;
    private int max_image = 0,_index;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        image = (SmartImageView) v.findViewById(R.id.image1_1);
        image_fragment = (RelativeLayout) v.findViewById(R.id.image_fragment);

        hideImage();
        //imageClose.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        hideImage();
                //MainActivity->hideImageView
        //    }
        //});
        image_fragment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){}
        });

        v.findViewById(R.id.image_right).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ rightImage(); }
        });

       v.findViewById(R.id.image_left).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ leftImage();}
        });

        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideImage();
            }
        });

        return v;
    }

    public void hideImage(){
        image_fragment.setVisibility(View.GONE);
    }

    public void showImage(){
        image_fragment.setVisibility(View.VISIBLE);
    }

    public void leftImage(){
        if(_index > 0) {
            viewImage(item,--_index);
        }
    }

    public void rightImage(){
        if(_index < max_image-1){
            viewImage(item,++_index);
        }
    }

    public void viewImage(Status status,int index){
        showImage();
        MediaEntity[] mediaEntities;

        _index = index;

        if(status.isRetweet()){ item = status.getRetweetedStatus(); }
        else{ item = status; }

        mediaEntities = item.getExtendedMediaEntities();
        max_image = mediaEntities.length;
        Picasso.with(getActivity()).load(mediaEntities[index].getMediaURLHttps()).into(image);

    }
    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
