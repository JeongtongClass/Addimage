package com.example.addimage;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    static final String TAG = "MainActivity";
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imageView);
        db = new DatabaseHelper(this);

    }

    //로드버튼 클릭시 실행
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    //이미지 선택작업을 후의 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outstream);
                byte imageInByte[] = outstream.toByteArray();
                long t = db.storeImage(imageInByte);
                if(t > 0)
                {
                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
                }

                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                imgView.setImageBitmap(scaled);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
   /* public void addImage(View v){
        Bitmap image =BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,outstream);
        byte imageInByte[] = outstream.toByteArray();
        long t = db.storeImage(imageInByte);
        if(t > 0)
        {
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
        }
    }*/
    public void getImage(View v)
    {
        imgView.setImageBitmap(db.getImage());
    }
}
