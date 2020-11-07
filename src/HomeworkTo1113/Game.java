package HomeworkTo1113;

import Methods.Methods;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

class GameTest {
    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.start();
    }
}

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;

    private final double MIN_CHANCE_TO_HIT = 0.3f;
    private final double MAX_CHANCE_TO_HIT = 0.95f;

    private final int MAX_STRENGTH = 8;

    public void start() {
        displayRules();

        setPlayers();

        int turnCounter = 0;
        while (!isSomeoneLostGame()) {
            Methods.line();
            System.out.println(
                    "Remaining health: \n" +
                            firstPlayer.getName() + ": " + firstPlayer.getHp() + "\n" +
                            secondPlayer.getName() + ": " + secondPlayer.getHp() + "\n");

            if (turnCounter % 2 == 0) {
                System.out.println(
                        firstPlayer.getName() + "'s turn!");
            } else {
                System.out.println(
                        secondPlayer.getName() + "'s turn!");
            }


            System.out.println("Choose the strength of a punch from 1 to " + MAX_STRENGTH + ": ");
            String strength;
            while (!isInputProper(strength = Methods.getString(), MAX_STRENGTH)) {
            }
            hit(Integer.parseInt(strength), turnCounter++ % 2 == 0);
        }
    }

    private void setPlayers() {
        firstPlayer = new Player();
        secondPlayer = new Player();

        // first player's name setting section
        System.out.println("Enter first player's name: ");
        String name;
        while ((name = Methods.getString()).isEmpty() || name.replaceAll(" ", "").isEmpty()) { // skipping empty strings
            System.out.println("Please, enter a valid name: ");
        }
        firstPlayer.setName(name);

        // second player's name setting section
        System.out.println("Enter second player's name: ");
        while ((name = Methods.getString()).isEmpty() ||
                name.replaceAll(" ", "").isEmpty() ||
                name.equals(firstPlayer.getName())) {
            System.out.println(name.equals(firstPlayer.getName()) ?
                    "Players must not have same names! " : "Please, enter a valid name: ");
        }
        secondPlayer.setName(name);

        // HP setting section
        System.out.println("Enter health amount from 1 to 100 for each player: ");
        byte health;
        String input;
        do {
            input = Methods.getString();
        } while (!isInputProper(input, 100));
        health = (byte) Integer.parseInt(input);
        firstPlayer.setHp(health);
        secondPlayer.setHp(health);
    }

    private void hit(int power, boolean isFirstPlayerTurn) {
        String s;
        if (power == 1) s = "";
        else s = "s";

        if (isFirstPlayerTurn) {
            if (Methods.getRandDouble() < getChance(power)) {
                System.out.println(
                        "You got him with a " + getFormattedChance(power) + " chance to hit! \n" +
                                secondPlayer.getName() + " looses " + power + " health point" + s + "!");
                secondPlayer.setHp((byte) (secondPlayer.getHp() - power));
            } else {
                System.out.println("Unfortunately, you missed with a " + getFormattedChance(power) + " chance to hit!");
            }
        } else {
            if (Methods.getRandDouble() < getChance(power)) {
                System.out.println(
                        "You got him with a " + getFormattedChance(power) + " chance to hit! \n" +
                                firstPlayer.getName() + " looses " + power + " health point" + s + "!");
                firstPlayer.setHp((byte) (firstPlayer.getHp() - power));
            } else {
                System.out.println("Unfortunately, you missed with a " + getFormattedChance(power) + " chance to hit!");
            }
        }
    }

    private boolean isSomeoneLostGame() {
        if (firstPlayer.getHp() <= 0) {
            System.out.println(
                    "\n" + firstPlayer.getName() + " looses the game \n" +
                            "Better luck to you next time!" +
                            "\n\n" + secondPlayer.getName() + " wins! \n" +
                            "Congratulations!");
            return true;
        } else if (secondPlayer.getHp() <= 0) {
            System.out.println(
                    "\n" + secondPlayer.getName() + " looses the game \n" +
                            "Better luck to you next time!" +
                            "\n\n" + firstPlayer.getName() + " wins! \n" +
                            "Congratulations!");
            return true;
        } else
            return false;
    }

    private boolean isInputProper(String input, int upperBound) {
        if (input.length() <= 3 && // not too long
                NumberUtils.isCreatable(input) && // is a number
                Integer.parseInt(input) > 0 && // positive
                Integer.parseInt(input) <= upperBound) { // not too big
            return true;
        } else {
            System.out.println("Please, enter a valid number from 1 to " + upperBound + ": ");
            return false;
        }
    }

    private double getChance(int power) {
        double section = (MAX_CHANCE_TO_HIT - MIN_CHANCE_TO_HIT) / (MAX_STRENGTH - 1);
        return MIN_CHANCE_TO_HIT + section * (MAX_STRENGTH - power);
    }

    private String getFormattedChance(int power) {
        BigDecimal bigDecimal = BigDecimal.valueOf(getChance(power) * 100);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue() + "%";
    }

    private void displayRules() {
        Methods.line();
        System.out.println("In this game your goal is to beat your opponent up \n" +
                "each turn you have to punch him with strength measured from 1 to " + MAX_STRENGTH + ". \n" +
                "If you don't miss the enemy, then he will lose health equal to your punch strength, \n" +
                "but aware that stronger the punch then less the chance to hit. \n" +
                "Here is the list of probabilities to hit your opponent depending on the punch power: ");
        for (int i = 1; i <= MAX_STRENGTH; i++) {
            System.out.println("Strength: " + i + " -> probability: " + getFormattedChance(i));
        }
        Methods.line();
    }

    private class Player {
        private String name;
        private byte hp;

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name.replaceFirst(
                    Character.toString(name.charAt(0)),
                    Character.toString(Character.toUpperCase(name.charAt(0)))
                            .replaceAll(" ", ""));
        }

        private byte getHp() {
            return hp;
        }

        private void setHp(byte hp) {
            this.hp = hp;
        }

        @Override
        public String toString() {
            return "Player {" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}