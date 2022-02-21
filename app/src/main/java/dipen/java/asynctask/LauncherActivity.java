package dipen.java.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class LauncherActivity extends AppCompatActivity {

    private LinearProgressIndicator linearProgressIndicator;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        initView();
    }

    private void initView() {
        linearProgressIndicator = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.result_iv);

        findViewById(R.id.start_counter_btn).setOnClickListener(view -> {


            new CustomAsyncTask<Bitmap>(new CustomAsyncTask.AsyncTaskCallback<Bitmap>() {
                @Override
                public void onPreExecute() {
                    linearProgressIndicator.show();
                    imageView.setImageBitmap(null);
                }

                @Override
                public Bitmap doInBackground() {
                    //Creating dummy background task and returning image once the background task is finished.
                    //We can replace this with actual network call and download the data from server.
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return getDummyBitmap();
                }

                @Override
                public void onPostExecute(Bitmap result) {
                    linearProgressIndicator.hide();
                    imageView.setImageBitmap(result);
                }
            }).execute();
        });
    }

    private Bitmap getDummyBitmap() {
        String encodedImage = "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";
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}