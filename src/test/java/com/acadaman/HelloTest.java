package com.acadaman;

import static java.lang.System.out;

public class HelloTest {

    Runnable r1 = () -> out.println(this);
    Runnable r2 = () -> out.println(toString());

    public String toString() { return "Hello, world!";  }

    public static void main(String... args) {
        new HelloTest().r1.run();
        new HelloTest().r1.run();
    }
}
