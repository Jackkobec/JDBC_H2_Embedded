package model;

import lombok.Data;

@Data
public class Lesson {

    private int id;
    private String name;
    private String description;

    public Lesson() {
    }
}
