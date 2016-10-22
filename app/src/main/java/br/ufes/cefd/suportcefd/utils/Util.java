package br.ufes.cefd.suportcefd.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;

/**
 * Created by pgrippa on 16/09/16.
 */
public class Util {

    public static String FROMEMAIL = "suporte.cefd@gmail.com";
    public static String FROMNAME = "Suporte Informática CEFD";
    public static String FROMPASSWORD = "cefd321456987";

    public static void setIconByType(Context context, ImageView imageView, String tipo){
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

    public static void setStatusIcon(Context context, ImageView imageView, int status){
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

    public static String getMessage(Service service, Person p){
        Date end = service.getRelease();
        String close;
        if(end == null){
            close = "-";
        }else{
            close = end.toString();
        }
        String msg = "Olá " + p.getName() + ",<br>Este é um email gerado automaticamente pelo sistema de Helpdesk do CEFD, POR FAVOR NÃO RESPONDA ESTE EMAIL. <br>Qualquer email enviado para este email será automaticamente descartado.<br><br>"
                + "<table style=\"width: 700px;\" class=\"tab_cadre\">"
                + "<tbody>\n"
                + "<tr><th colspan=\"2\"><a href=\"\">MANUTEN&Ccedil;&Atilde;O DE COMPUTADORES</a></th><th></th><th></th></tr>\n"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Requerentes :</td>\n"
                + "<td> "+p.getName()+" </td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>\n"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Chamado Nº:</td>\n"
                + "<td>"+String.format("%07d", service.getId())+"</td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Patrimonio Equipamento:</td>\n"
                + "<td>"+service.getPatrimony()+"</td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Descricao:</td>\n"
                + "<td>"+service.getDescription()+"</td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Data de Abertura:</td>\n"
                + "<td>"+service.getEntry()+"</td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>"
                + "<tr class=\"tab_bg_1\">\n"
                + "<td>Data de Fechamento:</td>\n"
                + "<td>"+close+"</td>\n"
                + "<td colspan=\"2\"></td>\n"
                + "</tr>"

                ;

        return msg;
    }
}
