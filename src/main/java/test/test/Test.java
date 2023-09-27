package test.test;

import java.util.ArrayList;
import java.util.Collections;

public class Test {
    public static void main(String[] args) {

        ArrayList<Integer> numList = new ArrayList<>();

        numList.add(1);
        numList.add(3);
        numList.add(4);
        numList.add(7);
        numList.add(15);
        numList.add(2);
        numList.add(9);
        numList.add(25);

        Collections.sort(numList, Collections.reverseOrder());

        for(Integer  num : numList){
            System.out.println(num);
        }

    }
}
