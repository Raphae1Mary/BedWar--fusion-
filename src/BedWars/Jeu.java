/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BedWars;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Exemple de classe jeu
 *
 * @author Alexandre.Vucemilovic
 */
public class Jeu {

    private BufferedImage decor;
    public Joueur joueur;
    private Carte carte;
    protected Connection connection;

    public Jeu(String pseudo, int equipe) throws SQLException {
        
//        this.connection = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20202021_s2_vs1_tp1_harrypotter?serverTimezone=UTC", "harry", "XtCQD");
        
        this.carte = new Carte();

        this.joueur = new Joueur(pseudo, connection, carte, equipe);
    }

    public void miseAJour() {
        this.joueur.miseAJour();
        this.joueur.saut();
    }

    public void rendu(Graphics2D contexte) {
        this.carte.rendu(contexte);
        this.joueur.rendu(contexte);
    }

}
