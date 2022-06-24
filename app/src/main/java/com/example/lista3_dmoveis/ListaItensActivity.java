package com.example.lista3_dmoveis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lista3_dmoveis.database.Banco;
import com.example.lista3_dmoveis.database.ItemsDAO;
import com.example.lista3_dmoveis.database.ListaComprasDAO;
import com.example.lista3_dmoveis.database.ProdutoDAO;
import com.example.lista3_dmoveis.database.SetorDAO;
import com.example.lista3_dmoveis.model.Item;
import com.example.lista3_dmoveis.model.ListaCompras;
import com.example.lista3_dmoveis.model.Produto;
import com.example.lista3_dmoveis.model.Setor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaItensActivity extends AppCompatActivity {
    
    Spinner sp;
    Banco bd;

    SetorDAO sdao;
    List<Setor> setores;
    ArrayAdapter<Setor> adapterSetores;
    SetoresObserver observerSetores;

    ProdutoDAO pdao;
    List<Produto> listaProdutos;
    ArrayAdapter<Produto> adapterProdutos;
    ListView listaProdutosDoSetor;
    ProdutoObserver produtoObserver;

    ListaCompras lc;


    ItemsDAO idao;
    ItemObserver itemObserver;
    ArrayAdapter<Item> adapterItems;
    List<Item> listaItens;
    ListView listaDeItens;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_itens);

        // -- SPINNER
        setores = new ArrayList<>(100);
        adapterSetores = new ArrayAdapter(this, android.R.layout.simple_spinner_item, setores);
        adapterSetores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        observerSetores = new SetoresObserver();
        itemObserver = new ItemObserver();
        bd = Room.databaseBuilder( getApplicationContext(),
                Banco.class, "lojinha").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        sdao = bd.setorDao();
        sdao.getAll().observe(this, observerSetores);
        sp = ((Spinner) findViewById(R.id.spinnerSetores));
        sp.setAdapter(adapterSetores);

        listaProdutosDoSetor = ((ListView) findViewById(R.id.listaProdutosDoSetor));
        listaDeItens = ((ListView) findViewById(R.id.listaDeItens));



        listaProdutos = new ArrayList<>(100);
        listaItens = new ArrayList<>(100);

        adapterProdutos = new ArrayAdapter<Produto>(this,
                android.R.layout.simple_list_item_1,
                listaProdutos);
        produtoObserver = new ProdutoObserver();
        pdao = bd.produtoDao();
        listaProdutosDoSetor.setAdapter(adapterProdutos);


        Bundle b = getIntent().getExtras();
        if (b != null){
            lc = (ListaCompras) b.getSerializable(ListaComprasActivity.LISTACOMPRAS);
        }

        adapterItems = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1,
                listaItens);
        idao = bd.itemsDAO();
        listaDeItens.setAdapter(adapterItems);
        idao.buscarPorListaDeCompras(lc.getId()).observe(this, itemObserver);

        listaProdutosDoSetor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                idao.inserir(new Item(1, false, lc.getId(), (int) listaProdutos.get(pos).getId()));
                return true;
            }
        });

        listaDeItens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                listaItens.get(pos).setComprado(true);
                idao.alterar(listaItens.get(pos));
                return true;
            }
        });

    }

    public void filtrarProdutos(View v){
        Setor setorSelecionado = (Setor) sp.getSelectedItem();
        pdao.buscarPorSetor(setorSelecionado.getId()).observe(this, produtoObserver);
    }

    class SetoresObserver implements Observer<List<Setor>> {
        @Override
        public void onChanged( List<Setor> setors) {
            setores.clear();
            setores.addAll( setors );
            adapterSetores.notifyDataSetChanged();
        }
    }

    class ProdutoObserver implements Observer<List<Produto>> {
        @Override
        public void onChanged( List<Produto> prods) {
            listaProdutos.clear();
            listaProdutos.addAll( prods );
            adapterProdutos.notifyDataSetChanged();
        }
    }

    class ItemObserver implements Observer<List<Item>> {
        @Override
        public void onChanged( List<Item> items) {
            listaItens.clear();
            listaItens.addAll( items );
            adapterProdutos.notifyDataSetChanged();
        }
    }
}