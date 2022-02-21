# AsyncTask(Deprecated) Alternative

AsyncTask is deprecated and it is suggested to use Kotlin coroutines instead.
https://developer.android.com/reference/android/os/AsyncTask

The other alternative is [suggested](https://stackoverflow.com/questions/58767733/the-asynctask-api-is-deprecated-in-android-11-what-are-the-alternatives) to use **Executor** and **Handler** which is kind of same as using AsyncTask and should be avoided but in case somebody want to use it they can implement it like below.

``` 
Executor executor = Executors.newSingleThreadExecutor(); 
Handler handler = new Handler(Looper.getMainLooper());

//Run any main thread task like launching progress bar
...
executor.execute(() -> {
        //Do some background operation here(e.g fetching data from server etc)     
        handler.post(() -> {
         //Do some main thread operation like stopping progress bar or updating UI with fecthed data 
        });
    }); 
```

--------------------------------------------------------------------------------------------------------------------------------------------------------------

I have implemented little bit generic way to do the above operation instead of writing Executors and Handler everytime we can use the below class and also we can specify the response type of doInBackground.

```
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

```

We can call the above class like below, We can specify the return type while initializing class and it will provide type safety for doInBackground result.

```
new CustomAsyncTask<String>(new CustomAsyncTask.AsyncTaskCallback<String>() {
                @Override
                public void onPreExecute() {
                    
                }

                @Override
                public String doInBackground() {
                    return null;
                }

                @Override
                public void onPostExecute(String result) {

                }
            }).execute();
```



https://user-images.githubusercontent.com/7566567/154971937-cf4d7002-88dd-461c-b345-b5442d6d0716.mp4


