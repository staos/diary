package com.example.diary.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.other.MyApplication;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class RegisterZeroActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private String phoneNumber;
    private EditText editVC;
    private String vc;
    private Button buttonVC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_zero);
        BmobSMS.initialize(this, "833d9f9423746b8c6c06f3c91bf76398");
        Button buttonNext = (Button)findViewById(R.id.oneNext);
        buttonNext.setOnClickListener(this);
        buttonVC = (Button)findViewById(R.id.buttonVC);
        buttonVC.setOnClickListener(this);
        editText = (EditText)findViewById(R.id.editTextPhone);
        editVC = (EditText)findViewById(R.id.editTextVC);
    }

    @Override
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.buttonVC:
               requestSMS();
               break;
           case R.id.oneNext:
               if(!editText.getText().toString().equals("") && !editVC.getText().toString().equals("")){
                   vc = editVC.getText().toString();
                   BmobSMS.verifySmsCode(this, phoneNumber, vc, new VerifySMSCodeListener() {
                       @Override
                       public void done(cn.bmob.sms.exception.BmobException e) {
                           if (e == null){//短信验证以验证成功
                               Intent intent = new Intent(MyApplication.getContext(),RegisterTwoActivity.class);
                               intent.putExtra("phoneNumber",phoneNumber);
                               startActivity(intent);
                           }else{
                               Log.i("bomb","验证失败：code =" + e.getErrorCode() + ",msg = " +e.getLocalizedMessage());
                           }
                       }
                   });
               }else{
                   Toast.makeText(this, "请填写完信息", Toast.LENGTH_SHORT).show();
               }
               break;
       }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
    private void requestSMS(){
        phoneNumber = editText.getText().toString();
        BmobSMS.requestSMSCode(this, phoneNumber, "短信模板名称", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                if (e == null){
                    buttonVC.setClickable(false);
                    Log.i("bmob","短信id：" + integer);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
