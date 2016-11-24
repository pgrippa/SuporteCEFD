package br.ufes.cefd.suportcefd.webservice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import br.ufes.cefd.suportcefd.utils.adapter.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.email.SendMailTask;

/**
 * Created by pgrippa on 22/11/16.
 */

public class Tasks extends Activity{

    private AccessServiceAPI m_AccessServiceAPI;
    private Service s;
    private Person p;
    private RecyclerView recyclerView;
    ArrayList<Service> serviceList;
    private Context context;
    private View v;

    public Tasks(Context context, View v, Person p){
        this.context = context;
        this.v = v;
        this.p = p;
    }

    public Tasks(Context context){
        this.context = context;
    }

    public void execNewService(Service s, Person p){
        this.s = s;
        this.p = p;
        new TaskNewService().execute(s.getPatrimony(),s.getType(), s.getLocal(), s.getDescription(),
                s.getEntryDate(),s.getReleaseDate(),s.getActive()+"",s.getIdResp()+"");
    }

    public void execNewUser(Person p){
        this.p = p;
        new TaskNewUser().execute(p.getName(),p.getTelephone(),p.getEmail(),p.getPassword(),p.getType());
    }

    public void execGetActiveServices(RecyclerView recyclerView, Person person, boolean active){
        this.recyclerView = recyclerView;
        this.p = person;
        new TaskGetActiveServices().execute(person.getId()+"", active ? "1":"0");
    }

    public class TaskNewService extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "addservice");
            postParam.put("patrimony", params[0]);
            postParam.put("type", params[1]);
            postParam.put("local", params[2]);
            postParam.put("description", params[3]);
            postParam.put("entdate", params[4]);
            postParam.put("reldate", params[5]);
            postParam.put("active", params[6]);
            postParam.put("idperson", params[7]);

            try{
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(Util.SERVICE_API_URL, postParam);

                if(jsonString.contains("0")){
                    return Util.RESULT_SUCCESS;
                }

                return Util.RESULT_ERROR;
            }catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer==0){
                Toast.makeText(context, context.getString(R.string.ns_success), Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sendmail = prefs.getBoolean("sendemail",false);
                if(sendmail) {

                    ArrayList<String> list = new ArrayList<>();

                    list.add(p.getEmail());

                    String msg = Util.getMessage(s, p);

                    new SendMailTask(Tasks.this).execute(Util.FROMEMAIL,
                            Util.FROMPASSWORD, list, context.getString(R.string.ns_suject,s.getId()), msg);
                }

            }else{
                Toast.makeText(context, "Ocorreu um erro ao salvar o serviço!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class TaskNewUser extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "addperson");
            postParam.put("name", params[0]);
            postParam.put("telephone", params[1]);
            postParam.put("email", params[2]);
            postParam.put("password", params[3]);
            postParam.put("type", params[4]);

            try{
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(Util.SERVICE_API_URL, postParam);

                if(jsonString.contains("0")){
                    return Util.RESULT_SUCCESS;
                }

                return Util.RESULT_ERROR;
            }catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer==0){
                Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(context, "Ocorreu ao realizar cadastro!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class TaskGetActiveServices extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getpersonservices");
            postParam.put("personid", params[0]);
            postParam.put("active", params[1]);

            try{
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(Util.SERVICE_API_URL, postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String patrimony;
                String type;
                String local;
                String description;
                String entdate;
                String reldate;
                int active;
                long idperson;
                int id;
                serviceList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        id = Integer.parseInt(jsonObject.getString("_ID"));
                        patrimony = String.valueOf(jsonObject.getString("patrimony"));
                        type= String.valueOf(jsonObject.getString("type"));
                        local= String.valueOf(jsonObject.getString("local"));
                        description = String.valueOf(jsonObject.getString("description"));
                        entdate = String.valueOf(jsonObject.getString("entdate"));
                        reldate = String.valueOf(jsonObject.getString("reldate"));
                        active = Integer.parseInt(jsonObject.getString("active"));
                        idperson = Long.parseLong(jsonObject.getString("idperson"));

                        s = new Service(patrimony, local, type, description, idperson);
                        s.setEntryDate(entdate);
                        s.setReleaseDate(reldate);
                        s.setActive(active);
                        s.setId(id);


                        serviceList.add(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("TAMANHO: "+serviceList.size());

                return Util.RESULT_SUCCESS;
            }catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer==0){

                if (serviceList == null) {
                    serviceList = new ArrayList<>();
                }

                recyclerView.setAdapter(new ServiceAdapter(context, serviceList, p));

                TextView empty = (TextView) v;
                if (serviceList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
            }
        }
    }



}