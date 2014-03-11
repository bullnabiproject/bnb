package com.bullnabi.bnb.domain;

public class RestDTO {

    private long id;
    
    private String name;
   
    private String job;
   
    public RestDTO() {
           this(0, "", "");
    }
   
    public RestDTO(long id, String name, String job) {
           this.id = id;
           this.name = name;
           this.job = job;
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

    public String getJob() {
           return job;
    }

    public void setJob(String job) {
           this.job = job;
    }
}
