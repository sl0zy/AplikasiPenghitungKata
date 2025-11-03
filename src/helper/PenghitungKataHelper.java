/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

/**
 *
 * @author slozoy
 */
public class PenghitungKataHelper {
    public static int hitungKata(String teks) {
        String[] words = teks.trim().split("\\s+");
        return teks.trim().isEmpty() ? 0 : words.length;
    }

    public static int hitungKarakter(String teks) {
        return teks.length();
    }

    public static int hitungKalimat(String teks) {
        String[] sentences = teks.split("[.!?]+");
        return teks.trim().isEmpty() ? 0 : sentences.length;
    }

    public static int hitungParagraf(String teks) {
        if (teks.trim().isEmpty()) return 0;
        // Cocok untuk Windows (\r\n), Mac (\r), dan Linux (\n)
        String[] paragraphs = teks.trim().split("(\\r?\\n)+");
        return paragraphs.length;
    }

    public static int cariKata(String teks, String kata) {
        if (kata == null || kata.isEmpty()) return 0;
        String regex = "\\b" + kata + "\\b";
        return teks.split(regex, -1).length - 1;
    }
}
