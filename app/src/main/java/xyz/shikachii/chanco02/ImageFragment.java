package xyz.shikachii.chanco02;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class ImageFragment extends Fragment {
    private TextView imageText;
    private RelativeLayout image_fragment;
    private SmartImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        image = (SmartImageView) v.findViewById(R.id.image1_1);
        image_fragment = (RelativeLayout) v.findViewById(R.id.image_fragment);
        imageText = (TextView) v.findViewById(R.id.input_text);
        Button imageClose = (Button) v.findViewById(R.id.image_close);

        hideImage();
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage();
                //MainActivity->hideImageView
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

    public void viewImage(Status status,int index){
        showImage();
        Status item;
        MediaEntity[] mediaEntities;
        int width,height;

        if(status.isRetweet()){
            item = status.getRetweetedStatus();
        }else{
            item = status;
        }

        mediaEntities = item.getExtendedMediaEntities();
        //showToast(getActivity() + mediaEntities[0].getMediaURLHttps() + image);
        Picasso.with(getActivity()).load(mediaEntities[index].getMediaURLHttps()).into(image);
        //imageText.setText(status.getText());

        width = image.getWidth();
        height = image.getHeight();
        //showToast(width + " : " + height);

    }
    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
