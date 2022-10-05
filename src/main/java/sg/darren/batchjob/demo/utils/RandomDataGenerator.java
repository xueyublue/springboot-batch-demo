package sg.darren.batchjob.demo.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {

    private static final Random RANDOM = new Random();
    private static final List<String> FIRST_NAMES = Arrays.asList("Abigail", "Emily", "Harper", "Daliah", "Amelia", "Zane", "Evelyn", "Elizabeth", "Sofia", "Madison", "Avery", "Ella", "Scarlett", "Grace", "Chloe", "Ahmet", "Berat", "Yildrim", "Kaya", "Jìng", "Li", "Wei", "Qiang,", "Minh");
    private static final List<String> LAST_NAMES = Arrays.asList("Singh", "Lee", "Smith", "Khan", "Nguyen", "Shah", "Chen", "Fox", "Gill", "Burke", "Potter", "Carvalho", "Sanchez", "Liu", "Aziz", "Mueller", "Lang", "Wolf", "Small", "Michael", "Knox", "Winters", "Sommers", "Spring");

    public static String getFirstName() {
        return FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
    }

    public static String getLastName() {
        return FIRST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
    }

    public static String getFullName() {
        String firstName = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
        return firstName + " " + lastName;
    }

    public static String getEmail() {
        String firstName = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
        return firstName + "." + lastName + "@domain.xyz";
    }

}
