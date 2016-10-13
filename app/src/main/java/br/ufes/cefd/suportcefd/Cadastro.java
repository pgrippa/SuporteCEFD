package br.ufes.cefd.suportcefd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.SpinnerItemAdapter;
import br.ufes.cefd.suportcefd.utils.SendMailTask;
import br.ufes.cefd.suportcefd.utils.Util;

public class Cadastro extends AppCompatActivity {
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(R.string.t_cadastrar_servico);
        setSupportActionBar(toolbar);

        String[] array_spinner=new String[6];
        array_spinner[0]=getString(R.string.t_desktop);
        array_spinner[1]=getString(R.string.t_notebook);
        array_spinner[2]=getString(R.string.t_monitor);
        array_spinner[3]=getString(R.string.t_impressora);
        array_spinner[4]=getString(R.string.t_network);
        array_spinner[5]=getString(R.string.t_outro);
        spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setAdapter(new SpinnerItemAdapter(this, array_spinner));

        Button cadastrar = (Button) findViewById(R.id.b_save);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveService();
            }
        });
    }

    private void saveService(){
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

        EditText resp = (EditText) findViewById(R.id.t_responsavel);
        String r = resp.getText().toString();

        if(r.isEmpty()){
            resp.setError(getString(R.string.error_field_required));
            return;
        }

        EditText desc = (EditText) findViewById(R.id.t_description);
        String d = desc.getText().toString();

        if(d.isEmpty()){
            desc.setError(getString(R.string.error_field_required));
            return;
        }

        EditText tel = (EditText) findViewById(R.id.t_telefone);
        String tl = tel.getText().toString();

        if(d.isEmpty()){
            desc.setError(getString(R.string.error_field_required));
            return;
        }

        EditText email = (EditText) findViewById(R.id.t_email);
        String em = email.getText().toString();

        if(!em.contains("@")){
            email.setError(getString(R.string.error_invalid_email));
            email.requestFocus();
            return;
        }

        Service e = new Service(p,l,t,r,d,em,tl);

        ServiceDAO dao = new ServiceDAO(getApplicationContext());
        dao.open("write");

        long id = dao.putService(e);

        dao.close();

        e.setId(id);

        Toast.makeText(getBaseContext(), "Serviço cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

        ArrayList<String> list = new ArrayList<>();

        list.add(em);

        String msg = Util.getMessage(e);

        new SendMailTask(Cadastro.this).execute(Util.FROMEMAIL,
                Util.FROMPASSWORD, list, "Testando App Android", msg);

        finish();
    }

}
