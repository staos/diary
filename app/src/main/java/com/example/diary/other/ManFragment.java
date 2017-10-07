package com.example.diary.other;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diary.R;
import com.example.diary.activity.FeedbackActivity;
import com.example.diary.activity.LoginActivity;
import com.example.diary.activity.MainActivity;
import com.example.diary.activity.MyCollectionActivity;
import com.example.diary.activity.modata;
import com.example.diary.activity.modificationPassword;
import com.example.diary.bean._User;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;
import com.tencent.connect.UserInfo;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import it.sephiroth.android.library.easing.Linear;

/**
 * Created by 周要明 on 2017/8/4.
 */
public class ManFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView backgroundImage;
    private CircleImageView headImage;
    private PopupWindowSelectPhoto popupWindowSelectPhoto;
    private static final int TAKE_CAMERA = 1;
    private static final int HEAD_CROP = 2;
    private static final int BACKGROUND_CROP = 3;
    private Uri inputImageUri;
    private Uri outputImageUri;
    private Uri outputTwoImageUri;
    private Uri outputHeadImageUri;
    private Uri outputHeadTwoImageUri;
    private String headUri;
    private String backgroundUri;
    private int width;
    private static final int REQUEST_CODE_CHOOSE = 4;
    List<Uri> mSelected;
    private int headOrBackground;
    private TextView nickName;
    private TextView autograph;
    private int TakePhotoOrAlbum;
    private File fileHeadTwo;
    private File fileBackgroundTwo;
    private static final String TAG = "ManFragment";
    _User user = _User.getCurrentUser(_User.class);
    private String userId = user.getObjectId();
    private boolean sOne;
    private boolean sTwo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( view == null){
            view = inflater.inflate(R.layout.man_fragment,container,false);
        }
        BmobQuery<_User> query = new BmobQuery<>();
        query.getObject(userId, new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if ( e == null){
                    sOne = user.getSwitchOne();
                    sTwo = user.getSwitchTwo();
                }
            }
        });
        headOrBackground = 0;
        createTakePhotoImageFile();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        width = dm.widthPixels;
        nickName = (TextView)view.findViewById(R.id.nick_name);
        autograph = (TextView)view.findViewById(R.id.autograph);
        backgroundImage = (ImageView)view.findViewById(R.id.background_photo);
        backgroundImage.setOnClickListener(this);
        headImage = (CircleImageView)view.findViewById(R.id.head_photo);
        headImage.setOnClickListener(this);
        SwitchButton switchOne = (SwitchButton)view.findViewById(R.id.switchOne);
        switchOne.setChecked(sOne);
        switchOne.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                _User user1 = new _User();
                user1.setSwitchOne(isChecked);
                user1.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if ( e == null){
                            Log.d(TAG, "done: 更新成功");
                        }else{
                            Log.d(TAG, "done: 更新失败");
                        }
                    }
                });
            }
        });
        SwitchButton switchTwo = (SwitchButton)view.findViewById(R.id.switchTwo);
        switchTwo.setChecked(sTwo);
        switchTwo.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                _User user1 = new _User();
                user1.setSwitchTwo(isChecked);
                user1.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if ( e == null){
                            Log.d(TAG, "done: 更新成功");
                        }else{
                            Log.d(TAG, "done: 更新失败");
                        }
                    }
                });
            }
        });
        Button signOutLogin = (Button)view.findViewById(R.id.signOutLogin);
        signOutLogin.setOnClickListener(this);
        LinearLayout manMo = (LinearLayout)view.findViewById(R.id.dataModification);
        manMo.setOnClickListener(this);
        LinearLayout pwm = (LinearLayout)view.findViewById(R.id.pwm);
        pwm.setOnClickListener(this);
        LinearLayout back = (LinearLayout)view.findViewById(R.id.feedback);
        back.setOnClickListener(this);
        LinearLayout myCollection = (LinearLayout)view.findViewById(R.id.collection);
        myCollection.setOnClickListener(this);
        initializationView();
        return view;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.head_photo:
                popupWindowSelectPhoto = new PopupWindowSelectPhoto(MyApplication.getContext(),itemOnClick);
                popupWindowSelectPhoto.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                headOrBackground = 1;
                break;
            case R.id.background_photo:
                popupWindowSelectPhoto = new PopupWindowSelectPhoto(MyApplication.getContext(),itemOnClick);
                popupWindowSelectPhoto.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                headOrBackground = 2;
                break;
            case R.id.dataModification:
                Intent intent = new Intent(getActivity(),modata.class);
                startActivityForResult(intent,5);
                break;
            case R.id.signOutLogin:
                BmobUser.logOut();
                Intent signOutIntent = new Intent(getActivity(),LoginActivity.class);
                startActivity(signOutIntent);
                break;
            case R.id.pwm:
                Intent pwmIntent = new Intent(getActivity(),modificationPassword.class);
                startActivity(pwmIntent);
                break;
            case R.id.feedback:
                Intent backIntent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(backIntent);
                break;
            case R.id.collection:
                Intent collectionIntent = new Intent(getActivity(), MyCollectionActivity.class);
                startActivity(collectionIntent);
                break;
        }
    }
    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindowSelectPhoto.dismiss();
            switch(v.getId()){
                case R.id.take_photo:
                    takePhoto();
                    TakePhotoOrAlbum = 1;
                    break;
                case R.id.album:
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                               Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                           ActivityCompat.requestPermissions(getActivity(),new
                                   String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                       }else{
                           chooseFromGallery();
                       }
                   }else{
                       chooseFromGallery();
                   }
                   TakePhotoOrAlbum = 2;
                    break;
                case R.id.cancel:
                    break;
            }
        }
    };
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,inputImageUri);
        startActivityForResult(intent, TAKE_CAMERA);
    }
    private void chooseFromGallery(){
        Matisse.from(ManFragment.this)
                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG,MimeType.GIF))
                .countable(false)
                .maxSelectable(1)
                 .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
    private void startBackgroundImageZoom(Uri inputUri,Uri outputUri ){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(inputUri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", width);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
        startActivityForResult(intent, BACKGROUND_CROP);
    }
    private void startHeadImageZoom(Uri inputUri,Uri outputUri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(inputUri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
        startActivityForResult(intent, HEAD_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case TAKE_CAMERA:
                if (inputImageUri != null){
                   if (headOrBackground == 1){
                       createHeadOneFile();
                       startHeadImageZoom(inputImageUri,outputHeadImageUri);
                   }else{
                       createBackgroundImageFile();
                       startBackgroundImageZoom(inputImageUri,outputImageUri);
                   }
                }
                break;
            case REQUEST_CODE_CHOOSE:
                if (data != null){
                    mSelected = Matisse.obtainResult(data);
                    for (Uri item: mSelected){
                        if (headOrBackground == 1){
                            createHeadTwoFile();
                            startHeadImageZoom(item,outputHeadTwoImageUri);
                        }else{
                            createBackgroundTwoImageFile();
                            startBackgroundImageZoom(item,outputTwoImageUri);
                        }
                    }
                }
                break;
            case BACKGROUND_CROP:
                if (outputImageUri != null || outputTwoImageUri != null){
                    if (TakePhotoOrAlbum == 1){
                        Bitmap bitmap = decodeUriBitmap(MyApplication.getContext(),outputImageUri);
                        backgroundImage.setImageBitmap(bitmap);
                        uploadImage(outputImageUri,2);
                    }else if (TakePhotoOrAlbum == 2){
                        Bitmap bitmap = decodeUriBitmap(MyApplication.getContext(),outputTwoImageUri);
                        backgroundImage.setImageBitmap(bitmap);
                        uploadImage(outputTwoImageUri,2);
                    }
                    createTakePhotoImageFile();
                }
                break;
            case HEAD_CROP:
                if (outputHeadImageUri != null || outputHeadTwoImageUri != null){
                   if (TakePhotoOrAlbum == 1){
                       Bitmap bitmap = decodeUriBitmap(MyApplication.getContext(),outputHeadImageUri);
                       headImage.setImageBitmap(bitmap);
                       uploadImage(outputHeadImageUri,1);
                   }else if(TakePhotoOrAlbum == 2){
                       Bitmap bitmap = decodeUriBitmap(MyApplication.getContext(),outputHeadTwoImageUri);
                       headImage.setImageBitmap(bitmap);
                       uploadImage(outputHeadTwoImageUri,1);
                   }
                    createTakePhotoImageFile();
                }
                break;
            case 5:
                _User user = _User.getCurrentUser(_User.class);
                nickName.setText(user.getNickName().toString());
                autograph.setText(user.getAutograph().toString());
                Log.d(TAG, "onActivityResult: UI刷新成功");
                break;
            default:
                break;
        }
    }
    private Bitmap  decodeUriBitmap(Context context,Uri uri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    private void createTakePhotoImageFile(){
        File fileTwo = new File(MyApplication.getContext().getExternalCacheDir(),"input_image.jpg");
        if (fileTwo.exists()){
            fileTwo.delete();
        }
        try {
            fileTwo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputImageUri = Uri.fromFile(fileTwo);//拍照到的图片的路径
    }
    private void createBackgroundImageFile(){
        File fileBackgroundOne = new File(MyApplication.getContext().getExternalCacheDir(),"output_image.jpg");
        if (fileBackgroundOne.exists()){
            fileBackgroundOne.delete();
        }
        try {
            fileBackgroundOne.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputImageUri = Uri.fromFile(fileBackgroundOne);//拍照图片裁剪得到背景图片的路径
    }
    private void createBackgroundTwoImageFile(){
        fileBackgroundTwo = new File(Environment.getExternalStorageDirectory(),"output_Two_image.jpg");
        if (fileBackgroundTwo.exists()){
            fileBackgroundTwo.delete();
        }
        try {
            fileBackgroundTwo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputTwoImageUri = Uri.fromFile(fileBackgroundTwo);//从图库选择的图片裁剪得到背景图片的路径
    }
    private void createHeadOneFile(){
        File fileHeadOne = new File(Environment.getExternalStorageDirectory(),"output_head_image.jpg");
        if (fileHeadOne.exists()){
            fileHeadOne.delete();
        }
        try {
            fileHeadOne.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputHeadImageUri = Uri.fromFile(fileHeadOne);//拍照裁剪得到头像图片的路径
    }
    private void createHeadTwoFile(){
        fileHeadTwo = new File(Environment.getExternalStorageDirectory(),"output_head_Two_image.jpg");
        if (fileHeadTwo.exists()){
            fileHeadTwo.delete();
        }
        try {
            fileHeadTwo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputHeadTwoImageUri = Uri.fromFile(fileHeadTwo);//从图库选择图片裁剪得到头像图片的路径
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseFromGallery();
                }else{
                    Toast.makeText(MyApplication.getContext(), "拒绝读取内存权限，无法选择照片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void initializationView(){
        Uri imageOne;
        Uri imageTwo;
        _User user = BmobUser.getCurrentUser(_User.class);
        if (user.getHeadImageUrl() != null){
            imageOne = Uri.parse(user.getHeadImageUrl());
            Glide.with(MyApplication.getContext())
                    .load(imageOne)
                    .into(headImage);
        }
        if(user.getBackgroundUrl() != null){
            imageTwo = Uri.parse(user.getBackgroundUrl());
            Glide.with(MyApplication.getContext())
                    .load(imageTwo)
                    .into(backgroundImage);
        }
        if (user.getAutograph() != null){
            autograph.setText(user.getAutograph().toString());
        }
        nickName.setText(user.getNickName());
    }
    private void uploadImage(Uri uri, int bh){
        if (bh == 1){
            final BmobFile bmobFile = new BmobFile(fileHeadTwo);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if ( e == null){
                        Log.d(TAG, "done: 图片上传成功");
                        headUri = bmobFile.getFileUrl();
                        _User user = BmobUser.getCurrentUser(_User.class);
                        user.setHeadImageUrl(headUri);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    Log.d(TAG, "用户更新信息成功");
                                }else{
                                    Log.d(TAG, "用户更新信息失败");
                                }
                            }
                        });
                        Log.d(TAG, "done: " + headUri);
                    }else{
                        Log.d(TAG, "done: 图片上传失败");
                    }
                }
            });
        }else if(bh == 2){
            final BmobFile bmobFile = new BmobFile(fileBackgroundTwo);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        Log.d(TAG, "done: 图片上传成功");
                        backgroundUri = bmobFile.getFileUrl();
                        _User user = BmobUser.getCurrentUser(_User.class);
                        user.setBackgroundUrl(backgroundUri);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    Log.d(TAG, "用户更新信息成功");
                                }else{
                                    Log.d(TAG, "用户更新信息失败");
                                }
                            }
                        });
                        Log.d(TAG, "done: " + backgroundUri);
                    }else{
                        Log.d(TAG, "done: 图片上传失败 " + e.getMessage());
                    }
                }
            });
        }
    }
}
