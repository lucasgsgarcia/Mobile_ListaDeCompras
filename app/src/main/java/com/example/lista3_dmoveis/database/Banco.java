package com.example.lista3_dmoveis.database;

import android.annotation.SuppressLint;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.lista3_dmoveis.model.Item;
import com.example.lista3_dmoveis.model.ListaCompras;
import com.example.lista3_dmoveis.model.Produto;
import com.example.lista3_dmoveis.model.Setor;

import com.example.lista3_dmoveis.database.SetorDAO;
import com.example.lista3_dmoveis.database.ProdutoDAO;

@SuppressLint("RestrictedApi")
@Database(entities = {Setor.class, Produto.class, ListaCompras.class, Item.class},
        version = 3,
        exportSchema = true
)
public abstract class Banco extends RoomDatabase {
    public abstract SetorDAO setorDao();
    public abstract ProdutoDAO produtoDao();
    public abstract ListaComprasDAO listaComprasDAO();
    public abstract ItemsDAO itemsDAO();


}
