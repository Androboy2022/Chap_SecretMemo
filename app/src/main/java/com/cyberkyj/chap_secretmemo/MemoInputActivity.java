package com.cyberkyj.chap_secretmemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoInputActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtMemo, edtName, edtNumber;
    TextView txtTile, txtDate;
    ImageView imageView;
    Button btnPhone, btnSms, btnSave, btnCancel;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
    File curFile;
    Intent intent;
    Bitmap imageBitmap;
    String mode;
    Uri fileURI;
    Bitmap rotatedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_input);

        edtMemo = findViewById(R.id.editText);
        edtName = findViewById(R.id.editText2);
        edtNumber = findViewById(R.id.editText3);
        txtTile = findViewById(R.id.textView);
        txtDate = findViewById(R.id.txtDate);
        imageView = findViewById(R.id.imageView);

        btnPhone = findViewById(R.id.button4);
        btnSms = findViewById(R.id.button5);
        btnSave = findViewById(R.id.button6);
        btnCancel = findViewById(R.id.button7);

        btnPhone.setOnClickListener(this);
        btnSms.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imageView.setOnClickListener(this);

        intent = getIntent();
        mode = intent.getStringExtra("mode");
        if(mode!=null && mode.equals("create")){
            txtTile.setText("새 메모");
            Date date = new Date();
            String timeStamp = dateFormat.format(date);
            txtDate.setText(timeStamp);
        }else if(mode!=null && mode.equals("modify")){
            String contents = intent.getStringExtra("contents");
            String friednName = intent.getStringExtra("friendName");
            String friendMobile = intent.getStringExtra("friendMobile");
            String timeStamp = intent.getStringExtra("timeStamp");
            String imagePath = intent.getStringExtra("imagePath");

            txtTile.setText("메모 수정");
            edtMemo.setText(contents);
            edtName.setText(friednName);
            edtNumber.setText(friendMobile);
            txtDate.setText(timeStamp);
            if(imagePath.equals("")){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }else{
                curFile = new File(imagePath);

                if(Build.VERSION.SDK_INT>=24){
                    fileURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, curFile);


                }else{
                    fileURI = Uri.fromFile(curFile);

                }

                try {
                    imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileURI));
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    Bitmap rotateBitmap = Bitmap.createBitmap(imageBitmap, 0,0,imageBitmap.getWidth(), imageBitmap.getHeight(),matrix,true);
                    imageBitmap = rotateBitmap;
                    imageView.setImageBitmap(imageBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btnPhone){
            String friendMobile = edtNumber.getText().toString();
            try{ Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+friendMobile));
                startActivity(intent);
            }catch (SecurityException e){

            }

        }else if(v==btnSms){
            String friendMobile = edtNumber.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("address",friendMobile);
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);

        }else if(v==btnSave){
            String contents = edtMemo.getText().toString();
            String friendName = edtName.getText().toString();
            String friendMobile = edtNumber.getText().toString();
            String timeStamp = txtDate.getText().toString();

            intent = new Intent();
            intent.putExtra("mode", mode);
            intent.putExtra("contents", contents);
            intent.putExtra("friendName",friendName);
            intent.putExtra("friendMobile", friendMobile);
            intent.putExtra("timeStamp", timeStamp);
            if(curFile!=null){
                intent.putExtra("imagePath", curFile.getAbsolutePath());
            }else{
                intent.putExtra("imagePath","");
            }
            setResult(RESULT_OK, intent);
            finish();

        }else if(v==btnCancel){

        }else if(v==imageView){

            SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date date = new Date();
            String fileStamp = fileFormat.format(date)+".jpg";
            curFile = new File(getFilesDir(), fileStamp);
            if(Build.VERSION.SDK_INT>=24){
                fileURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, curFile);
                //fileURI = FileProvider.getUriForFile(getApplicationContext(),"com.cyberkyj.chap_secretmemo.fileprovider",curFile);

            }else{
                fileURI = Uri.fromFile(curFile);

            }
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent,1004);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1004){

                try {
                    ExifInterface ei = new ExifInterface(curFile);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    Matrix matrix = new Matrix();

                    imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileURI));


                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                                    imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                                    imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                                    imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                        default:
                            rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                                    imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                    }


                    imageBitmap = rotatedBitmap;
                    imageView.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }


        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){



        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT) {

        }
    }
}
