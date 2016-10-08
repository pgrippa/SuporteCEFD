package br.ufes.cefd.suportcefd.model;

/**
 * Created by pgrippa on 14/09/16.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.utils.Util;

public class ServiceAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final ArrayList<Service> services;

    public ServiceAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.services = services;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ServiceViewHolder holder = (ServiceViewHolder) viewHolder;


        Service s = services.get(position);

        holder.getResponsible().setText(s.getResponsible());
        holder.getPatrimony().setText(s.getPatrimony());
        Util.setIconByType(context,holder.getIcon() , s.getType());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    protected class ServiceViewHolder extends RecyclerView.ViewHolder{

        private TextView patrimony;
        private TextView responsible;
        private ImageView icon;

        public ServiceViewHolder(View view) {
            super(view);

            setPatrimony((TextView) view.findViewById(R.id.t_tpatrimonio));
            setResponsible((TextView) view.findViewById(R.id.t_tresponsavel));
            setIcon((ImageView) view.findViewById(R.id.i_logo));
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
    }
}
