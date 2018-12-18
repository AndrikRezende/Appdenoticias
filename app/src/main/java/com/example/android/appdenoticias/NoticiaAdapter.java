package com.example.android.appdenoticias;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Collection;

/**
 * Created by Andrik on 24/03/2018.
 */

public class NoticiaAdapter extends ArrayAdapter<Noticia> {


    public NoticiaAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public void addAll(Collection<? extends Noticia> collection) {
        super.addAll(collection);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        Noticia noticiaCorrente = getItem(position);
        TextView titulo = (TextView) listItemView.findViewById(R.id.titulo);
        titulo.setText(noticiaCorrente.getTitulo());

        TextView secao = (TextView) listItemView.findViewById(R.id.secao);
        secao.setText(noticiaCorrente.getNomeSecao());

        TextView data = (TextView) listItemView.findViewById(R.id.data);
        data.setText(noticiaCorrente.getDataPublicacao().split("T")[0]);

        return listItemView;
    }
}
