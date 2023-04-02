package me.femrene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BotConfig {
    protected static void CreateConfig() throws IOException {
        File file = new File("config.txt");
        if (!file.exists()) {
            Scanner scanner = new Scanner(System.in);
            FileWriter writer = new FileWriter(file);
            System.out.println("Enter your token here (eg. MTA5MTczOTM2MzMzNzA2MDM1Mg.GhSvp2.h_q1uXD9-xvRG3V7AvEY3dbZFcPVKmZ6FLUFgc)");
            String token = scanner.nextLine();
            System.out.println("Enter your Activity here (eg. your favourite Music)");
            String act = scanner.nextLine();
            writer.write("token="+token+"\n");
            writer.write("activity="+act+"\n");
            writer.flush();
            writer.close();
        }
    }

    public static String getString(String path) throws IOException {
        File file = new File("config.txt");
        if (!file.exists()){
            CreateConfig();
        }
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()){
                String line = scan.nextLine();
                if (line.contains(path)) {
                    String[] result = line.split("=");
                    return result[1];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Config wurde nicht gelesen!");
            throw new RuntimeException(e);
        }
        return null;
    }
}
