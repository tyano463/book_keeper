package jp.co.interplan.bookkeeper;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static void fetchUrl(String urlString, UrlResponseCallback callback) {
        new FetchUrlTask(callback).execute(urlString);
    }

    private static class FetchUrlTask extends AsyncTask<String, Void, String> {
        private UrlResponseCallback callback;
        private Exception exception;

        public FetchUrlTask(UrlResponseCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                exception = e;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (exception != null) {
                callback.onError(exception);
            } else {
                callback.onResponse(result);
            }
        }
    }
}
