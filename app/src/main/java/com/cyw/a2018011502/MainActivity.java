package com.cyw.a2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
ImageView img,img2;
ProgressBar pb;
TextView tv,tv2,tv3,tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.imageView);
        img2=(ImageView)findViewById(R.id.imageView2);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        tv=(TextView)findViewById(R.id.textView);
        tv2=(TextView)findViewById(R.id.textView2);
        tv3=(TextView)findViewById(R.id.textView3);
        tv4=(TextView)findViewById(R.id.textView4);
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
                    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    //用byte陣列抓,依次抓1024個byte
                    byte[] buf = new byte[1024];
                    int length;
                    int sum=0;
                    final int totallength=conn.getContentLength();
                    //當length=-1就是沒檔案了,但每次要寫進去的是目前的length, 因為最後一次可能不滿1024byte
                    while ((length = inputStream.read(buf)) != -1)
                    {
                        bos.write(buf, 0, length);

                        sum+=length;
                        final int temp=sum;
                        Log.d("Net",String.valueOf(temp)+"%"+(int)((100*temp/totallength)));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress((int)((100*temp/totallength)));
                                tv.setText(String.valueOf(temp)+"/"+String.valueOf(totallength));
                            }
                        });
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

    //SyncTask倒數計時
    public void click2(View v)
    {
        MyTask task = new MyTask();
        task.execute(10);
    }
    //利用AsyncTask取代Thread與Runnable, 三個引數, 初始值, ?, 最終字串
    class MyTask extends AsyncTask <Integer, Integer, String>
    {
        @Override
        //把結果印出(s)就是okay
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv3.setText(s);
        }

        @Override
        //進度顯示,就是publishProgress傳回值
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tv2.setText(String.valueOf(values[0]));
        }

        @Override
        //背景執行,會傳回結果,目前設為okay
        protected String doInBackground(Integer... integers) {
            int i;
            for (i=0;i<=integers[0]; i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TASK", "doInBackground, i:" + i);
                publishProgress(i);//公布進度
            }
            return "okay";
        }
    }


    //用SyncTask抓圖
    public void click3(View v)
    {
        MyTaskPic task = new MyTaskPic();
        task.execute("https://5.imimg.com/data5/UH/ND/MY-4431270/red-rose-flower-500x500.jpg");
    }
    //利用AsyncTask取代Thread與Runnable, 三個引數, 輸入值, 進度值, 傳回結果
    class MyTaskPic extends AsyncTask <String, Integer, Bitmap>
    {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img2.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int i=0;
            while (i!=-1) {
                tv4.setText(String.valueOf(values[i]));
                Log.d("Task","pic i="+String.valueOf(values[i]));
                break;
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String str_url = strings[0];
            URL url;
            try {
                url = new URL(str_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                //抓到存到ByteArrayOutputSteam
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //用byte陣列抓,依次抓1024個byte
                byte[] buf = new byte[1024];
                int length;
                int sum=0;
                final int totallength=conn.getContentLength();
                //當length=-1就是沒檔案了,但每次要寫進去的是目前的length, 因為最後一次可能不滿1024byte
                while ((length = inputStream.read(buf)) != -1)
                {
                    bos.write(buf, 0, length);
                    sum+=length;
                    final int temp=sum;
                    Log.d("Net",String.valueOf(temp)+"%"+(int)((100*temp/totallength)));
                    publishProgress((int)((100*temp/totallength)));
                }
                byte[] results = bos.toByteArray();
                //寫完之後要把byte array轉成bmp
                final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
