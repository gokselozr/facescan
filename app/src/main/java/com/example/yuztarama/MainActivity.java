package com.example.yuztarama;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {
    Button bulbtn;
    ImageView faceimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faceimage=(ImageView)findViewById(R.id.imageView);
        bulbtn=(Button)findViewById(R.id.button);
        final Bitmap myBitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.raw.palvin);
        faceimage.setImageBitmap(myBitmap);
        final Paint rectPaint=new Paint();
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.YELLOW);
        rectPaint.setStyle(Paint.Style.STROKE);
        final Bitmap tempBitmap=Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(),Bitmap.Config.RGB_565);
        final Bitmap mutableBitmap = tempBitmap.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas=new Canvas(mutableBitmap);
        canvas.drawBitmap(myBitmap,0,0,null);

        bulbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetector faceDetector =new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(true)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();

                if (!faceDetector.isOperational())
                {
                    Toast.makeText(MainActivity.this, "Yüz tanıma sistemi cihazınıza yüklenmedi", Toast.LENGTH_SHORT).show();
                }

                Frame frame=new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> sparseArray=faceDetector.detect(frame);

                for (int i=0;i<sparseArray.size();i++)
                {
                    Face face=sparseArray.valueAt(i);
                    float x1=face.getPosition().x;
                    float y1=face.getPosition().y;
                    float x2=x1+face.getWidth();
                    float y2=y1+face.getHeight();
                    RectF rectF=new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);
                }

                faceimage.setImageDrawable(new BitmapDrawable(getResources(),mutableBitmap));
            }
        });
    }
}
