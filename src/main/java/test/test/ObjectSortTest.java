package test.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ObjectSortTest {
    public static void main(String[] args) {
        ArrayList<Person> personList = new ArrayList<>();

        Person p1 = new Person(22, 160, "홍길동");
        Person p2 = new Person(61, 175, "이순신");
        Person p3 = new Person(33, 188, "임꺽정");
        Person p4 = new Person(18, 171, "황진이");
        Person p5 = new Person(9, 130, "홍길순");

        personList.add(p1);
        personList.add(p2);
        personList.add(p3);
        personList.add(p4);
        personList.add(p5);

        for (Person person : personList) {
            person.introduce();
        }
        PersonComparator pc = new PersonComparator();
        Collections.sort(personList, pc);

        for (Person person : personList) {
            person.introduce();
        }
    }
}

class PersonComparator implements Comparator<Person> {


    @Override
    public int compare(Person o1, Person o2) {
        if (o1.getHeight() < o2.getHeight()) {
            return 1;
        }
        return -1; // 1을 리턴 -> 자리를 바꿈. -1 리턴 자리 유지.
    }
}

class Person {
    private int age;
    private double height;
    private String name;

    public Person(int age, double height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    public void introduce() {
        System.out.printf("%d살 %s입니다. 키는 %f cm입니다.\n", age, name, height);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}