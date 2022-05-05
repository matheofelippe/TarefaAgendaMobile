package com.example.tarefaagendamobile.activities;


import static com.example.tarefaagendamobile.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.tarefaagendamobile.R;
import com.example.tarefaagendamobile.dao.PersonagemDAO;
import com.example.tarefaagendamobile.model.Personagem;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class FormularioPersonagemActivity extends AppCompatActivity {

    //Cria variaveis com titulos, campos e variavel pra guardar o personagem
    private static final String TITULO_APPBAR_EDITA_PERSONAGEM = "Editar o Personagem";
    private static final String TITULO_APPBAR_NOVO_PERSONAGEM = "Novo Personagem";
    private EditText campoNome;
    private EditText campoNascimento;
    private EditText campoAltura;
    //Variavel da classe personagem DAO
    private final PersonagemDAO dao = new PersonagemDAO();
    private Personagem personagem;

    //Cria o metodo onCreateOptionsMenu para criar o menu com as opções de seleção
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_formulario_personagem_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Cria o metodo onOptionsItemSelected para selecionar o item no menu previamente criado e finaliza ele
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_personagem_menu_salvar){
            finalizarFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void finalizarFormulario() {
        preencherPersonagem();
        if(personagem.idValido()){
            dao.edita(personagem);
            finish();
        }else{
            dao.salva(personagem);
        }
    }

    private void preencherPersonagem() {
        String nome = campoNome.getText().toString();
        String altura = campoNome.getText().toString();
        String nascimento = campoNome.getText().toString();
        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
    }

    //Metodo que
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_personagem_activity);
        inicializacaoCampos();
        //configuraBotaoSalvar();
        carregaPersonagem();
        //checaPermissoes();
    }

    //Cria o metodo carregaPersonagem pra "puxar" as informações do personagem
    private void carregaPersonagem() {
        Intent dados = getIntent();
        if(dados.hasExtra(CHAVE_PERSONAGEM)){
            getTitle(TITULO_APPBAR_EDITA_PERSONAGEM);
            personagem = (Personagem) dados.getSerializableExtra(CHAVE_PERSONAGEM);
            preencheCampos();
        }else{
            setTitle(TITULO_APPBAR_NOVO_PERSONAGEM);
            personagem = new Personagem();
        }
    }

    private void getTitle(String tituloAppbarEditaPersonagem) {
    }

    //preencehr os campos com os dados recebidos
    private void preencheCampos() {
        campoNome.setText(personagem.getNome());
        campoAltura.setText(personagem.getAltura());
        campoNascimento.setText(personagem.getNascimento());
    }

    //Salva os dados digitados e salva nessas variaveis
    private void inicializacaoCampos(){
        campoNome = findViewById(R.id.editText_nome);
        campoAltura = findViewById(R.id.editText_altura);
        campoNascimento = findViewById(R.id.editText_nascimento);

        SimpleMaskFormatter smfAltura = new SimpleMaskFormatter("N,NN");
        MaskTextWatcher ntwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(ntwAltura);

        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher ntwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(ntwNascimento);
    }

    private void preenchePersonagem(){
        String nome = campoNome.getText().toString();
        String altura = campoAltura.getText().toString();
        String nascimento = campoNascimento.getText().toString();

        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
    }

}
