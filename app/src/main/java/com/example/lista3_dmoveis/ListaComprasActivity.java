package com.example.lista3_dmoveis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lista3_dmoveis.database.Banco;
import com.example.lista3_dmoveis.database.ListaComprasDAO;
import com.example.lista3_dmoveis.model.ListaCompras;
import com.example.lista3_dmoveis.model.Setor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaComprasActivity extends AppCompatActivity {


    public static final String LISTACOMPRAS = "LISTACOMPRAS";
    Button criarNovaListaBotao;
    Banco bd;

    
    ListaComprasDAO lcDAO;
    List<ListaCompras> arrayCompras;
    ArrayAdapter<ListaCompras> adapter;
    ListaComprasObserver lcObserver;

    EditText prioridade;
    EditText nomeDaLista;

    ListView listaCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);
        lcObserver = new ListaComprasObserver();
        criarNovaListaBotao = findViewById(R.id.criarNovaListaBt);
        nomeDaLista = ((EditText) findViewById(R.id.nomeDaLista));
        prioridade = ((EditText) findViewById(R.id.prioridade));
        listaCompras = ((ListView) findViewById(R.id.listaCompras));
        bd = Room.databaseBuilder( getApplicationContext(),
                Banco.class, "lojinha").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        lcDAO = bd.listaComprasDAO();
        arrayCompras = new ArrayList<>(100);
        adapter = new ArrayAdapter<ListaCompras>(this,
                android.R.layout.simple_list_item_single_choice,
                arrayCompras);
        listaCompras.setAdapter(adapter);
        listaCompras.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lcDAO.getAll().observe(this, lcObserver);

        listaCompras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Intent it = new Intent(getApplicationContext(), ListaItensActivity.class);
                it.putExtra(LISTACOMPRAS, (Serializable) arrayCompras.get(pos));
                startActivity(it);
                return true;
            }
        });

    }

    public void criarNovaLista(View v) {
        String nomeLista = nomeDaLista.getText().toString();
        int pri = Integer.parseInt(prioridade.getText().toString());
        lcDAO.inserir(new ListaCompras(nomeLista, pri, 0));
    }

    class ListaComprasObserver implements Observer<List<ListaCompras>> {
        @Override
        public void onChanged( List<ListaCompras> lcompras) {
            arrayCompras.clear();
            arrayCompras.addAll( lcompras );
            adapter.notifyDataSetChanged();
        }
    }


}