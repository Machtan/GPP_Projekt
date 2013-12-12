package classes;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import com.apple.eawt.Application;


/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public class MainMenu extends javax.swing.JFrame {
    
    /**
     * Creates new form MainMenu
     */ 
    public MainMenu() {
        super();
        try {
            URL url = MainMenu.class.getClassLoader().getResource("images/Plane.png");
            Image img = Toolkit.getDefaultToolkit().createImage(url);
            Application application = Application.getApplication();
            application.setDockIconImage(img);
        } catch (Exception ex) {
            System.out.println("No Mac OSX icon this time :(");
        }
        Utils.setFrameIcon(this, "images/Plane.png");
        
        initComponents();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        this.setTitle("SUPERFLITE 9001"); // Purrfect
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        showFlightBrowserButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Tilføj ny reservation");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewReservationAction(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setText("Hovedmenu");

        showFlightBrowserButton.setText("Se sæder for afgang");
        showFlightBrowserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFlightBrowserButtonActionPerformed(evt);
            }
        });

        jButton2.setText("Redigér reservationer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editReservationAction(evt);
            }
        });

        jButton3.setText("Fjern reservationer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeReservationAction(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(showFlightBrowserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(showFlightBrowserButton)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createNewReservationAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewReservationAction
        ReservationEditor editor = new ReservationEditor();
        editor.setTitle("Tilføj ny Reservation");
        Utils.transition(this, editor);
    }//GEN-LAST:event_createNewReservationAction

    private void showFlightBrowserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFlightBrowserButtonActionPerformed
        FlightInspector menu = new FlightInspector();
        Utils.transition(this, menu);
        
    }//GEN-LAST:event_showFlightBrowserButtonActionPerformed

    private void editReservationAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editReservationAction
        final FlightBrowser win = new FlightBrowser(); //Unbound version :)
        win.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReservationBrowser res = new EditReservationBrowser(win.getChosen());
                Utils.transition(win, res);
            }
        });
        Utils.transition(this, win);
    }//GEN-LAST:event_editReservationAction

    private void removeReservationAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeReservationAction
        final FlightBrowser win = new FlightBrowser(); //Unbound version :)
        win.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReservationBrowser res = new RemoveReservationBrowser(win.getChosen());
                Utils.transition(win, res);
            }
        });
        Utils.transition(this, win);
    }//GEN-LAST:event_removeReservationAction



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton showFlightBrowserButton;
    // End of variables declaration//GEN-END:variables
}
