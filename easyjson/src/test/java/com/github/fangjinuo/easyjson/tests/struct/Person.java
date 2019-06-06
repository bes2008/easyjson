package com.github.fangjinuo.easyjson.tests.struct;

import java.util.Date;
import java.util.Objects;

public class Person {
    private int id;
    private String name;
    private Date birthday;
    private Contact contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != person.id) return false;
        if (!name.equals(person.name)) return false;
        if (!birthday.equals(person.birthday)) return false;
        return contact.equals(person.contact);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + birthday.hashCode();
        result = 31 * result + contact.hashCode();
        return result;
    }
}
