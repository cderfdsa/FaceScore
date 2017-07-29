package com.yyyclj.facescore.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyyclj.facescore.R;
import com.yyyclj.facescore.bean.DetectInfo;
import com.yyyclj.facescore.bean.FaceInfo;
import com.yyyclj.facescore.bean.FacePositionInfo;
import com.yyyclj.facescore.utils.CheckPermission;
import com.yyyclj.facescore.utils.Constants;
import com.yyyclj.facescore.utils.FaceUtil;
import com.yyyclj.facescore.utils.OkHttpClientMgr;

import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_CODE =1;
    private ImageView mPhoto;
    private Button selectBtn;
    private Button detectBtn;
    private TextView infoTv;

    //本地图片路径
    private String imagePath = null;
    private Paint mypaint;
    private Bitmap myBitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mypaint = new Paint();
        initViews();
        //6.0检查权限
        CheckPermission.verifyBLEPermissions(this);
    }

    private void initViews(){
        mPhoto = (ImageView)findViewById(R.id.img_photo);
        selectBtn = (Button)findViewById(R.id.btn_select);
        selectBtn.setOnClickListener(this);
        detectBtn = (Button)findViewById(R.id.btn_detect);
        detectBtn.setOnClickListener(this);
        infoTv = (TextView)findViewById(R.id.tv_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select:   //本地选取图片
                //获取系统选择图片intent
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //开启选择图片功能响应码为PICK_CODE
                startActivityForResult(intent,PICK_CODE);
                break;
            case R.id.btn_detect:
                if (myBitmapImage == null)
                    return;
                FaceUtil.dectect(myBitmapImage, imagePath, new OkHttpClientMgr.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.i("TAG", "onError: " + request.toString());
                        infoTv.setText("请求出错");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        infoTv.setText("请求成功");
                        Gson gson = new Gson();
                        DetectInfo detectInfo = gson.fromJson(response, DetectInfo.class);
                        Log.i("TAG", "onResponse: " + detectInfo.toString());
                        String errorMsg = detectInfo.getError_message();
                        if (errorMsg.equals(Constants.CONCURRENCY_LIMIT_EXCEEDED)){
                            infoTv.setText("并发数限制");
                            return;
                        }
                        List<Object> faceInfoList = detectInfo.getFaces();
                        if (faceInfoList == null || faceInfoList.size() == 0){
                            Log.i("TAG", "onResponse: 没有发现人脸");
                            infoTv.setText("没有发现人脸");
                            return;
                        }
                        FaceInfo faceInfo = gson.fromJson(faceInfoList.get(0).toString(), FaceInfo.class);

                        Log.i("TAG", "onResponse: " + faceInfo.getFace_rectangle().toString());
                        //绘制脸框
                        drawFrame(faceInfo.getFace_rectangle());
                        //让主线程的相框刷新
                        mPhoto.setImageBitmap(myBitmapImage);
                        //显示个人信息，性别，年龄
                        infoTv.setText("年龄: " + faceInfo.getAttributes().getAge() + " 性别: " + faceInfo.getAttributes().getGender());
                    }
                });
                break;
        }
    }

    //设置响应intent请求
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == PICK_CODE)
        {
            if(intent != null)
            {
                //获取图片路径
                //获取所有图片资源
                Uri uri = intent.getData();
                //设置指针获得一个ContentResolver的实例
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                cursor.moveToFirst();
                //返回索引项位置
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                //返回索引项路径
                imagePath = cursor.getString(index);
                cursor.close();
                //这个jar包要求请求的图片大小不得超过3m所以要进行一个压缩图片操作
                resizePhoto();
                mPhoto.setImageBitmap(myBitmapImage);
            }
        }
    }

    private void resizePhoto() {
        //得到BitmapFactory的操作权
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 如果设置为 true ，不获取图片，不分配内存，但会返回图片的高宽度信息。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        //计算宽高要尽可能小于1024
        double ratio = Math.max(options.outWidth*1.0d/1024f,options.outHeight*1.0d/1024f);
        //设置图片缩放的倍数。假如设为 4 ，则宽和高都为原来的 1/4 ，则图是原来的 1/16 。
        options.inSampleSize = (int)Math.ceil(ratio);
        //我们这里并想让他显示图片所以这里要置为false
        options.inJustDecodeBounds = false;
        //利用Options的这些值就可以高效的得到一幅缩略图。
        myBitmapImage = BitmapFactory.decodeFile(imagePath, options);
    }

    private void drawFrame(FacePositionInfo position){
        //新建一个Bitmap使用它作为Canvas操作的对象
        Bitmap bitmap = Bitmap.createBitmap(myBitmapImage.getWidth(),myBitmapImage.getHeight(),myBitmapImage.getConfig());
        //实例化一块画布
        Canvas canvas = new Canvas(bitmap);
        //把原图先画到画布上面
        canvas.drawBitmap(myBitmapImage, 0, 0, null);
        float top = position.getTop();
        float left = position.getLeft();
        float height = position.getHeight();
        float width = position.getWidth();
        Log.i("TAG", "drawFrame: " + top + "left" + left);
        //设置画笔颜色
        mypaint.setColor(Color.BLUE);
        //设置画笔宽度
        mypaint.setStrokeWidth(5);
        canvas.drawLine(left, top, left + width, top, mypaint);
        canvas.drawLine(left, top, left, top + height, mypaint);
        canvas.drawLine(left + width, top + height, left, top + height, mypaint);
        canvas.drawLine(left + width, top + height, left + width, top, mypaint);
        //得到新的bitmap
        myBitmapImage = bitmap;

    }
}
