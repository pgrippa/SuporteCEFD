package br.ufes.cefd.suportcefd.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pgrippa on 8/26/16.
 */
public class Service implements Serializable{

    private String patrimony;
    private String local;
    private String type;
    private String responsible;
    private String email;
    private String description;
    private Date entry;
    private String entryDate;
    private Date release;
    private String releaseDate;
    private String telephone;
    private long id;

    public Service(String p, String l, String t, String r, String d, String e, String tel) {
        this.setPatrimony(p);
        this.setLocal(l);
        this.setType(t);
        this.setResponsible(r);
        this.setDescription(d);
        this.setEmail(e);
        this.setTelephone(tel);
        this.setEntry(Calendar.getInstance().getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
        this.setEntryDate(dateFormat.format(getEntry()));
        this.setRelease(null);
    }


    @Override
    public String toString(){
        return getPatrimony() +" - "+ getLocal() +" - "+ getType() +" - "+ getResponsible();
    }

    public String getPatrimony() {
        return patrimony;
    }

    public void setPatrimony(String patrimony) {
        this.patrimony = patrimony;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getEntry() {
        return entry;
    }

    public void setEntry(Date entry) {
        this.entry = entry;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
