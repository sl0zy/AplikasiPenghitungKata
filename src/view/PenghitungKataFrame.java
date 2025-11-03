/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import helper.PenghitungKataHelper;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author slozoy
 */
public class PenghitungKataFrame extends javax.swing.JFrame {

    /**
     * Creates new form PenghitungKataFrame
     */
    public PenghitungKataFrame() {
        initComponents();
        
        // === Placeholder Setup untuk txtInput ===
        Color placeholderColor = new Color(150, 150, 150);
        Color normalColor = Color.BLACK;

        // Set placeholder awal
        txtInput.setForeground(placeholderColor);
        txtInput.setText("Silahkan input teks disini!");

        // Nonaktifkan tombol awalnya
        btnHitung.setEnabled(false);
        btnCari.setEnabled(false);
        btnSimpan.setEnabled(false);

        // Listener untuk txtInput
        txtInput.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtInput.getText().equals("Silahkan input teks disini!")) {
                    txtInput.setText("");
                    txtInput.setForeground(normalColor);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtInput.getText().trim().isEmpty()) {
                    txtInput.setText("Silahkan input teks disini!");
                    txtInput.setForeground(placeholderColor);
                }
                updateButtonState();
            }
        });

        // === Placeholder Setup untuk txtCari ===
        txtCari.setForeground(placeholderColor);
        txtCari.setText("Masukkan kata yang ingin dicari...");

        txtCari.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtCari.getText().equals("Masukkan kata yang ingin dicari...")) {
                    txtCari.setText("");
                    txtCari.setForeground(normalColor);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtCari.getText().trim().isEmpty()) {
                    txtCari.setText("Masukkan kata yang ingin dicari...");
                    txtCari.setForeground(placeholderColor);
                }
                updateButtonState();
            }
        });

        // === DocumentListener untuk update tombol saat mengetik ===
        txtInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleInputChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleInputChange(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleInputChange(); }
        });

        txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleCariChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleCariChange(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleCariChange(); }
        });
    }
    
    private void updateButtonState() {
        String teksInput = txtInput.getText().trim();
        String teksCari = txtCari.getText().trim();

        boolean inputValid = !teksInput.isEmpty() && 
                             !teksInput.equals("Silahkan input teks disini!");
        boolean cariValid = !teksCari.isEmpty() &&
                            !teksCari.equals("Masukkan kata yang ingin dicari...");

        btnHitung.setEnabled(inputValid);
        btnSimpan.setEnabled(inputValid);
        btnCari.setEnabled(inputValid && cariValid);
    }
    
    private void updateSearchResultPlaceholder() {
        String teksCari = txtCari.getText().trim();

        // Jika kosong atau masih placeholder
        if (teksCari.isEmpty() || teksCari.equals("Masukkan kata yang ingin dicari...")) {
            lblHasilCari.setText("Hasil Pencarian Kata akan ditampilkan disini.");
        }
    }
    
    private void updateStats() {
        String teks = txtInput.getText().trim();

        // Jika kosong atau masih placeholder → tampilkan teks default
        if (teks.isEmpty() || teks.equals("Silahkan input teks disini!")) {
            lblKata.setText("Hasil Jumlah Kata akan ditampilkan disini.");
            lblKarakter.setText("Hasil Jumlah Karakter akan ditampilkan disini.");
            lblKalimat.setText("Tekan Tombol Hitung untuk Menghitung Jumlah Kalimat!");
            lblParagraf.setText("Tekan Tombol Hitung untuk Menghitung Jumlah Paragraf!");
            return;
        }

        // Jika ada teks valid → tampilkan hasil real-time
        int kata = helper.PenghitungKataHelper.hitungKata(teks);
        int karakter = helper.PenghitungKataHelper.hitungKarakter(teks);

        lblKata.setText("Jumlah Kata: " + kata);
        lblKarakter.setText("Jumlah Karakter: " + karakter);
    }
    
    private void handleCariChange() {
        updateButtonState();
        updateSearchResultPlaceholder();
    }
    
    private void handleInputChange() {
        updateStats();
        updateButtonState();
    }
    
    private void simpanTXT() {
        String teks = txtInput.getText().trim();

        // Jalankan logika hitung otomatis (kalau user belum tekan tombol Hitung)
        int jumlahKata = helper.PenghitungKataHelper.hitungKata(teks);
        int jumlahKarakter = helper.PenghitungKataHelper.hitungKarakter(teks);
        int jumlahKalimat = helper.PenghitungKataHelper.hitungKalimat(teks);
        int jumlahParagraf = helper.PenghitungKataHelper.hitungParagraf(teks);

        // Pastikan label di-update juga biar sinkron
        lblKata.setText("Jumlah Kata: " + jumlahKata);
        lblKarakter.setText("Jumlah Karakter: " + jumlahKarakter);
        lblKalimat.setText("Jumlah Kalimat: " + jumlahKalimat);
        lblParagraf.setText("Jumlah Paragraf: " + jumlahParagraf);

        // Lanjut ke pemilihan file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Hasil Penghitungan");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Tambahkan ekstensi .txt jika belum ada
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            String hasil =
                "Jumlah Kata: " + jumlahKata + "\n" +
                "Jumlah Karakter: " + jumlahKarakter + "\n" +
                "Jumlah Kalimat: " + jumlahKalimat + "\n" +
                "Jumlah Paragraf: " + jumlahParagraf + "\n";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write("=== Teks Asli ===\n");
                writer.write(teks + "\n\n");
                writer.write("=== Hasil Penghitungan ===\n");
                writer.write(hasil);

                JOptionPane.showMessageDialog(this,
                    "Teks dan hasil berhasil disimpan ke:\n" + fileToSave.getAbsolutePath(),
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInput = new javax.swing.JTextArea();
        txtCari = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        lblHasilCari = new javax.swing.JLabel();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblKata = new javax.swing.JLabel();
        lblKarakter = new javax.swing.JLabel();
        lblKalimat = new javax.swing.JLabel();
        lblParagraf = new javax.swing.JLabel();
        btnHitung = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("2310010054 - Said Muhdaffa Hasyim");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setText("Aplikasi Penghitung Kata");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("Pencarian Kata:");

        txtInput.setColumns(20);
        txtInput.setLineWrap(true);
        txtInput.setRows(5);
        txtInput.setText("Silahkan input teks disini!");
        txtInput.setWrapStyleWord(true);
        txtInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInputFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInputFocusLost(evt);
            }
        });
        txtInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtInputMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtInput);

        txtCari.setText("Masukkan kata yang ingin dicari...");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblHasilCari.setText("Hasil Pencarian Kata akan ditampilkan disini.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHasilCari, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHasilCari)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCari.setText("Cari Kata");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblKata.setText("Hasil Jumlah Kata akan ditampilkan disini.");

        lblKarakter.setText("Hasil Jumlah Karakter akan ditampilkan disini. ");

        lblKalimat.setText("Tekan Tombol Hitung untuk Menghitung Jumlah Kalimat!");

        lblParagraf.setText("Tekan Tombol Hitung untuk Menghitung Jumlah Paragraf!");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblKata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblKarakter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblKalimat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblParagraf, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblKarakter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblKalimat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblParagraf)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnHitung.setText("Hitung");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan ");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(btnHitung, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHitung, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        // TODO add your handling code here:
        String teks = txtInput.getText();
        int kata = PenghitungKataHelper.hitungKata(teks);
        int karakter = PenghitungKataHelper.hitungKarakter(teks);
        int kalimat = PenghitungKataHelper.hitungKalimat(teks);
        int paragraf = PenghitungKataHelper.hitungParagraf(teks);

        lblKata.setText("Jumlah Kata: " + kata);
        lblKarakter.setText("Jumlah Karakter: " + karakter);
        lblKalimat.setText("Jumlah Kalimat: " + kalimat);
        lblParagraf.setText("Jumlah Paragraf: " + paragraf);
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
            String teks = txtInput.getText();
        String kataDicari = txtCari.getText();

        // Cek apakah valid
        if (kataDicari.equals("Masukkan kata yang ingin dicari...") || kataDicari.trim().isEmpty()) {
            lblHasilCari.setText("Hasil Pencarian Kata akan ditampilkan disini.");
            return;
        }

        int jumlah = helper.PenghitungKataHelper.cariKata(teks, kataDicari);
        lblHasilCari.setText("Kata \"" + kataDicari + "\" ditemukan " + jumlah + " kali.");
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        simpanTXT();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInputMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInputMouseClicked

    private void txtInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInputFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInputFocusGained

    private void txtInputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInputFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInputFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PenghitungKataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenghitungKataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenghitungKataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenghitungKataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenghitungKataFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblHasilCari;
    private javax.swing.JLabel lblKalimat;
    private javax.swing.JLabel lblKarakter;
    private javax.swing.JLabel lblKata;
    private javax.swing.JLabel lblParagraf;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextArea txtInput;
    // End of variables declaration//GEN-END:variables
}
