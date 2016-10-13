package br.ufes.cefd.suportcefd.domain;

/**
 * Created by pgrippa on 12/10/16.
 */

public class User extends Person {
    private String password;

    public User(String name, String telephone, String email, String password) {
        super(name, telephone, email);
        this.password = password;
    }
}
