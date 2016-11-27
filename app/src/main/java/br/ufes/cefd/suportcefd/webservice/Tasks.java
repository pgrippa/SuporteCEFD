package br.ufes.cefd.suportcefd.webservice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.db.PersonDAO;
import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.Util;
import br.ufes.cefd.suportcefd.utils.adapter.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.email.SendMailTask;

/**
 * Created by pgrippa on 22/11/16.
 */

public class Tasks extends Activity {

    private AccessServiceAPI m_AccessServiceAPI;
    private Service s;
    private Person p;
    private RecyclerView recyclerView;
    ArrayList<Service> serviceList;
    ArrayList<Person> personList;
    private Context context;
    private View v;
    private View mProgressView;
    private SharedPreferences prefs;
    private View[] views;

    public Tasks(Context context, View v, Person p) {
        this.context = context;
        this.v = v;
        this.p = p;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Tasks(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void execNewService(Service s, Person p) {
        this.s = s;
        this.p = p;
        new TaskNewService().execute(s.getPatrimony(), s.getType(), s.getLocal(), s.getDescription(),
                s.getEntryDate(), s.getReleaseDate(), s.getActive() + "", s.getIdResp() + "");
    }

    public void execNewUser(Person p) {
        this.p = p;
        new TaskNewUser().execute(p.getName(), p.getTelephone(), p.getEmail(), p.getPassword(), p.getType());
    }

    public void execGetPersonServices(RecyclerView recyclerView, Person person, boolean active) {
        this.recyclerView = recyclerView;
        this.p = person;
        new TaskGetPersonServices().execute(person.getId() + "", active ? "1" : "0");
    }

    public void execGetPersonAllServices(RecyclerView recyclerView, Person person) {
        this.recyclerView = recyclerView;
        this.p = person;
        new TaskGetPersonServices().execute(person.getId() + "");
    }

    public void execGetActiveServices(RecyclerView recyclerView, boolean active) {
        this.recyclerView = recyclerView;
        new TaskGetServices().execute(active ? "1" : "0");
    }

    public void execSearchServices(RecyclerView recyclerView, String query) {
        this.recyclerView = recyclerView;
        new TaskSearchServices().execute(query);
    }

    public void execGetServices(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        new TaskGetServices().execute();
    }

    public void execGetServicesAdmin(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        new TaskGetServicesAdmin().execute();
    }

    public void execUpdateService(Service s, Person p) {
        this.s = s;
        this.p = p;
        new TaskUpdateService().execute(s);
    }

    public void execGetAllPerson(View... params) {
        this.views = params;
        new TaskGetAllPerson().execute();
    }


    /*
        ################# TASKS #################
     */

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

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                return Integer.parseInt(jsonString.split(";")[0]);

            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer != -1) {
                Toast.makeText(context, context.getString(R.string.ns_success), Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sendmail = prefs.getBoolean("sendemail", false);
                s.setId(integer);

                ServiceDAO dao = new ServiceDAO(context);
                dao.putService(s);

                if (sendmail) {

                    ArrayList<String> list = new ArrayList<>();

                    list.add(p.getEmail());

                    String msg = Util.getMessage(s, p);

                    new SendMailTask(Tasks.this).execute(Util.FROMEMAIL,
                            Util.FROMPASSWORD, list, context.getString(R.string.ns_suject, s.getId()), msg);
                }

            } else {
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

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                return Integer.parseInt(jsonString.split(";")[0]);

            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer !=-1) {
                PersonDAO dao = new PersonDAO(context);
                dao.putPerson(p);

                Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Ocorreu ao realizar cadastro!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class TaskGetServices extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            if (params.length == 1) {

                postParam.put("action", "getactiveservices");
                postParam.put("active", params[0]);
            } else {
                postParam.put("action", "getservices");
            }

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Util.RESULT_ERROR;
                }

                packServices(jsonArray);

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            showServices(integer);
        }
    }

    public class TaskGetServicesAdmin extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();

            postParam.put("action", "getadminservices");


            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                String[] db = jsonString.split(";person;");

