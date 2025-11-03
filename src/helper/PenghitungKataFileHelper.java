/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author slozoy
 */
public class PenghitungKataFileHelper {
    public static void simpanKeFile(String teks, String hasil) {
        try (FileWriter writer = new FileWriter("hasil_penghitungan.txt")) {
            writer.write("=== Teks Asli ===\n");
            writer.write(teks + "\n\n");
            writer.write("=== Hasil Penghitungan ===\n");
            writer.write(hasil);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
