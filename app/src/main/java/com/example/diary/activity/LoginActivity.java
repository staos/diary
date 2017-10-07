package com.example.diary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.bean._User;
import com.example.diary.other.MyApplication;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText phoneNumber;
    private EditText password;
    private Tencent mTencent;//qq主要操作对象
    private BaseUiListener mIUiListener;//qq授权监听器
    private IUiListener userInfoListener;//获取用户信息监听器
    private String nickName;
    private String headImage;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTencent = Tencent.createInstance("1106252757",this.getApplicationContext());//Tencent是SDK的功能入口，所有的接口调用都得通过Tencent进行调用
        mIUiListener = new BaseUiListener();
        Button registerButton = (Button)findViewById(R.id.registerAccount);
        registerButton.setOnClickListener(this);
        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        ImageButton qqLoginButton = (ImageButton) findViewById(R.id.qq_login);
        qqLoginButton.setOnClickListener(this);
        ImageButton weiboLoginButton = (ImageButton)findViewById(R.id.weibo_login);
        weiboLoginButton.setOnClickListener(this);
        phoneNumber = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                if (!phoneNumber.getText().toString().equals("") && !password.getText().toString().equals("")){
                    BmobUser user = new BmobUser();
                    user.setUsername(phoneNumber.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null){
                                _User newUser = BmobUser.getCurrentUser(_User.class);
                                newUser.setNickName("昵称");
                                newUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Log.d(TAG, "更新用户个人信息成功");
                                        }else{
                                            Log.d(TAG, e.getMessage());
                                        }
                                    }
                                });
                                Intent intent = new Intent(MyApplication.getContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else if(phoneNumber.getText().toString().equals("")){
                    Toast.makeText(this, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equals("")){
                    Toast.makeText(this, "密码号码不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.registerAccount:
                Intent intent = new Intent(this,RegisterZeroActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_login:
                qqLogin();
                break;
            case R.id.weibo_login:
                break;
        }
    }
    private void qqLogin(){
        if (!mTencent.isSessionValid()){
            mTencent.login(this,"all",mIUiListener);
        }
    }
    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object response) {
            if(response != null){
                JSONObject jsonObject = (JSONObject)response;
                try{
                    String openid = jsonObject.getString("openid");//用户身份的唯一标识
                    String token = jsonObject.getString("access_token");//接口调用凭证
                    String expires = jsonObject.getString("expires_in");//token的有效时间
                    mTencent.setOpenId(openid);
                    mTencent.setAccessToken(token, expires);
                    //构造BmobThirdUserAuth(第三方授权信息)对象
                    BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq",token,expires,openid);
                    BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
                        @Override
                        public void done(JSONObject jsonObject, BmobException e) {
                          if (e == null){
                              getUserInfo();
                              Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                              startActivity(intent);
                              finish();
                          }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void  getUserInfo(){
        UserInfo userInfo = new UserInfo(LoginActivity.this,mTencent.getQQToken());
        userInfoListener = new IUiListener() {
            @Override
            public void onComplete(Object response) {
                try{
                    JSONObject jsonData = (JSONObject)response;
                    nickName = jsonData.getString("nickname");
                    headImage = jsonData.getString("figureurl_qq_2");
                    _User user = BmobUser.getCurrentUser(_User.class);
                    user.setNickName(nickName);
                    user.setHeadImageUrl(headImage);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.d(TAG, "更新用户个人信息成功");
                            }else{
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            /**
             *"is_yellow_year_vip": "0",
              "ret": 0,
             "figureurl_qq_1":
             "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
             "figureurl_qq_2":
             "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
             "nickname": "小罗",
             "yellow_vip_level": "0",
             "msg": "",
             "figureurl_1":
             "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
             "vip": "0",
             "level": "0",
             "figureurl_2":
             "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
             "is_yellow_vip": "0",
             "gender": "男",
             "figureurl":
             "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
             *
             */

            @Override
            public void onError(UiError uiError) {
                Log.d(TAG, "onError: 获取用户信息失败");
            }

            @Override
            public void onCancel() {

            }
        };
        userInfo.getUserInfo(userInfoListener);
    }

}
