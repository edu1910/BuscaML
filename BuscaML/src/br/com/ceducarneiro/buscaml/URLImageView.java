package br.com.ceducarneiro.buscaml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.net.URL;

public class URLImageView extends FrameLayout {

    private ProgressBar progress;
    private ImageView image;
    private ImageAsyncTask task;

    public URLImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.url_image_view, this);

        progress = (ProgressBar) findViewById(R.id.progress);
        image = (ImageView) findViewById(R.id.image);

        progress.setVisibility(View.INVISIBLE);
    }

    public void setImageURL(String url) {
        progress.setVisibility(View.VISIBLE);
        image.setImageDrawable(null);

        if (task != null) {
            task.cancel(true);
        }

        task = new ImageAsyncTask();
        task.execute(url);

    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;

            try {
                URL url = new URL(params[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception ignored) {
                /* Empty */
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progress.setVisibility(View.INVISIBLE);

            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }

    }

}
