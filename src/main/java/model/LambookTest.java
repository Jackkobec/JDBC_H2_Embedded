package model;

import lombok.Data;

@Data
public class LambookTest {
    //@Getter @Setter
    private int a = 7;

    public static void main(String[] args) {
        System.out.println(new LambookTest().getA());
    }
}
