package com.example.android.appdenoticias;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Noticia>> {

    private static final int NOTICIA_LOADER_ID= 1;
    private NoticiaAdapter mAdapter;
    private final String USGS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=test&q=";
    private String mPalavraChave = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isOnline()) {
            TextView texto =(TextView) findViewById(R.id.erro);
            texto.setText(R.string.busca_noticia);
        }
        else {
            TextView texto =(TextView) findViewById(R.id.erro);
            texto.setText(R.string.sem_internet);
        }

    }

    @Override
    public Loader<List<Noticia>> onCreateLoader(int id, Bundle args) {
        return new NoticiaLoader(this,USGS_REQUEST_URL+mPalavraChave);
    }

    @Override
    public void onLoadFinished(Loader<List<Noticia>> loader, List<Noticia> noticias) {
        mAdapter.clear();
        if (mAdapter != null) {
            if(noticias==null){
                if(isOnline()){
                    TextView texto = (TextView) findViewById(R.id.erro);
                    texto.setText(R.string.lista_vazia);
                }
                else {
                    TextView texto =(TextView) findViewById(R.id.erro);
                    texto.setText(R.string.sem_internet);
                }
            }
            else {
                mAdapter.addAll(noticias);
                TextView texto =(TextView) findViewById(R.id.erro);
                texto.setText("");
                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mAdapter.getItem(position).getLink()));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Noticia>> loader) {
        mAdapter.clear();
    }

    public boolean isOnline(){
        boolean online = false;
        ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            online=true;
        return online;
    }

    public void buscar(View v){
        EditText editText = (EditText)findViewById(R.id.edit_text);
        mPalavraChave = editText.getText().toString();
        if(isOnline()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NOTICIA_LOADER_ID, null, this);
            mAdapter = new NoticiaAdapter(this);
        }
        else {
            TextView texto =(TextView) findViewById(R.id.erro);
            texto.setText(R.string.sem_internet);
        }
    }

}
