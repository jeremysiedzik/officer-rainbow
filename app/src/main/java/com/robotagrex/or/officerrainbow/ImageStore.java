package com.robotagrex.or.officerrainbow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.graphics.*;
import java.net.*;


import java.util.ArrayList;

public class ImageStore extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagestore);

        GridView imageGrid = (GridView) findViewById(R.id.gridview);
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        try {
            for(int i = 0; i < 10; i++) {
                bitmapList.add(urlImageToBitmap("http://placehold.it/150x150"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageGrid.setAdapter(new ImageAdapter(this, bitmapList));

    }

    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
        Bitmap result;
        URL url = new URL(imageUrl);
        result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return result;
    }
}
