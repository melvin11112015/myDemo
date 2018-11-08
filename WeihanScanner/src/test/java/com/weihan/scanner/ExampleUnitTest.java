package com.weihan.scanner;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(new MyNum()));
    }

    @Test
    public void patternTest() throws Exception {

        assertTrue(Pattern.compile("^(-?\\d+)(\\.\\d+)?$").matcher("-1").matches());

    }

    private class MyNum {
        double haha = 5.00f;
    }
}