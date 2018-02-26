package model;

import lombok.Data;

/**
 * Created by Jack on 12.11.2016.
 */
@Data
public class Student {

    private int id;
    private String name;
    private int group_id;

    public Student() {
    }
}
