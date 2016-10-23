package br.ufes.cefd.suportcefd.utils.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.Util;

/**
 * Created by pgrippa on 17/09/16.
 */
public class FullService extends AppCompatActivity {

    private EditText l;
    private EditText r;
    private EditText d;
    private EditText e;
    private EditText t;
    private TextView dtx;
    private TextView dts;
    private Button edit;
    private boolean editing = false;
    private Person person;
    private Service service;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullservice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarfull);

        person = (Person) this.getIntent().getExtras().getSerializable("person");
        service = (Service) this.getIntent().getExtras().getSerializable("service");

        long id = service.getId();
        System.out.println("SERVICE ACTIVE FULL: "+service.getActive());

        if(toolbar != null){
            toolbar.setTitle("Chamado nº "+String.format("%07d", id));
            setSupportActionBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String patrimonio = service.getPatrimony();
        String local = service.getLocal();
        String responsavel = person.getName();
        String descricao = service.getDescription();
        String tipo = service.getType();
        String data = service.getEntryDate();
        String email = person.getEmail();
        String telefone = person.getTelephone();

        ImageView imageView = (ImageView) findViewById(R.id.i_ftipo);

        TextView p = (TextView) findViewById(R.id.l_fpatrimonio);
        p.setText(patrimonio);

        l = (EditText) findViewById(R.id.txlocal);
        l.setText(local);

        r = (EditText) findViewById(R.id.txresponsavel);
        r.setText(responsavel);

        d = (EditText) findViewById(R.id.txdescricao);
        d.setText(descricao);

        e = (EditText) findViewById(R.id.txemail1);
        e.setText(email);

        t = (EditText) findViewById(R.id.txtel);
        t.setText(telefone);

        TextView dt = (TextView) findViewById(R.id.t_data);
        dt.setText(data);

        dtx = (TextView) findViewById(R.id.t_data2);
        dts = (TextView) findViewById(R.id.t_datas);

        ImageView status = (ImageView) findViewById(R.id.icon_st);

        Util.setIconByType(this,imageView,tipo);
        Util.setStatusIcon(this,status,service.getActive());

        edit = (Button) findViewById(R.id.editbutton);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFieldsEnable(!editing);
            }
        });

        if(service.getActive() == Service.INACTIVE){
            Button close = (Button) findViewById(R.id.closebutton);
            close.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            dts.setText(service.getReleaseDate());
        }else {
            dtx.setVisibility(View.GONE);
            dts.setVisibility(View.GONE);
        }
    }

    public void closeService(View v){
        Intent intent = new Intent();

        intent.putExtra("id",service.getId());
        setResult(RESULT_OK,intent);

        finish();
    }

    private void setFieldsEnable(boolean value){
        l.setEnabled(value);
        r.setEnabled(value);
        d.setEnabled(value);
        e.setEnabled(value);
        t.setEnabled(value);
        if(value){
            edit.setText("Salvar");
        }else{
            edit.setText("Editar");
        }

        editing = value;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(service.getActive()==1){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_full_service, menu);
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_edit:
                setFieldsEnable(!editing);
                break;

            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return true;
    }


}
