package br.ufes.cefd.suportcefd.utils.adapter;

/**
 * Created by pgrippa on 14/09/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.db.PersonDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.Util;
import br.ufes.cefd.suportcefd.webservice.AccessServiceAPI;

public class ServiceAdapter extends RecyclerView.Adapter {
    SharedPreferences prefs;
    private AccessServiceAPI m_AccessServiceAPI;
    private final Context context;
    private final ArrayList<Service> services;
    private Service service;
    private Person person;

    public ServiceAdapter(Context context, ArrayList<Service> services, Person person) {
        this.context = context;
        this.services = services;
        this.person = person;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ServiceViewHolder holder = (ServiceViewHolder) viewHolder;


        service = services.get(position);
        holder.getResponsible().setText(person.getName());
        holder.getPatrimony().setText(service.getPatrimony());
        Util.setIconByType(context,holder.getIcon(), service.getType());
        Util.setStatusIcon(context,holder.getStatus(), service.getActive());

        //new TaskGetPerson(holder).execute();

    }

    @Override
    public int getItemCount() {

        return services!=null ? services.size() : 0;
    }

    public void swap(ArrayList<Service> list){
        services.clear();
        services.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<Service> getServices(){
        return services;
    }



    protected class ServiceViewHolder extends RecyclerView.ViewHolder{

        private TextView patrimony;
        private TextView responsible;
        private ImageView icon;
        private ImageView status;

        public ServiceViewHolder(View view) {
            super(view);

            setPatrimony((TextView) view.findViewById(R.id.t_tpatrimonio));
            setResponsible((TextView) view.findViewById(R.id.t_tresponsavel));
            setIcon((ImageView) view.findViewById(R.id.i_logo));
            setStatus((ImageView) view.findViewById(R.id.icon_ok));
        }

        public TextView getPatrimony() {
            return patrimony;
        }

        public void setPatrimony(TextView patrimony) {
            this.patrimony = patrimony;
        }

        public TextView getResponsible() {
            return responsible;
        }

        public void setResponsible(TextView responsible) {
            this.responsible = responsible;
        }

        public ImageView getIcon() {
            return icon;
        }

        public void setIcon(ImageView icon) {
            this.icon = icon;
        }

        public ImageView getStatus() {
            return status;
        }

        public void setStatus(ImageView status) {
            this.status = status;
        }
    }

    public class TaskGetPerson extends AsyncTask<ServiceViewHolder, Void, Integer> {

        ServiceViewHolder holder;
        public TaskGetPerson(ServiceViewHolder holder){
            this.holder = holder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(ServiceViewHolder... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getpersonbyid");
            postParam.put("id", service.getIdResp()+"");

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
                    person = new Person(jsonObject);
                }

                return Util.RESULT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Util.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            holder.getResponsible().setText(person.getName());
            holder.getPatrimony().setText(service.getPatrimony());
            Util.setIconByType(context,holder.getIcon(), service.getType());
            Util.setStatusIcon(context,holder.getStatus(), service.getActive());
        }
    }
}
