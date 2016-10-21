package br.ufes.cefd.suportcefd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.SpinnerItemAdapter;
import br.ufes.cefd.suportcefd.utils.SendMailTask;
import br.ufes.cefd.suportcefd.utils.Util;

public class NewService extends AppCompatActivity {
    private Spinner spinner;
    private Person person;
    private AutoCompleteTextView responsible;
    private EditText telephone;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(R.string.t_cadastrar_servico);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] array_spinner=new String[6];
        array_spinner[0]=getString(R.string.t_desktop);
        array_spinner[1]=getString(R.string.t_notebook);
        array_spinner[2]=getString(R.string.t_monitor);
        array_spinner[3]=getString(R.string.t_impressora);
        array_spinner[4]=getString(R.string.t_network);
        array_spinner[5]=getString(R.string.t_outro);
        spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setAdapter(new SpinnerItemAdapter(this, array_spinner));

        person = (Person) this.getIntent().getExtras().getSerializable("person");

        responsible = (AutoCompleteTextView) findViewById(R.id.t_responsavel);
        telephone = (EditText) findViewById(R.id.t_telefone);
        email = (EditText) findViewById(R.id.t_email);

        if(person.getType().equals("user")){
            responsible.setText(person.getName());
            responsible.setVisibility(View.GONE);

            telephone.setText(person.getTelephone());
            telephone.setVisibility(View.GONE);

            email.setText(person.getEmail());
            email.setVisibility(View.GONE);
        }
    }

    public void saveService(View v){
        EditText pat = (EditText) findViewById(R.id.t_patrimonio);
        String p = pat.getText().toString();

        if(p.isEmpty()){
            pat.setError(getString(R.string.error_field_required));
            return;
        }

        String t = spinner.getSelectedItem().toString();

        EditText local = (EditText) findViewById(R.id.t_local);
        String l = local.getText().toString();

        if(l.isEmpty()){
            local.setError(getString(R.string.error_field_required));
            return;
        }

        String r = responsible.getText().toString();

        if(r.isEmpty()){
            responsible.setError(getString(R.string.error_field_required));
            return;
        }

        EditText desc = (EditText) findViewById(R.id.t_description);
        String d = desc.getText().toString();

        if(d.isEmpty()){
            desc.setError(getString(R.string.error_field_required));
            return;
        }

        telephone = (EditText) findViewById(R.id.t_telefone);
        String tl = telephone.getText().toString();

        if(d.isEmpty()){
            desc.setError(getString(R.string.error_field_required));
            return;
        }

        email = (EditText) findViewById(R.id.t_email);
        String em = email.getText().toString();

        if(!em.contains("@")){
            email.setError(getString(R.string.error_invalid_email));
            email.requestFocus();
            return;
        }

        Service e = new Service(p,l,t,d,person.getId());

        ServiceDAO dao = new ServiceDAO(getApplicationContext());

        long id = dao.putService(e);

        e.setId(id);

        Toast.makeText(getBaseContext(), "Serviço cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

        ArrayList<String> list = new ArrayList<>();

        list.add(em);

        String msg = Util.getMessage(e,person);



        /*new SendMailTask(NewService.this).execute(Util.FROMEMAIL,
                Util.FROMPASSWORD, list, "[CEFD #"+String.format("%07d", id)+"] Novo Chamado SUPORTE CEFD", msg);*/

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
