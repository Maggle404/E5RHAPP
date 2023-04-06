package org.example;


public class User {
    private int id;
    private String name;
    private String lastname;
    private String email;
    private int age;
    private String job;

    public User(int id, String name, String lastname, String email, int age, String job)
    {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.job = job;
    }



    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getLastname()
    {
        return lastname;
    }
    public String getEmail()
    {
        return email;
    }
    public int getAge()
    {
        return age;
    }
    public String getJob()
    {
        return job;
    }
}