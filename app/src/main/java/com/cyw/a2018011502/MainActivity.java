package com.cyw.a2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.imageView);

    }
    //用Stream(原始)方法抓圖
    public void click1(View v)
    {

        new Thread() {
            @Override
            public void run() {
                super.run();
                String str_url = "https://5.imimg.com/data5/UH/ND/MY-4431270/red-rose-flower-500x500.jpg";
                URL url;
                try {
                    url = new URL(str_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    //抓到存到ByteArrayOutputSteam
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    //用byte陣列抓,依次抓1024個byte
                    byte[] buf = new byte[1024];
                    int length;
                    //當length=-1就是沒檔案了,但每次要寫進去的是目前的length, 因為最後一次可能不滿1024byte
                    while ((length = inputStream.read(buf)) != -1)
                    {
                        bos.write(buf, 0, length);
                    }
                    byte[] results = bos.toByteArray();
                    //寫完之後要把byte array轉成bmp
                    final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);
                    //然後轉回主執行緒將圖片放入
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bmp);
                        }
                    });



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
