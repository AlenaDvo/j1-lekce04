package cz.czechitas.kockamyssyr;

import dev.czechitas.java1.kockamyssyr.api.*;

import java.awt.*;
import java.util.Random;

/**
 * Hlaví třída pro hru Kočka–myš–sýr.
 */
public class HlavniProgram {
    private final Random random = new Random();

    private final int VELIKOST_PRVKU = 50;
    private final int SIRKA_OKNA = 1000 - VELIKOST_PRVKU;
    private final int VYSKA_OKNA = 600 - VELIKOST_PRVKU;

    private Cat tom;
    private Mouse jerry;

    private Cheese syr;

    private Meat jitrnice;
    /**
     * Spouštěcí metoda celé aplikace.
     *
     * @param args
     */
    public static void main(String[] args) {
        new HlavniProgram().run();
    }

    /**
     * Hlavní metoda obsahující výkonný kód.
     */
    public void run() {
        tom = vytvorKocku();
        tom.setBrain(new KeyboardBrain(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D));

        jerry = vytvorMys();
        jerry.setBrain(new KeyboardBrain());

        vytvorVeci(4);
        chytMys();
    }

    public void chytMys() {
//        System.out.println("X tom: " + tom.getX() + " jerry: " + jerry.getX());
//        System.out.println("Y tom: " + tom.getY() + " jerry: " + jerry.getY());
        while (jerry.isAlive() && (jitrnice.isAlive() || syr.isAlive())) {
////            osa x
            int xRozdil = jerry.getX() - tom.getX();
            if (xRozdil < -15 || xRozdil > 15) {
//                System.out.println("X tom: " + tom.getX() + " jerry: " + jerry.getX() + " rozdil: " + xRozdil);
////                nejsme na stejno
                if (xRozdil < 0) {
////                    tom je vic vpravo
                    jdiDoleva(-xRozdil);
                } else {
//                    tom je vic vlevo
                    jdiDoprava(xRozdil);
                }
            } else if (!tom.isPossibleToMoveForward()) {
//                System.out.println("obchazim strom");
                obejdiStrom(30);
            }
//            osa y
            int yRozdil = jerry.getY() - tom.getY();
            if (yRozdil < -15 || xRozdil > 15) {
//                System.out.println("Y tom: " + tom.getY() + " jerry: " + jerry.getY() + " rozdil: " + yRozdil);
////            nejsme na stejno
                if (yRozdil < 0) {
////                tom je vic nahore
                    jdiNahoru(-yRozdil);
                } else {
//                  tom je vic dole
                    jdiDolu(yRozdil);
                }
            } else if (!tom.isPossibleToMoveForward()) {
//                System.out.println("obchazim strom");
                obejdiStrom(30);
            }
        }
    }


    public void jdiDoleva(int oKolik) {
        if (tom.getOrientation() == PlayerOrientation.RIGHT) {
//          tom se diva doprava, otoci se doleva
            tom.turnLeft();
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.DOWN) {
//          tom se diva dolu, otoci se doleva
            tom.turnRight();
        } else if (tom.getOrientation() == PlayerOrientation.UP) {
//          tom se diva nahoru, otoci se doleva
            tom.turnLeft();
        }
//        System.out.println("jdu doleva " + oKolik);
        if (!tom.isPossibleToMoveForward()) {
//            System.out.println("obchazim strom");
            obejdiStrom(30);
        }
        tom.moveForward(oKolik);
    }

    public void jdiDoprava(int oKolik) {
        if (tom.getOrientation() == PlayerOrientation.LEFT) {
//          tom se diva doleva, otoci se doprava
            tom.turnLeft();
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.DOWN) {
//          tom se diva dolu, otoci se doprava
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.UP) {
//          tom se diva nahoru, otoci se doprava
            tom.turnRight();
        }
        if (!tom.isPossibleToMoveForward()) {
//            System.out.println("obchazim strom");
            obejdiStrom(30);
        }
//        System.out.println("jdu doprava " + oKolik);
        tom.moveForward(oKolik);
    }

    public void jdiNahoru(int oKolik) {
        if (tom.getOrientation() == PlayerOrientation.DOWN) {
//          tom se diva dolu, otoci se nahoru
            tom.turnLeft();
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.LEFT) {
//          tom se diva doleva, otoci se nahoru
            tom.turnRight();
        } else if (tom.getOrientation() == PlayerOrientation.RIGHT) {
//          tom se diva doprava, otoci se nahoru
            tom.turnLeft();
        }
        if (!tom.isPossibleToMoveForward()) {
//            System.out.println("obchazim strom");
            obejdiStrom(50);
        }
//        System.out.println("jdu nahoru " + oKolik);
        tom.moveForward(oKolik);
    }

    public void jdiDolu(int oKolik) {
        if (tom.getOrientation() == PlayerOrientation.UP) {
//          tom se diva nahoru, otoci se dolu
            tom.turnLeft();
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.LEFT) {
//          tom se diva doleva, otoci se dolu
            tom.turnLeft();
        } else if (tom.getOrientation() == PlayerOrientation.RIGHT) {
//          tom se diva doprava, otoci se dolu
            tom.turnRight();
        }
        if (!tom.isPossibleToMoveForward()) {
//            System.out.println("obchazim strom");
            obejdiStrom(50);
        }
//        System.out.println("jdu dolu " + oKolik);
        tom.moveForward(oKolik);
    }

    public void obejdiStrom(int oKolik) {
        tom.turnRight();
//        System.out.println("otacim se doprava");
        if (tom.isPossibleToMoveForward()) {
//            System.out.println("muzu dopredu");
            tom.moveForward(oKolik);
            tom.turnLeft();
        } else {
//            System.out.println("nemuzu dopredu");
            obejdiStrom(oKolik);
        }
    }

    public void vytvorVeci(int pocetStromu) {
        for (int i = 0; i < pocetStromu; i++) {
            vytvorStrom();
        }
        syr = vytvorSyr();
        jitrnice = vytvorJitrnici();
    }

    public Tree vytvorStrom() {
        return new Tree(vytvorNahodnyBod());
    }

    public Cat vytvorKocku() {
        return new Cat(vytvorNahodnyBod());
    }

    public Mouse vytvorMys() {
        return new Mouse(vytvorNahodnyBod());
    }

    public Cheese vytvorSyr() {
        return new Cheese(vytvorNahodnyBod());
    }

    public Meat vytvorJitrnici() {
        return new Meat(vytvorNahodnyBod());
    }

    private Point vytvorNahodnyBod() {
        return new Point(random.nextInt(SIRKA_OKNA), random.nextInt(VYSKA_OKNA));
    }

}
