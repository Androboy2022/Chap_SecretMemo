package com.cyberkyj.chap_secretmemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MemoItemView extends LinearLayout {

    Context context;
    TextView txtContents, txtName, txtMobile, txtTimeStamp;
    ImageView imageView;

    public MemoItemView(Context context) {
        super(context);
        this.context =context;
        init();
    }

    public MemoItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.memo_item, this, true);
        txtContents = findViewById(R.id.textView4);
        txtName = findViewById(R.id.textView5);
        txtMobile = findViewById(R.id.textView6);
        txtTimeStamp = findViewById(R.id.textView7);
        imageView = findViewById(R.id.imageView2);

    }


    public void setTxtContents(String txtContents) {
        this.txtContents.setText(txtContents);
    }

    public void setTxtName(String txtName) {
        this.txtName.setText(txtName);
    }

    public void setTxtMobile(String txtMobile) {
        this.txtMobile.setText(txtMobile);
    }

    public void setTxtTimeStamp(String txtTimeStamp) {
        this.txtTimeStamp.setText(txtTimeStamp);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
    }

    public void setImage(int imageView) {
        this.imageView.setImageResource(imageView);
    }
}
