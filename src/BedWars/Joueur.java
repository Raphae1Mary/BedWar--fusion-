/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BedWars;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Images : 
 * 
 * @author Alexandre.Vucemilovic
 */
public class Joueur {

    // SKIN
    private int skin;
    protected BufferedImage sprite;
    private Carte carte;
    
    // CARACTERISTIQUE JOUEUR
    protected double x, y;
    private String pseudo;
    private int ptsDeVie;
    private boolean statut;
    private int idJoueur;
    private int equipeJoueur;
    
    // DEPLACEMENT
    private boolean gauche, droite, haut, bas;
    private double gravite;
    private boolean conditionSaut;
    private boolean etatSaut;
    private boolean doubleSaut;
    private int compteur;
    private boolean direction;
    
    
    // BASE DE DONNEES
    protected Connection connection;
    
    
    

    public Joueur(String pseudo, Connection connection,Carte carte, int equipeJoueur) {
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/perso bleu qui court découpé.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.carte = carte;
        this.connection = connection;
        
        this.x = 0;
        this.y = 0;
        
        this.gauche = false;
        this.droite = false;
        this.gravite = 10;
        this.conditionSaut = true;
        this.etatSaut = false;
        this.doubleSaut = true;
        this.compteur = 0;
        
        
        this.pseudo = pseudo;
        this.direction=false;
        this.statut= true;
        this.idJoueur= 0;
        
        int equipe= equipeJoueur;
        this.ptsDeVie = 20;
        int skin=0;
        
        
        
        // IL Y A DES PROBLEMES AVEC L'INITIALISATION, IL Y A 10 FOIS LA METHODE SET() POUR SEULEMENT 7 POINTS D'INTERROGATIONS
        
        
        
        
        
//        try {
//            PreparedStatement requete = connection.prepareStatement("SELECT MAX(id) AS 'max' FROM joueur ");
//            ResultSet resultat = requete.executeQuery();
//            while ( resultat . next () ) {
//                this.idJoueur= resultat.getInt("max")+1;
//            }
//            requete.close();
//            
//            requete = connection.prepareStatement("INSERT INTO joueur VALUES (?,?,?,?,?,?,?)");
//            requete.setInt(1,idJoueur);                 // ID
//            requete.setString(2, pseudo);               // PSEUDO
//            requete.setDouble(3, x);                    // X
//            requete.setDouble(4, y);                    // Y
//            requete.setBoolean(5,direction);            // DIRECTION (TRUE = ? ET FALSE = ?)
//            requete.setBoolean(6,statut);               // STATUT (TRUE = ? ET FALSE = ?)
//            requete.setInt(7, ptsDeVie);                // POINTS DE VIE
//            requete.setInt(8, idJoueur);                // ENCORE ID JOUEUR?
//            requete.setInt(9, equipe);                  // EQUIPE
//            requete.setInt(10, skin);                   // NUMERO SKIN
//
//            requete.executeUpdate();
//
//            requete.close();     
//                
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//       }
    }
    
    
    //TEST
    
    public int getPtsDeVie(){
        return(this.ptsDeVie);
    }
    
    
    
    //SETTER
    
    public void setGauche(boolean gauche) {
        this.gauche = gauche;      
    }

    public void setDroite(boolean droite) {
        this.droite = droite;    
    }

    public void setHaut(boolean haut) {
        this.haut = haut;
    }

