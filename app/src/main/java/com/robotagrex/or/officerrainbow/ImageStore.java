package com.robotagrex.or.officerrainbow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.graphics.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;

public class ImageStore extends AppCompatActivity {

    public GridView imageGrid;
    public ArrayList<Bitmap> bitmapList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagestore);

        Button buttonsave = (Button) findViewById(R.id.buttonsave);
        assert buttonsave != null;

        this.imageGrid = (GridView) findViewById(R.id.gridview);
        this.bitmapList = new ArrayList<>();

        imageGrid.setAdapter(new ImageAdapter(this, bitmapList));

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(ImageStore.this, UI.class);
                startActivity(qoneintent);
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 10; i++) {
                        System.out.println("iterating http bitmap pull #"+i);
                        bitmapList.add(urlImageToBitmap("http://feed.robotagrex.com/404.png"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        this.imageGrid.setAdapter(new ImageAdapter(this, this.bitmapList));

    }

    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
        try {
            //URL url = new URL("http://www.helpinghomelesscats.com/images/cat1.jpg");
            URL url = new URL(imageUrl);
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            return BitmapFactory.decodeByteArray(data, 0, data.length);
            //imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Bitmap result;
        //URL url = new URL(imageUrl);
        //result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        //return result;
        return null;
    }
}
