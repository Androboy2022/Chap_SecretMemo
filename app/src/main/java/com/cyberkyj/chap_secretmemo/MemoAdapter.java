package com.cyberkyj.chap_secretmemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {

    Context context;
    ArrayList<MemoItem> items = new ArrayList<>();
    String imagePath;

    public MemoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoItemView view =null;
        if(convertView==null){
            view = new MemoItemView(context);
        }else{
            view =(MemoItemView)convertView;
        }
        MemoItem item = items.get(position);
        view.setTxtContents(item.getContens());
        view.setTxtName(item.getFriendName());
        view.setTxtMobile(item.getFriendPhone());
        view.setTxtTimeStamp(item.getTimeStamp());
        imagePath = item.getImagePath();
        if(imagePath.equals("")){
            view.setImage(R.mipmap.ic_launcher);
        }else{
            File file = new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=8;
            Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap rotateBitmap = Bitmap.createBitmap(imageBitmap, 0,0,imageBitmap.getWidth(), imageBitmap.getHeight(),matrix,true);
            imageBitmap = rotateBitmap;
            view.setImageBitmap(imageBitmap);

        }

        return view;
    }

    public void addItem(MemoItem item){
        items.add(item);
    }
}
