package br.ufes.cefd.suportcefd.domain;

/**
 * Created by pgrippa on 12/10/16.
 */

public class Person {

    private long id;
    private String name;
    private String telephone;
    private String email;
    private String password;

    public Person(String name, String telephone, String email, String password){
        this.setName(name);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setPassword(password);
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
}
