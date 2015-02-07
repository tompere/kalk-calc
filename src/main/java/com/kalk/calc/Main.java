package com.kalk.calc;

import com.google.common.base.Stopwatch;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    static Stopwatch stopwatch;

    static int[] set = new int[5];

    static int target = -1;

    public static void main(String[] args){
        String errorMessageFormat = "input was invalid (%s), please try again and follow the instructions";
        try {
            startInteractive();
            playGame();
        } catch (NumberFormatException e){
            System.err.println(String.format(errorMessageFormat, "non-numeric characters"));
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println(String.format(errorMessageFormat, "set was bigger than 5 numbers"));
        } catch (IllegalArgumentException e){
            System.err.println(String.format(errorMessageFormat, e.getMessage()));
        }
        System.exit(1);
    }

    private static void startInteractive() {
        System.out.println(getHeader());
        Scanner in = new Scanner(System.in);
        System.out.println("██ Enter 5 numbers set in the following format: 2 5 4 8 10");
        System.out.print("██ > ");
        parseSetInput(in.nextLine());
        System.out.println("██ Enter the target number:");
        System.out.print("██ > ");
        parseTraget(in.nextLine());
    }

    private static void parseTraget(String s) {
        target = Integer.parseInt(s.trim());
    }

    private static void parseSetInput(String input) {
        int i=0;
        for (String rawNum : input.split(" ")){
            rawNum = rawNum.trim();
            if (!rawNum.isEmpty()){
                int num = Integer.parseInt(rawNum);
                if (num < 0 || num > 10){
                    throw new IllegalArgumentException("number " + num + " is invalid, can only be between 0 and 10");
                }
                set[i++] = num;
            }
        }
        if (i < 5){
            throw new IllegalArgumentException("set was of size " + i + ", size should be 5");
        }
    }

    private static void playGame() {
        stopwatch = Stopwatch.createStarted();
        GivenSet gamePlay = new GivenSet(set, target);
        System.out.println("calculating possible solutions ... ");
        if (!gamePlay.isSetGood()){
            System.out.println("There are no solutions for the given set");
        }
        stopwatch.stop();
        System.out.println("Done. Took " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds.");
    }

    private static String getHeader(){
        return "\n" +
                "██╗  ██╗ █████╗ ██╗     ██╗  ██╗      ██████╗ █████╗ ██╗      ██████╗\n" +
                "██║ ██╔╝██╔══██╗██║     ██║ ██╔╝     ██╔════╝██╔══██╗██║     ██╔════╝\n" +
                "█████╔╝ ███████║██║     █████╔╝█████╗██║     ███████║██║     ██║     \n" +
                "██╔═██╗ ██╔══██║██║     ██╔═██╗╚════╝██║     ██╔══██║██║     ██║     \n" +
                "██║  ██╗██║  ██║███████╗██║  ██╗     ╚██████╗██║  ██║███████╗╚██████╗\n" +
                "╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝      ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝\n" +
                "powered by @(github.com/tompere/kalk-calc)" +
                "\n";
    }

}
