package dipen.java.asynctask;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CustomAsyncTask<ResponseType> {
    private final AsyncTaskCallback<ResponseType> asyncTaskCallback;
    private final ExecutorService executor;
    private final Handler handler;

    interface AsyncTaskCallback<ResponseType> {
        void onPreExecute();

        ResponseType doInBackground();

        void onPostExecute(ResponseType result);
    }

    public CustomAsyncTask(AsyncTaskCallback<ResponseType> asyncTaskCallback) {
        this.asyncTaskCallback = asyncTaskCallback;

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void execute() {
        executor.execute(() -> {
            handler.post(() -> {
                //UI Thread work here
                asyncTaskCallback.onPreExecute();
            });

            //Background work here
            ResponseType res = asyncTaskCallback.doInBackground();

            handler.post(() -> {
                //UI Thread work here
                asyncTaskCallback.onPostExecute(res);
            });
        });
    }
}
