package com.example.tarefaagendamobile.activities;

import static com.example.tarefaagendamobile.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tarefaagendamobile.R;
import com.example.tarefaagendamobile.dao.PersonagemDAO;
import com.example.tarefaagendamobile.model.Personagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaPersonagemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Lista de Personagens";
    private final PersonagemDAO dao = new PersonagemDAO();
    private ArrayAdapter<Personagem> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_personagem_activity);
        setTitle(TITULO_APPBAR);
        configuraFabNovoPersonagem();
        configuraLista();
    }

    private void configuraFabNovoPersonagem() {
        FloatingActionButton botaoNovoPersonagem = findViewById(R.id.fab_abb);
        botaoNovoPersonagem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                abreFormulario();
            }

        });
    }

    private void abreFormulario() {
        startActivity(new Intent(this, FormularioPersonagemActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaPersonagem();
    }

    //metodo pra atualizar o personagem
    private void atualizaPersonagem() {
        adapter.clear();
        adapter.addAll(dao.todos());
    }

    //metodo pra remover personagem
    private void remove(Personagem personagem){
        dao.remove(personagem);
        adapter.remove(personagem);
    }

    //expandindo menu dos personagens
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.add("Remover");
        getMenuInflater().inflate(R.menu.activity_lista_personagem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.activity_lista_personagem_menu_remover){
            new AlertDialog.Builder(this)
                    .setTitle("Removendo Personagem")
                    .setMessage("Tem certeza que quer remover?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                            Personagem personagemEscolhido = adapter.getItem(menuInfo.position);
                            remove(personagemEscolhido);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    private void configuraLista() {
        ListView listaDePersonagem = findViewById(R.id.lista_pensonagem_activity);
        configuraAdaptor(listaDePersonagem);
        configuraItemPorClique(listaDePersonagem);
        registerForContextMenu(listaDePersonagem);
    }



    private void configuraItemPorClique(ListView listaDePersonagem) {
        listaDePersonagem.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id){
                Personagem personagemEscolhido = (Personagem) adapterView.getItemAtPosition(posicao);
                abreFormularioEditar(personagemEscolhido);
            }
        });
    }

    //Edita formulario
    private void abreFormularioEditar(Personagem personagemEscolhido) {
        Intent vaiParaFormulario = new Intent(ListaPersonagemActivity.this, FormularioPersonagemActivity.class);
        vaiParaFormulario.putExtra(CHAVE_PERSONAGEM, personagemEscolhido);
        startActivity(vaiParaFormulario);
    }

    private void configuraAdaptor(ListView listaDePersonagem) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaDePersonagem.setAdapter(adapter);
    }
}
