package com.example.android.appdenoticias;

/**
 * Created by Andrik on 23/03/2018.
 */

public class Noticia {

    String mTitulo;
    String mNomeSecao;
    String mDataPublicacao;
    String mLink;

    public Noticia(String titulo, String nomeSecao, String dataPublicacao, String link) {
        this.mTitulo = titulo;
        this.mNomeSecao = nomeSecao;
        this.mDataPublicacao = dataPublicacao;
        this.mLink = link;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public String getNomeSecao() {
        return mNomeSecao;
    }

    public String getDataPublicacao() {
        return mDataPublicacao;
    }

    public String getLink() {
        return mLink;
    }
}
