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
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.Util;
import br.ufes.cefd.suportcefd.webservice.AccessServiceAPI;

public class ServiceAdapter extends RecyclerView.Adapter {
    SharedPreferences prefs;
    private AccessServiceAPI m_AccessServiceAPI;
    private final Context context;
    private final ArrayList<Service> services;
    private ArrayList<Person> personList;
    private Service service;
    private Person person;
    private ImageView status;
    private ImageView icon;

    public ServiceAdapter(Context context, ArrayList<Service> services, Person person) {
        this.context = context;
        this.services = services;
        this.person = person;
        this.personList = null;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ServiceAdapter(Context context, ArrayList<Service> services, ArrayList<Person> personList) {
        this.context = context;
        this.services = services;
        this.personList = personList;
        this.person = null;
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

        if(personList != null){

            person = getPerson(service.getIdResp());
        }

        holder.getResponsible().setText(person.getName());
        holder.getPatrimony().setText(service.getPatrimony());
        setIconByType(context, holder.getIcon(), service.getType());
        setStatusIcon(holder.getStatus(), service.getActive());

    }

    private Person getPerson(long idperson){
        Person p;
        for(int i=0; i< personList.size(); i++){
            p = personList.get(i);
            if(p.getId()==idperson){
                return p;
            }
        }

        return null;
    }

    public void setPersonList(ArrayList<Person> personList){
        this.personList = personList;
    }

    @Override
    public int getItemCount() {

        return services !=null ? services.size() : 0;
    }

    public void swap(ArrayList<Service> list){
        services.clear();
        services.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<Service> getServices(){
        return services;
    }

    public ArrayList<Person> getPersonList(){
        return personList;
    }

    public void setIconByType(Context context, ImageView imageView, String tipo){
        if (tipo.equals(context.getString(R.string.t_desktop))) {
            imageView.setImageResource(R.drawable.desktop);
        } else if (tipo.equals(context.getString(R.string.t_notebook))) {
            imageView.setImageResource(R.drawable.notebook);
        } else if (tipo.equals(context.getString(R.string.t_impressora))) {
            imageView.setImageResource(R.drawable.printer);
        } else if (tipo.equals(context.getString(R.string.t_monitor))) {
            imageView.setImageResource(R.drawable.monitor);
        }else if (tipo.equals(context.getString(R.string.t_network))) {
            imageView.setImageResource(R.drawable.network2);
        }else{
            imageView.setImageResource(R.drawable.maintenance);
        }
    }

    public void setStatusIcon(ImageView imageView, int status){
        switch (status){

            case 0:
                imageView.setImageResource(R.drawable.ic_status_ok_24dp);
                break;

            case 1:
                imageView.setImageResource(R.drawable.ic_status_pr_24dp);
                break;

            default:
                imageView.setImageResource(R.drawable.ic_status_pr_24dp);
                break;
        }
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
}
