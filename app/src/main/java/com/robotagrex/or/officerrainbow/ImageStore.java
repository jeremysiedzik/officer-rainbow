package com.robotagrex.or.officerrainbow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.graphics.*;
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
                        bitmapList.add(urlImageToBitmap("http://placehold.it/150x150"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }

    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
        Bitmap result;
        URL url = new URL(imageUrl);
        result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return result;
    }
}
