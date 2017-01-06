package org.tiwindetea.animewarfare.net;

import org.junit.Test;

import java.util.Random;

/**
 * Created by maliafo on 05/01/17.
 */
@SuppressWarnings("ALL")
public class auei {

    static final String[] names = {"Suna", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto", "Ceres", "Pallas", "Vesta", "Hygiea", "Interamnia", "Europa", "Davida", "Sylvia", "Cybele", "Eunomia", "Juno", "Euphrosyne", "Hektor", "Thisbe", "Bamberga", "Patientia", "Herculina", "Doris", "Ursula", "Camilla", "Eugenia", "Iris", "Amphitrite"};

    @Test
    public void generateName() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String out = new String();
            int syllableNbr = random.nextInt(2) + 2;
            for (int j = 0; j < syllableNbr; j++) {
                String word = names[random.nextInt(names.length)];
                int tmp = random.nextInt(word.length() - 2);
                int tmp2 = random.nextInt(2) + 2 + tmp;
                if (tmp2 > word.length()) {
                    tmp2 = word.length();
                }
                out += word.substring(tmp, tmp2);
            }
            System.out.println(out.substring(0, 1).toUpperCase() + out.substring(1).toLowerCase());
        }
    }
}
