package com.cyberkyj.chap_secretmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordSettingActivity extends AppCompatActivity {

    EditText edtPassword1, edtPassword2;
    CheckBox checkBox;
    TextView txtTitle;
    Intent intent;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);

        edtPassword1 = findViewById(R.id.editText4);
        edtPassword2 = findViewById(R.id.editText5);

        Button btnSave = findViewById(R.id.button8);
        Button btnCancel = findViewById(R.id.button9);

        txtTitle = findViewById(R.id.textView8);
        checkBox = findViewById(R.id.checkBox);

        intent = getIntent();
        mode = intent.getStringExtra("mode");
        if(mode!=null && mode.equals("lock")){
            checkBox.setVisibility(View.GONE);
            txtTitle.setText("비밀번호 설정");
            btnSave.setText("저장");
            btnCancel.setText("취소");
        }else{
            checkBox.setVisibility(View.VISIBLE);
            txtTitle.setText("비밀번호 확인");
            btnSave.setText("확인");
            btnCancel.setText("끝내기");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = edtPassword1.getText().toString();
                String paswword2 = edtPassword2.getText().toString();

                if(!(password1.equals(paswword2))){
                    Toast.makeText(getApplicationContext(),"입력한 두 개의 비밀번호가 다릅니다",Toast.LENGTH_LONG).show();
                    return;
                }
                if(mode!=null && mode.equals("lock")){
                    savePasswordUseYN(getApplicationContext(),"Y");
                    savePassword(getApplicationContext(),password1);
                    Toast.makeText(getApplicationContext(),"비밀번호가 설정되었습니다.",Toast.LENGTH_LONG).show();
                    finish();

                }else if(mode!=null && mode.equals("unlock")){
                    String prevPassword = loadPassword(getApplicationContext());
                    if(password1.equals(prevPassword)){
                        if(checkBox.isChecked()){
                            savePasswordUseYN(getApplicationContext(),"N");
                            savePassword(getApplicationContext(),"");
                            Toast.makeText(getApplicationContext(),"비밀번호가 해제되었습니다",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"비밀번호가 확인되었습니다.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"비밀번호가 맞지 않습니다.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode!=null && mode.equals("unlock")){
                    intent.putExtra("mode","exit");
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }else{
                    finish();
                }
            }
        });
    }

    private void savePasswordUseYN(Context context, String useYN){
        SharedPreferences preferences = context.getSharedPreferences("environ",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("passwordUseYN",useYN);
        editor.commit();
    }

    private void savePassword(Context context, String password){
        SharedPreferences preferences = context.getSharedPreferences("environ",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password",password);
        editor.commit();
    }

    private String loadPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("environ", 0);
        return  preferences.getString("password","");

    }

    @Override
    public void onBackPressed() {
        intent.putExtra("mode","exit");
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