    public void setBas(boolean bas) {
        this.bas = bas;
    }
    
    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }
    public String getPseudo(){
        return(this.pseudo);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    
    
    
    //MISE A JOUR
    
    public void miseAJour() {
        
        y += gravite;
        
        //Déplacement horizontaux
        if (this.gauche && !collisionDecorGauche()) {
            x -= 10;
        }
        if (this.droite && !collisionDecorDroite()) {
            x += 10;
        }
        if (x > 1920) {
            x = 1920;
        }
        if (x < 0) {
            x = 0;
        }
        
        //Déplacement verticaux
        if (this.haut) {
            if(collisionDecorBas()){
                conditionSaut = true;
                doubleSaut = true;
            }
            
            if(doubleSaut && !conditionSaut){
                conditionSaut = true;
                doubleSaut = false;
                compteur = 0;
            }
            
            
        }
        if (this.bas) {
            y += 10;
            etatSaut = false;
            conditionSaut = false;
        }
        if (collisionDecorBas()) {
            y = y-gravite;
        }
        if (y < 0) {
            y = 0;
        }    
    }
    
    
    public void saut(){
        
        if(conditionSaut){
            etatSaut = true;
        }    
            
        if (etatSaut){     
            compteur++;
            if(compteur < 5){
                y -= 30;
            } else {
                compteur = 0;
                etatSaut = false;
                conditionSaut = false;
            }
        }
        
    }
    
    public boolean collisionDecorBas(){
        int[][] tableau = carte.getMap();
        int xmin,xmax,ymin,ymax,i,j;
        int hauteur_perso=32;
        int largueur_perso=32;
        xmin=(int) this.x/32;
        ymin=(int) this.y/32;
        xmax=(int) (this.x+32-1)/32;
        ymax=(int) (this.y+32-1)/32;
        for(i=xmin;i<=xmax;i++) 
    { 
        for(j=ymax;j<=ymax;j++) 
        {  
            if (tableau[j][i]==0 || tableau[j][i]==2 || tableau[j][i]==3 || tableau[j][i]==1){
                return true; 
        }    
        }}
        return false;
    }    
    
        public boolean collisionDecorDroite(){
        int[][] tableau = carte.getMap();
        int xmin,xmax,ymin,ymax,i,j;
        int hauteur_perso=32;
        int largueur_perso=32;
        xmin=(int) this.x/32;
        ymin=(int) this.y/32;
        xmax=(int) (this.x+32-1)/32;
        ymax=(int) (this.y+32-1)/32;
        for(i=xmax;i<=xmax;i++) 
    { 
        for(j=ymin;j<=ymin;j++) 
        {  
            if (tableau[j][i]==0 || tableau[j][i]==2 || tableau[j][i]==3 || tableau[j][i]==1){
                return true; 
        }    
        }}
        return false;
    } 
        
        public boolean collisionDecorGauche(){
        int[][] tableau = carte.getMap();
        int xmin,xmax,ymin,ymax,i,j;
        int hauteur_perso=32;
        int largueur_perso=32;
        xmin=(int) this.x/32;
        ymin=(int) this.y/32;
        xmax=(int) (this.x+32-1)/32;
        ymax=(int) (this.y+32-1)/32;
        for(i=xmin;i<=xmin;i++) 
    { 
        for(j=ymin;j<=ymin;j++) 
        {  
            if (tableau[j][i]==0 || tableau[j][i]==2 || tableau[j][i]==3 || tableau[j][i]==1){
                return true; 
        }    
        }}
        return false;
    } 
    //RENDU
    
    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.sprite, (int) x, (int) y, null);
    }
    
    
    //DEBUT METHODES
    
    public void attaque( int idVictime){
        int objetMain= this.getObjetMain(this.idJoueur);
        int degatArme = this.getDegatsEquipement(objetMain);
        int pvVictime= this.getPVJoueur(idVictime);
        if(pvVictime > degatArme){
            pvVictime= pvVictime - degatArme;
            this.setPVJoueur(idVictime, pvVictime);
        }
        else if(pvVictime<= degatArme){
            pvVictime=0;
            this.setPVJoueur(idVictime, pvVictime);
            this.setStatutJoueur(idVictime, false);
        }
    }
    
    
    // DEBUT DES REQUETES SQL POUR LES CARACTERISTIQUES JOUEURS (LOCALISATION, POINTS DE VIE, ...)
    
    //LOCALISATION (GETTER ET SETTER)
    
    
      
    public double getXJoueur(int idJoueur){
        double latitude=0;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT latitude FROM joueur WHERE Id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                latitude = resultat.getDouble("latitude");
            }
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return latitude;
    }
    
    
    public double getYJoueur(int idJoueur){
        double longitude=0;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT longitude FROM joueur WHERE Id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                longitude = resultat.getDouble("longitude");
            }
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return longitude;
    }
    
    
    public void setLocalisationJoueur( int idJoueur, double x, double y){
        try {

            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET latitude = ?, longitude = ? WHERE Id = ?");
            requete.setDouble(1, x);
            requete.setDouble(2, y);
            requete.setInt(3, idJoueur);
            requete.executeUpdate();

            requete.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    
    //PV (GETTER ET SETTER)
    public int getPVJoueur(int idJoueur){
        int pointDeVie=-1;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT pv FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                pointDeVie = resultat.getInt("pv");
            }
            requete.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pointDeVie;
    }
    
    
    public void setPVJoueur(int idJoueur, int pointDeVie){
        try {
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET pv = ? WHERE id = ?");
            requete.setInt(1, pointDeVie);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    //DIRECTION (GETTER ET SETTER)
    public boolean getDirectionJoueur(int idJoueur){
        boolean direction= false;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT Direction FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                direction = resultat.getBoolean("Direction");
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return direction;
    }
    
    
    public void setDirectionJoueur(int idJoueur, boolean direction){
        
        try {
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET Direction = ? WHERE id = ?");
            requete.setBoolean(1, direction);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }  
  
    
    //DIRECTION (GETTER ET SETTER)
    
    public boolean getStatutJoueur(int idJoueur){
        boolean statut= true;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT statut FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                statut = resultat.getBoolean("statut");
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return statut;
    }
    
    
    public void setStatutJoueur(int idJoueur ,boolean statut){
        try {
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET statut = ? WHERE id = ?");
            requete.setBoolean(1, statut);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    
    //EQUIPE (GETTER ET SETTER)
    public int getEquipeJoueur(int idJoueur){
        int equipe=-1;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT equipe FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                equipe = resultat.getInt("equipe");
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return equipe;
    }
    
    
    public void setEquipeJoueur(int idJoueur, int equipe){
        try {
            // CHANGER L'EQUIPE D'UN JOUEUR
            
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET equipe = ? WHERE id = ?");
            requete.setInt(1, equipe);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    
     //SKIN(GETTER ET SETTER)
    public int getSkinJoueur(int idJoueur){
        int skin = -1;
        try {
            // OBTENIR LE SKIN D'UN JOUEUR
            PreparedStatement requete = connection.prepareStatement("SELECT skin FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                skin = resultat.getInt("skin");
                
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return skin;
    }
    
    
    public void setSkinJoueur(int idJoueur, int skin){
        try {
            // CHANGER LE SKIN DU JOUEUR
            
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET skin = ? WHERE id = ?");
            requete.setInt(1, skin);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    // OBJET 
    public void setObjetMain(int idJoueur, int idEquipement){
        try {
            // CHANGER L'OBJET DANS LA MAIN
            
            PreparedStatement requete = connection.prepareStatement("UPDATE joueur SET objetMain = ? WHERE id = ?");
            requete.setInt(1, idEquipement);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    
    public int getObjetMain(int idJoueur){
        int objetMain=-1;
        try {
            // CONNAIT L'OBJET DANS LA MAIN
            

            PreparedStatement requete = connection.prepareStatement("SELECT objetMain FROM joueur WHERE id = ?;");
            requete.setInt(1, idJoueur);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                objetMain = resultat.getInt("objetMain");
                
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return objetMain;
    }
    
    
    
    //ID EQUIPE
    public void setIdEquipe(int idJoueur, int equipe){
        try {
            
            
            PreparedStatement requete = connection.prepareStatement("UPDATE equipe SET idEquipe = ? WHERE id = ?");
            requete.setInt(1, equipe);
            requete.setInt(2, idJoueur);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    //DEGATS EQUIPEMENT
    public int getDegatsEquipement(int idEquipement){
        int degat=-1;
        try {
            // OBTIENT LES DEGATS D'UN OBJET DE L'INVENTAIRE
            PreparedStatement requete = connection.prepareStatement("SELECT degats FROM inventaire WHERE idEquipement = ?;");
            requete.setInt(1, idEquipement);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                degat = resultat.getInt("degat");
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return degat;
    }
    
    
    public void setDegatsEquipement(int idEquipement, int degat){
        try {
            // CHANGER LES DEGATS D'UN OBJET DE L'INVENTAIRE
            
            PreparedStatement requete = connection.prepareStatement("UPDATE inventaire SET degat = ? WHERE idEquipement = ?");
            requete.setInt(1,degat );
            requete.setInt(2,idEquipement);
            requete.executeUpdate();

            requete.close();
            
                    

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
  
    //DEPOT ITEM
    public void setDepot(int idEquipement, boolean depot){
        try {
            // DEFINI SI L'OBJET EST DEPOSABLE OU NON
            
            PreparedStatement requete = connection.prepareStatement("UPDATE inventaire SET depot = ? WHERE idEquipement = ?");
            requete.setBoolean(1,depot );
            requete.setInt(2,idEquipement);
            requete.executeUpdate();

            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public boolean getDepot(int idEquipement){
        boolean depot= true;
        try {
            // DEFINI SI L'OBJET EST DEPOSABLE OU NON 
            PreparedStatement requete = connection.prepareStatement("SELECT depot FROM inventaire WHERE idEquipement = ?;");
            requete.setInt(1, idEquipement);
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                depot = resultat.getBoolean("depot");
            }
            requete.close();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return depot;
    }
    
    

 
    
    
    
}