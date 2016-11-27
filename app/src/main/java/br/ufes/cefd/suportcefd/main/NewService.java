package br.ufes.cefd.suportcefd.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.Util;
import br.ufes.cefd.suportcefd.utils.adapter.SpinnerItemAdapter;
import br.ufes.cefd.suportcefd.webservice.AccessServiceAPI;
import br.ufes.cefd.suportcefd.webservice.Tasks;

public class NewService extends AppCompatActivity {
    private Spinner spinner;
    private Person person;
    private Person auxPerson = null;
    private AutoCompleteTextView responsible;
    private EditText telephone;
    private EditText email;
    private Tasks tasks;
    private ArrayList<Person> personList;
    private AlertDialog alertNewUser;
    private AccessServiceAPI m_AccessServiceAPI;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        init();
    }

    public void init(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

        person = (Person) this.getIntent().getExtras().getSerializable(getString(R.string.sp_person));

        responsible = (AutoCompleteTextView) findViewById(R.id.t_responsavel);
        telephone = (EditText) findViewById(R.id.t_telefone);
        email = (EditText) findViewById(R.id.t_email);

        if(person.getType().equals(getString(R.string.sp_user))){
            responsible.setText(person.getName());
            responsible.setVisibility(View.GONE);

            telephone.setText(person.getTelephone());
            telephone.setVisibility(View.GONE);

            email.setText(person.getEmail());
            email.setVisibility(View.GONE);
        }else{
            tasks = new Tasks(this);
            tasks.execGetAllPerson(responsible, telephone, email);

            responsible.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<Person> adapter = (ArrayAdapter<Person>) responsible.getAdapter();
                    auxPerson = adapter.getItem(position);
                    telephone.setText(auxPerson.getTelephone());
                    email.setText(auxPerson.getEmail());
                }
            });
        }

        alertNewUser = createAlertNewUser("Novo Usuário","Deseja adicionar o usuário "+responsible.getText().toString()+"?");
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

        Service s1;
        Person p1;

        if(person.getType().equals("admin")){

            if(auxPerson==null){
                alertNewUser.show();
                return;
            }
            s1 = new Service(p,l,t,d,auxPerson.getId());
            p1 = auxPerson;

        }else{
            s1 = new Service(p,l,t,d,person.getId());
            p1 = person;
        }

        tasks = new Tasks(this);
        tasks.execNewService(s1,p1);

        finish();
    }

    private AlertDialog createAlertNewUser(String title, String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);

        builder.setMessage(msg);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent it = new Intent(NewService.this, NewUser.class);

                Person p1 = new Person(responsible.getText().toString(), telephone.getText().toString(),
                        email.getText().toString(),"","");

                it.putExtra("person",p1);
                startActivityForResult(it, 10);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                auxPerson=person;
            }
        });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String nemail = email.getText().toString();
                if(b!=null){
                    nemail = data.getExtras().getString("email");
                }

                new TaskGetPerson().execute(nemail);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public class TaskGetPerson extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getperson");
            postParam.put("email", params[0]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (jsonArray != null) {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                    auxPerson = new Person(jsonObject);
                }

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }
    }

}

