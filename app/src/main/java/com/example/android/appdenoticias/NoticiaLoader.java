package com.example.android.appdenoticias;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrik on 23/03/2018.
 */

public class NoticiaLoader extends AsyncTaskLoader<List<Noticia>>{

    private String mUrl;

    public NoticiaLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Noticia> loadInBackground() {
        if(mUrl==null)
            return null;
        URL url = createUrl(mUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException exception) {
            Log.e(MainActivity.class.getSimpleName(), "Error with doInBackground", exception);
        }

        ArrayList<Noticia> noticias = extractFromJson(jsonResponse);
        return noticias;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(MainActivity.class.getSimpleName(), "Error with createUrl", exception);
        }
        return url;
    }//fim createUrl

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(R.integer.read_timeout /* milliseconds */);
            urlConnection.setConnectTimeout(R.integer.connect_timeout /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }
        catch (IOException exception) {
            Log.e(MainActivity.class.getSimpleName(), "Error with makeHttpRequest", exception);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }//fim makeHttpRequest

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }//fim readFromStream

    private ArrayList<Noticia> extractFromJson(String noticiaJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(noticiaJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray itensArray = response.getJSONArray("results");

            ArrayList<Noticia> noticias = new ArrayList<>();

            for (int i=0; i< itensArray.length();i++) {
                JSONObject primeiroItem = itensArray.getJSONObject(i);
                noticias.add(new Noticia(primeiroItem.getString("webTitle"),primeiroItem.getString("sectionName")
                        ,primeiroItem.getString("webPublicationDate"),primeiroItem.getString("webUrl")));
            }
            return noticias;

        } catch (JSONException exception) {
            Log.e(MainActivity.class.getSimpleName(), "Error with extractFromJson", exception);
        }
        return null;
    }//fim extractFromJson
}
