package com.example.explosivechat;
import java.util.Random;

public class LetterSingleton {
    private String bombLetter = "L";
	private static LetterSingleton instance;
	
	public static synchronized LetterSingleton getInstance() {
		if (instance == null) {
			instance = new LetterSingleton();
		}
		return instance;
	}

    public void newLetter() {
        Random rand = new Random();

        int n = rand.nextInt(8);

        switch (n) {
            case 0:
                setBombLetter("м");
                break;
            case 1:
                setBombLetter("к");
                break;
            case 2:
                setBombLetter("л");
                break;
            case 3:
                setBombLetter("в");
                break;
            case 4:
                setBombLetter("р");
                break;
            case 5:
                setBombLetter("с");
                break;
            case 6:
                setBombLetter("т");
                break;
            case 7:
                setBombLetter("н");
                break;
            default:
                throw new AssertionError();
        }
    }

    public void setBombLetter(String newLett) {
        instance.bombLetter = newLett;
    }

    public String getLetter() {
        return instance.bombLetter;
    }
}
