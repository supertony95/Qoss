/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_DV;
import base_simulator.DV.NetworkInterface;
import base_simulator.DV.InfosDV;
import base_simulator.DV.Applicazione;
import base_simulator.DV.Grafo;
import base_simulator.DV.canale;
import base_simulator.layers.DV.LinkLayer;
import base_simulator.layers.DV.TransportLayer;
import base_simulator.layers.DV.physicalLayer;
import base_simulator.DV.link_extended;
import base_simulator.DV.schedulerDV;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import org.jdom2.Document;

import org.jdom2.Element;

import org.jdom2.JDOMException;

import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author domen
 */
public class main_app_modified extends javax.swing.JFrame {

    /**
     * Creates new form main_app_modified
     */
    public main_app_modified() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Principale = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        testa_button = new javax.swing.JButton();
        crea_rete_button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        crea_rete = new javax.swing.JPanel();
        back_crea_rete = new javax.swing.JButton();
        Nodo_router = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        Canale = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        idL = new javax.swing.JLabel();
        capacitaL = new javax.swing.JLabel();
        capacita = new javax.swing.JTextField();
        DimPacchettoL = new javax.swing.JLabel();
        DimPacchetto = new javax.swing.JTextField();
        T_propagazioneL = new javax.swing.JLabel();
        Tpropagazione = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Progetto Telecomunicazione\n");
        setName("Progetto"); // NOI18N

        Principale.setLayout(new java.awt.CardLayout());

        Menu.setBackground(new java.awt.Color(51, 177, 31));

