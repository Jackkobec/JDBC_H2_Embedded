package model;

import lombok.Data;

@Data
public class Student {

    private int id;
    private String name;
    private int group_id;

    public Student() {
    }
}
