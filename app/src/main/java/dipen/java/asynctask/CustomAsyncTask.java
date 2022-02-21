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
        //Call it before executing background task
        void onPreExecute();

        //Execute background thread and call the below method
        ResponseType doInBackground();

        //Once the background thread finishes call this method on main thread
        void onPostExecute(ResponseType result);
    }

    public CustomAsyncTask(AsyncTaskCallback<ResponseType> asyncTaskCallback) {
        this.asyncTaskCallback = asyncTaskCallback;

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void execute() {
        //Executing on caller thread(Assuming that user is calling CustomAsyncTask on main thread)
        asyncTaskCallback.onPreExecute();

        executor.execute(() -> {
            //Background work here
            ResponseType res = asyncTaskCallback.doInBackground();

            //Execute on main thread
            handler.post(() -> {
                //UI Thread work here
                asyncTaskCallback.onPostExecute(res);
            });
        });
    }
}
