package com.robotagrex.or.officerrainbow;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ImageViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final Book[] books;

    // 1
    ImageViewAdapter(Context context, Book[] books) {
        this.mContext = context;
        this.books = books;
    }

    // 2 test
    @Override
    public int getCount() {
        return books.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final Book book = books[position];

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_book, parent, false);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);
        final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_favorite);

        // 4
        imageView.setImageResource(book.getImageResource());
        nameTextView.setText(mContext.getString(book.getName()));
        authorTextView.setText(mContext.getString(book.getAuthor()));
        imageViewFavorite.setImageResource(
                book.getIsFavorite() ? R.drawable.star_enabled : R.drawable.star_disabled);
        return convertView;
    }
}
