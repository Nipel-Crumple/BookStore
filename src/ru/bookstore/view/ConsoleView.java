package ru.bookstore.view;

import java.io.*;
import java.util.Scanner;
import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class ConsoleView {
    private PrintStream ps = null;
    private Scanner sc = null;
    private Logger logger = Logger.getLogger("ConsoleView");


    public ConsoleView(InputStream in, PrintStream ps) {
        this.ps = ps;
        sc = new Scanner(in);
    }

    public String[] readLine() {
        String s = sc.nextLine();
        String[] splitted =  s.split("\\s+");
        return splitted;
    }

    public String readLogin() {
        String regex = "[\\w\\-_]+";
        String s = sc.nextLine();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return s;
        } else {
            logger.error("Incorrect input Login. Try again");
            return null;
        }
    }


    public String readPassword() {
        String regex = "\\w{3,20}";
        String s = sc.nextLine();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return s;
        } else {
            logger.error("Incorrect input. Try again");
            return null;
        }
    }

    public void println(String s) {
        ps.println(s);
    }

    public void println() {
        ps.println();
    }
    public void print(String s) {
        ps.print(s);
    }
    /*public static void main(String[] args) {
        ConsoleView cs = new ConsoleView(System.in, System.out);
        String pass = cs.readPassword();
        System.out.println(pass);
    }*/
}
