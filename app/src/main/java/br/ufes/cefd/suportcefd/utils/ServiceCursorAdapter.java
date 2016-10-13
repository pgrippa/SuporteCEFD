package br.ufes.cefd.suportcefd.utils;

/**
 * Created by pgrippa on 14/09/16.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.utils.Util;

public class ServiceCursorAdapter extends RecyclerView.Adapter {
    private final Context context;
    private Cursor cursor;

    public ServiceCursorAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ServiceViewHolder holder = (ServiceViewHolder) viewHolder;

        int pPos, rPos, tPos;
        pPos = cursor.getColumnIndex("patrimony");
        rPos = cursor.getColumnIndex("responsible");
        tPos = cursor.getColumnIndex("type");

        if(pPos == -1 || rPos == -1 || tPos == -1){
            return;
        }

        String p = "";
        String r = "";
        String t = "";
        if(cursor.moveToPosition(position)){
            p = cursor.getString(pPos);
            r = cursor.getString(rPos);
            t = cursor.getString(tPos);
        }else{
            return;
        }

        holder.getResponsible().setText(r);
        holder.getPatrimony().setText(p);
        Util.setIconByType(context,holder.getIcon() , t);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
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
