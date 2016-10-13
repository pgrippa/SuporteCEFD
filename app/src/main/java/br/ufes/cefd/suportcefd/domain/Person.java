package br.ufes.cefd.suportcefd.domain;

/**
 * Created by pgrippa on 12/10/16.
 */

public class Person {

    private long id;
    private String name;
    private String telephone;
    private String email;

    public Person(String name, String telephone, String email){
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }
}
