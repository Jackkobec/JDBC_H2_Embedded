package model;

import lombok.Data;

/**
 * Created by Jack on 20.11.2016.
 */
@Data
public class LambookTest {
    //@Getter @Setter
    private int a = 7;

    public static void main(String[] args) {
        System.out.println(new LambookTest().getA());
    }
}