        testa_button.setBackground(new java.awt.Color(255, 255, 255));
        testa_button.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        testa_button.setText("Testa");
        testa_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testa_buttonActionPerformed(evt);
            }
        });

        crea_rete_button.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        crea_rete_button.setText("Crea rete");
        crea_rete_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crea_rete_buttonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("       Progetto Reti di Telecomunicazione");

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(163, 163, 163))
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addComponent(crea_rete_button, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testa_button, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112))))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(crea_rete_button, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(testa_button, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(202, 202, 202))
        );

        Principale.add(Menu, "menu");

        crea_rete.setBackground(new java.awt.Color(51, 177, 31));

        back_crea_rete.setText("Back");
        back_crea_rete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back_crea_reteActionPerformed(evt);
            }
        });

        Nodo_router.setBackground(new java.awt.Color(0, 0, 0));
        Nodo_router.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        Nodo_router.setForeground(new java.awt.Color(255, 255, 255));
        Nodo_router.setText("Nodo Router :");

        jTextField1.setText("jTextField1");

        Canale.setBackground(new java.awt.Color(0, 0, 0));
        Canale.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        Canale.setForeground(new java.awt.Color(255, 255, 255));
        Canale.setText("Canale:");

        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        idL.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        idL.setForeground(new java.awt.Color(255, 255, 255));
        idL.setText("ID:");

        capacitaL.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        capacitaL.setForeground(new java.awt.Color(255, 255, 255));
        capacitaL.setText("Capacita:");

        capacita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capacitaActionPerformed(evt);
            }
        });

        DimPacchettoL.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        DimPacchettoL.setForeground(new java.awt.Color(255, 255, 255));
        DimPacchettoL.setText("Dim.Pacchetto:");

        DimPacchetto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DimPacchettoActionPerformed(evt);
            }
        });

        T_propagazioneL.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        T_propagazioneL.setForeground(new java.awt.Color(255, 255, 255));
        T_propagazioneL.setText("T.propagazione:");

        Tpropagazione.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TpropagazioneActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton2.setText("Aggiungi Canale");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout crea_reteLayout = new javax.swing.GroupLayout(crea_rete);
        crea_rete.setLayout(crea_reteLayout);
        crea_reteLayout.setHorizontalGroup(
            crea_reteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crea_reteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(crea_reteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(crea_reteLayout.createSequentialGroup()
                        .addComponent(idL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(capacitaL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(capacita, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DimPacchettoL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DimPacchetto, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(T_propagazioneL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Tpropagazione, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addComponent(back_crea_rete)
                    .addComponent(Nodo_router)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Canale))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        crea_reteLayout.setVerticalGroup(
            crea_reteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crea_reteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(back_crea_rete)
                .addGap(18, 18, 18)
                .addComponent(Nodo_router, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Canale, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(crea_reteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idL)
                    .addComponent(capacitaL)
                    .addComponent(capacita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DimPacchettoL)
                    .addComponent(DimPacchetto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T_propagazioneL)
                    .addComponent(Tpropagazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(315, Short.MAX_VALUE))
        );

        Principale.add(crea_rete, "crea_rete");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(844, 566));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void crea_rete_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crea_rete_buttonActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) Principale.getLayout();
         cl.show(Principale, "crea_rete");

    }//GEN-LAST:event_crea_rete_buttonActionPerformed

    private void testa_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testa_buttonActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) Principale.getLayout();
        cl.show(Principale, "menu");
    }//GEN-LAST:event_testa_buttonActionPerformed

    private void DimPacchettoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DimPacchettoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DimPacchettoActionPerformed

    private void capacitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capacitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_capacitaActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void back_crea_reteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back_crea_reteActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) Principale.getLayout();
        cl.show(Principale, "menu");
    }//GEN-LAST:event_back_crea_reteActionPerformed

    private void TpropagazioneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TpropagazioneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TpropagazioneActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(capacita.getText()==""||DimPacchetto.getText()==""||Tpropagazione.getText()==null){
            JOptionPane.showMessageDialog(this, "Inserimento non valido", "Errore!", HEIGHT);
        }
        else{
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File("C:\\Users\\domen\\Desktop\\Simulatore_con_tabelleDelleDistanze\\simulatorepa_real\\src\\reti_tlc_gruppo_0\\conf.xml"));
            Element root = document.getRootElement();
            List children = root.getChildren();
            Iterator iterator = children.iterator();
            while(iterator.hasNext()){ 
            Element item = (Element)iterator.next();
            if(item.getName()=="canali"){
                Element child=new Element("canale");
                child.setAttribute("id","7");
                child.setAttribute("tipo","WIRED");
                child.setAttribute("capacita",capacita.getText());
                child.setAttribute("dim_pacchetto",DimPacchetto.getText());
                child.setAttribute("tempo_propagazione",Tpropagazione.getText());
                item.addContent(child);
                
                 //Creazione dell'oggetto XMLOutputter
                 XMLOutputter outputter = new XMLOutputter();
                //Imposto il formato dell'outputter come "bel formato"
                   outputter.setFormat(Format.getPrettyFormat());
                //Produco l'output sul file xml.foo
                outputter.output(document, new FileOutputStream("C:\\Users\\domen\\Desktop\\Simulatore_con_tabelleDelleDistanze\\simulatorepa_real\\src\\reti_tlc_gruppo_0\\conf.xml"));
                System.out.println("File creato:");
                //Stampo l'output anche sullo standard output
                outputter.output(document, System.out);
    } 
            
            }
        } catch (JDOMException ex) {
            Logger.getLogger(main_app_modified.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main_app_modified.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(main_app_modified.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_app_modified.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_app_modified.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_app_modified.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_app_modified().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Canale;
    private javax.swing.JTextField DimPacchetto;
    private javax.swing.JLabel DimPacchettoL;
    private javax.swing.JPanel Menu;
    private javax.swing.JLabel Nodo_router;
    private javax.swing.JPanel Principale;
    private javax.swing.JLabel T_propagazioneL;
    private javax.swing.JTextField Tpropagazione;
    private javax.swing.JButton back_crea_rete;
    private javax.swing.JTextField capacita;
    private javax.swing.JLabel capacitaL;
    private javax.swing.JPanel crea_rete;
    private javax.swing.JButton crea_rete_button;
    private javax.swing.JTextField id;
    private javax.swing.JLabel idL;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton testa_button;
    // End of variables declaration//GEN-END:variables
}