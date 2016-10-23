package br.ufes.cefd.suportcefd.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.utils.Util;

/**
 * Created by pgrippa on 17/09/16.
 */
public class SpinnerItemAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public SpinnerItemAdapter(Context context, String[] values) {
        super(context, R.layout.spinner_item, R.id.s_tipo, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label=(TextView)row.findViewById(R.id.s_tipo);
        label.setText(values[position]);

        ImageView imageView=(ImageView)row.findViewById(R.id.i_icon);

        String s = values[position];

        Util.setIconByType(context, imageView, s);

        /*if (s.equals(context.getString(R.string.t_desktop))) {
            imageView.setImageResource(R.drawable.desktop);
        } else if (s.equals(context.getString(R.string.t_notebook))) {
            imageView.setImageResource(R.drawable.notebook);
        } else if (s.equals(context.getString(R.string.t_impressora))) {
            imageView.setImageResource(R.drawable.printer);
        } else if (s.equals(context.getString(R.string.t_monitor))) {
            imageView.setImageResource(R.drawable.monitor);
        }else if (s.equals(context.getString(R.string.t_network))) {
            imageView.setImageResource(R.drawable.network2);
        }else{
            imageView.setImageResource(R.drawable.maintenance);
        }*/

        return row;
    }
}
