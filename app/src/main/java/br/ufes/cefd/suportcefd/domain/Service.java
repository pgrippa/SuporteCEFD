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
    private String description;
    private Date entry;
    private String entryDate;
    private Date release;
    private String releaseDate;
    private long id;
    private long idResp;
    private int active;
    public static int ACTIVE = 1;
    public static int INACTIVE = 0;

    public Service (String p, String l, String t, String d, long idResp){
        this.patrimony = p;
        this.local = l;
        this.type = t;
        this.description = d;
        this.idResp = idResp;
        this.active = ACTIVE;

        this.setEntry(Calendar.getInstance().getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
        this.setEntryDate(dateFormat.format(getEntry()));
        this.setReleaseDate("");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
        setReleaseDate(dateFormat.format(release));
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

    public long getIdResp() {
        return idResp;
    }

    public void setIdResp(long idResp) {
        this.idResp = idResp;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString(){
        return patrimony+" - "+type;
    }
}