                JSONArray serviceJsonArray = null;
                JSONArray personJsonArray = null;
                try {
                    serviceJsonArray = new JSONArray(db[0]);
                    personJsonArray = new JSONArray(db[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Util.RESULT_ERROR;
                }

                packServicesDAO(serviceJsonArray);
                packPeopleDAO(personJsonArray);

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            showServicesAdmin(integer);
        }
    }

    public class TaskGetPersonServices extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();

            if (params.length == 2) {
                postParam.put("action", "getpersonservices");
                postParam.put("personid", params[0]);
                postParam.put("active", params[1]);
            } else {
                postParam.put("action", "getpersonallservices");
                postParam.put("personid", params[0]);
            }

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Util.RESULT_ERROR;
                }

                packServices(jsonArray);

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            showServices(integer);
        }
    }

    public class TaskSearchServices extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();

            postParam.put("action", "searchservice");
            postParam.put("search", params[0]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Util.RESULT_ERROR;
                }

                packServices(jsonArray);

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            showServices(integer);
        }
    }

    public class TaskUpdateService extends AsyncTask<Service, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(Service... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "updateservice");
            postParam.put("id", params[0].getId() + "");
            postParam.put("patrimony", params[0].getPatrimony());
            postParam.put("type", params[0].getType());
            postParam.put("local", params[0].getLocal());
            postParam.put("description", params[0].getDescription());
            postParam.put("entdate", params[0].getEntryDate());
            postParam.put("reldate", params[0].getReleaseDate());
            postParam.put("active", params[0].getActive() + "");
            postParam.put("idperson", params[0].getIdResp() + "");


            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                if (jsonString.contains("0")) {
                    return Util.RESULT_SUCCESS;
                }

                return Util.RESULT_ERROR;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                Toast.makeText(context, "Serviço atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sendmail = prefs.getBoolean("sendemail",false);
                if(sendmail) {

                    ArrayList<String> list = new ArrayList<>();

                    list.add(p.getEmail());

                    String msg = Util.getMessage(s, p);

                    new SendMailTask(Tasks.this).execute(Util.FROMEMAIL,
                            Util.FROMPASSWORD, list, context.getString(R.string.ns_suject,s.getId()), msg);
                }*/

            } else {
                Toast.makeText(context, "Ocorreu um erro ao atualizar o serviço!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class TaskGetAllPerson extends AsyncTask<View, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(View... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getallperson");

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(prefs.getString("webservice", ""), postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Util.RESULT_ERROR;
                }

                packPeople(jsonArray);

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            AutoCompleteTextView responsible = (AutoCompleteTextView) views[0];

            responsible.setThreshold(1);
            responsible.setAdapter(new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line, personList));
        }
    }

    private void packServices(JSONArray jsonArray) {
        serviceList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                s = new Service(jsonObject);
                serviceList.add(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void packPeople(JSONArray jsonArray) {

        personList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));

                p = new Person(jsonObject);
                personList.add(p);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void packServicesDAO(JSONArray jsonArray) {
        serviceList = new ArrayList<>();
        ServiceDAO dao = new ServiceDAO(context);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                s = new Service(jsonObject);

                if(s.getActive()==1){
                    serviceList.add(s);
                }

                dao.putService(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void packPeopleDAO(JSONArray jsonArray) {

        personList = new ArrayList<>();
        PersonDAO dao = new PersonDAO(context);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));

                p = new Person(jsonObject);
                personList.add(p);
                dao.putPerson(p);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showServices(Integer integer) {
        showProgress(false);

        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }

        TextView empty = (TextView) v;
        if (serviceList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(new ServiceAdapter(context, serviceList, p));
    }

    private void showServicesAdmin(Integer integer) {
        showProgress(false);

        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }

        TextView empty = (TextView) v;
        if (serviceList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(new ServiceAdapter(context, serviceList, personList));
    }

    public void setProgressBar(View mProgress) {
        this.mProgressView = mProgress;
    }

    public void showProgress(final boolean show) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

}
