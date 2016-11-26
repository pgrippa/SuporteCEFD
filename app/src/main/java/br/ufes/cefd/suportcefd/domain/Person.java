package br.ufes.cefd.suportcefd.domain;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by pgrippa on 12/10/16.
 */

public class Person implements Serializable{

    public enum TYPE{
        ADMIN, USER;
    }

    private long id;
    private String name;
    private String telephone;
    private String email;
    private String password;
    private String type;

    public Person(String name, String telephone, String email, String password, String type){
        this.setName(name);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setPassword(password);
        this.setType(type);
    }

    public Person(Cursor cursor){
        cursor.moveToFirst();
        this.setId(cursor.getLong(0));
        this.setName(cursor.getString(1));
        this.setTelephone(cursor.getString(2));
        this.setEmail(cursor.getString(3));
        this.setPassword(cursor.getString(4));
        this.setType(cursor.getString(5));
    }

    public Person(JSONObject jsonObject){
        try {
            this.id = Long.parseLong(jsonObject.getString("_ID"));
            this.name = String.valueOf(jsonObject.getString("name"));
            this.telephone = String.valueOf(jsonObject.getString("telephone"));
            this.email = String.valueOf(jsonObject.getString("email"));
            this.password = String.valueOf(jsonObject.getString("password"));
            this.type = String.valueOf(jsonObject.getString("type"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return this.getName();
    }

}
