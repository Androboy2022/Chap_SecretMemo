package com.cyberkyj.chap_secretmemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMemo, btnPassword, btnFinish;
    MemoAdapter adapter;
    ListView listView;
    File file;
    int location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE},0);

        btnMemo = findViewById(R.id.button);
        btnPassword = findViewById(R.id.button2);
        btnFinish = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);

        btnMemo.setOnClickListener(this);
        btnPassword.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        file = new File(getFilesDir(),"list.txt");

        adapter = new MemoAdapter(this);
        listView.setAdapter(adapter);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            int count = (Integer)objectInputStream.readObject();
            for(int i=0; i<count; i++){
                MemoItem item = (MemoItem)objectInputStream.readObject();
                adapter.addItem(item);
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String passwordUseYN = loadPasswordUseYN(this);
        if(passwordUseYN!=null && passwordUseYN.equals("Y")){
            Intent intent = new Intent(getApplicationContext(), PasswordSettingActivity.class);
            intent.putExtra("mode", "unlock");
            startActivityForResult(intent,1002);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location = position;
                MemoItem item = (MemoItem)adapter.getItem(position);
                String contents = item.getContens();
                String friendName = item.getFriendName();
                String friendMobile = item.getFriendPhone();
                String timeStamp = item.getTimeStamp();
                String imagePath = item.getImagePath();

                Intent intent = new Intent(getApplicationContext(), MemoInputActivity.class);
                intent.putExtra("mode", "modify");
                intent.putExtra("contents", contents);
                intent.putExtra("friendName",friendName);
                intent.putExtra("friendMobile", friendMobile);
                intent.putExtra("timeStamp", timeStamp);
                intent.putExtra("imagePath", imagePath);
                startActivityForResult(intent,1001);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MemoItem item = (MemoItem)adapter.getItem(position);
                String imagePath = item.getImagePath();
                File curFile = new File(imagePath);
                curFile.delete();

                ArrayList<MemoItem> items = adapter.items;
                items.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"항목이 삭제되었습니다.",Toast.LENGTH_LONG).show();
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeObject(new Integer(items.size()));
                    for(int i=0; i<items.size(); i++){
                        MemoItem memoItem = items.get(i);
                        outputStream.writeObject(memoItem);
                    }
                    outputStream.flush();
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==btnMemo){
            Intent intent = new Intent(getApplicationContext(), MemoInputActivity.class);
            intent.putExtra("mode","create");
            startActivityForResult(intent,1001);

        }else if(v==btnPassword){
            Intent intent = new Intent(getApplicationContext(), PasswordSettingActivity.class);
            intent.putExtra("mode","lock");
            startActivityForResult(intent,1002);

        }else if(v==btnFinish){
            finish();
        }

    }

    private String loadPasswordUseYN(Context context){
        SharedPreferences preferences = context.getSharedPreferences("environ",0);
        return  preferences.getString("passwordUseYN","N");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && requestCode==1002){
            String mode = data.getStringExtra("mode");
            if(mode!=null&&mode.equals("exit")){
                finish();
            }
        }else if(data!=null && requestCode==1001){
            String mode = data.getStringExtra("mode");
            String contents = data.getStringExtra("contents");
            String friednName = data.getStringExtra("friendName");
            String friendMobile = data.getStringExtra("friendMobile");
            String timeStamp = data.getStringExtra("timeStamp");
            String imagePath = data.getStringExtra("imagePath");

            if(mode!=null && mode.equals("create")){
                MemoItem item = new MemoItem(contents, friednName, friendMobile, timeStamp, imagePath);
                adapter.addItem(item);
                adapter.notifyDataSetChanged();

                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    ArrayList<MemoItem> items = adapter.items;
                    outputStream.writeObject(new Integer(items.size()));
                    for(int i=0; i<items.size(); i++){
                        MemoItem memoItem = items.get(i);
                        outputStream.writeObject(memoItem);
                    }
                    outputStream.flush();
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(mode!=null && mode.equals("modify")){
                MemoItem item = new MemoItem(contents, friednName, friendMobile, timeStamp, imagePath);
                ArrayList<MemoItem> items = adapter.items;
                items.remove(location);
                items.add(location,item);
                adapter.notifyDataSetChanged();

                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeObject(new Integer(items.size()));
                    for(int i=0; i<items.size(); i++){
                        MemoItem memoItem = items.get(i);
                        outputStream.writeObject(memoItem);
                    }
                    outputStream.flush();
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

