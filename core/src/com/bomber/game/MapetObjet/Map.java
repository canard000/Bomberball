
package com.bomber.game.MapetObjet;


import com.badlogic.gdx.scenes.scene2d.Group;
import com.bomber.game.*;
import com.bomber.game.Bonus.BonusBombe;
import com.bomber.game.Bonus.BonusExplo;
import com.bomber.game.Bonus.BonusMove;
import com.bomber.game.Bonus.BonusPousser;
import com.bomber.game.Ennemis.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Classe Map
 * Groupe de case contenu dans une matrice pour pouvoir voir les voisins
 */
public class Map extends Group {//meme chose map est un group d'acteurs (les cases)
    private Case[][] grille;    //grille de case
    private int x;              //dimensions de la map, typiquement 15x13
    private int y;


    /**
     * Constructeur de la classe Map
     *
     * @return une map 15*13
     */
    public Map() {
        super();
        setName("Map");
        this.setPosition(Bomberball.taillecase * 2.5f, Bomberball.taillecase / 2);
        //la taillecase est defini dans bomberball par rapport à la taille de l'écran

        grille = new Case[15][13];
        x = 15;
        y = 13;
    }


    /**
     * Constructeur de la classe Map
     *
     * @param g un tableau de case
     * @return une map dont la grille est initialisée
     */
    public Map(Case g[][]) {
        super();
        grille = g;
    }


    /**
     * Accesseur du tableau de case
     *
     * @return la grille
     */
    public Case[][] getGrille() {
        return grille;
    }

    /**
     * modificateur de grille
     * @param grille
     */
    public void setGrille(Case[][] grille) {
        this.grille = grille;
    }

    /**
     * accesseur x
     * @return int
     */
    public int tailleX() {
        return x;
    }

    /**
     * Modificateur x
     * @param x
     */
    public void settailleX(int x) {
        this.x = x;
    }

    /**
     * accesseur Y
     * @return int
     */
    public int tailleY() {
        return y;
    }

    /**
     * Modificateur Y
     * @param y
     */
    public void settailleY(int y) {
        this.y = y;
    }




    /**
     * génération de la map PvP de base
     * Renvoie un tableau de case de taille 15x13 avec le pourtour
     * @param nbDestru
     * @param bonus
     * @return générer une map multijoueur
     */
    public static Map generatePvp(int nbDestru, int bonus) {
        int i;            // indice de ligne
        int j;            // indice de colonne
        int cpt = 0;      //compteur de cases potentiellement destructibles, spoiler il y en a 93
        int cpt2 = 0;     //indice allant de 1 à nbDestru
        int random;
        if (nbDestru > 93) {
            nbDestru = 93;
        }
        if (bonus > nbDestru) {
            bonus = nbDestru;
        }


        int id = 0;

        Case[][] g = new Case[15][13];                                                                                                                                      //ce qu'on va renvoyer, le tableau de case
        Case[] casePotDes = new Case[100];                                                                                                                                  //repertorie les cases potentiellement destructibles dans un  tableau
        Case[] caseDes = new Case[200];                                                                                                                                     // réportorie les cases destructibles (après la génération des murs destructibles)

        for (i = 0; i < 15; i++) {                                                                                                                                          //on parcourt toutes les cases de la map
            for (j = 0; j < 13; j++) {
                g[i][j] = new Case();                                                                                                                                       //création d'une nouvelle case à la postion i,j
                g[i][j].setposX(i);
                g[i][j].setposY(j);
                if (i == 0 || i == 14 || j == 0 || j == 12 || (i % 2 == 0 && j % 2 == 0)) {                                                                                 //si la case fait partie des case indestructibles
                    Mur m = new MurI();                                                                                                                                     //on crée un mur indestructible et on le met dans la case
                    g[i][j].setMur(m);
                } else if ((j == 1 && (i == 3 || i == 11)) || (j == 3 && (i == 1 || i == 13)) || (j == 9 && (i == 1 || i == 13)) || (j == 11 && (i == 3 || i == 11))) {
                    Mur m = new MurD();                                                                                                                                     //mise en place des cases destructibles obligatoires autour de zones de départ
                    g[i][j].setMur(m);
                    caseDes[cpt2] = g[i][j];
                    cpt2++;
                } else if (!((j <= 2 && i <= 2) || (j >= 10 && i <= 2) || (j <= 2 && i >= 12) || (j >= 10 && i >= 12))) {
                    casePotDes[cpt] = g[i][j];                                                                                                                              //pour toutes les autres cases sauf celles de la zone de départ
                    cpt++;                                                                                                                                                  //on ajoute la case de coordonnées i,j à la liste des cases potentiellement destru
                }
                if ((i == 1 || i == 13) && (j == 1 || j == 11)) {
                    g[i][j].setPersonnage(new Personnage(true, g[i][j], 2, 1, 5, id));
                    id++;
                }
            }
        }
        int x;
        int y;
        for (i = 0; i < nbDestru; i++) {
            random = (int) (Math.random() * cpt);
            x = casePotDes[random].posX();
            y = casePotDes[random].posY();
            Mur m = new MurD();
            g[x][y].setMur(m);
            casePotDes[random] = casePotDes[cpt - 1];
            cpt--;
            caseDes[cpt2] = g[x][y];
            cpt2++;
        }

        int compteurb = 0;
        int a;
        int b;

        while (compteurb != bonus) {
            a = (int) (Math.random() * cpt2 - 1);
            while (caseDes[a].getBonus() != null) {
                a = (int) (Math.random() * cpt2 - 1);
            }
            b = (int) (Math.random() * 4);
            switch (b) {
                case 0:
                    caseDes[a].setBonus(new BonusBombe(caseDes[a]));
                    break;
                case 2:
                    caseDes[a].setBonus(new BonusExplo(caseDes[a]));
                    break;
                case 3:
                    caseDes[a].setBonus(new BonusMove(caseDes[a]));
                    break;
                case 1:
                    caseDes[a].setBonus(new BonusPousser(caseDes[a]));
                    break;

            }
            compteurb++;
        }

        Map m = new Map();
        m.settailleX(15);
        m.settailleY(13);
        m.setGrille(g);
        for (i = 0; i < m.tailleX(); i++) {
            for (j = 0; j < m.tailleY(); j++) {
                m.getGrille()[i][j].setName("Case" + i + j);
                m.addActor(m.getGrille()[i][j]);
                m.getGrille()[i][j].setMap(m);

            }

        }
        return m;
    }





    /**
     *  1 : indestructible
     *  2 : entree/sortie
     *  0 : libre
     *
     * Vérifie qu'un tableau passé en paramètre avec la convention ci-dessus est valide pour représenter une map
     * C'est-à-dire qu'il existe un chemin entre l'entrée et la sortie
     *
     * @param t
     * @return true si la map a un chemin entre l'entrée et la sortie, false sinon
     */
    public boolean verifSolo(int t[][]) {
        int lignes = t.length;
        int colonnes = t[0].length;
        int xd = -1, yd = -1, xa = -1, ya = -1;
        int i = 0;
        int j = 0;
        int compteur = 0;
        //On cherche le départ et l'arrivée
        for (i = 0; i < lignes; i++) {
            for (j = 0; j < colonnes; j++) {
                if (t[i][j] == 2) {
                    if (xd == -1) {
                        xd = i;
                        yd = j;
                    } else {
                        xa = i;
                        ya = j;
                    }
                }

            }


        }

        int tmp[][] = new int[lignes * colonnes][lignes * colonnes];                //tmp indique s'il existe un chemin entre deux sommets
        int exist[][] = new int[lignes * colonnes][lignes * colonnes];              //On prépare la matrice d'existence de lien (numérotée dans le sens de la gauche vers la droite et on retourne à chaque ligne) Ainsi t[i,j]=j+11*i
        for (i = 0; i < lignes; i++) {
            for (j = 0; j < colonnes; j++) {
                if (j > 0 && j < colonnes - 1) {
                    if (t[i][j] != 1 && t[i][j - 1] != 1) {
                        exist[j + colonnes * i][j - 1 + colonnes * i] = 1;
                        exist[j - 1 + colonnes * i][j + colonnes * i] = 1;
                    }
                    if (t[i][j] != 1 && t[i][j + 1] != -1) {
                        exist[j + colonnes * i][j + 1 + colonnes * i] = 1;
                        exist[j + 1 + colonnes * i][j + colonnes * i] = 1;
                    }
                }
                if (i > 0 && i < lignes - 1) {
                    if (t[i][j] != 1 && t[i - 1][j] != 1) {
                        exist[j + colonnes * i][j + colonnes * (i - 1)] = 1;
                        exist[j + colonnes * (i - 1)][j + colonnes * i] = 1;
                    }
                    if (t[i][j] != 1 && t[i + 1][j] != 1) {
                        exist[j + colonnes * i][j + colonnes * (i + 1)] = 1;
                        exist[j + colonnes * (i + 1)][j + colonnes * i] = 1;
                    }
                }
            }
        }

        //Ici la matrice d'existence est faite.

        int a = yd + colonnes * xd;                                                 //Valeur des sommets dans la matrice d'existence
        int b = ya + colonnes * xa;


        int k;
        for (i = 0; i < colonnes * lignes; i++) {
            for (j = 0; j < colonnes * lignes; j++) {
                tmp[i][j] = exist[i][j];
            }
        }
        for (k = 0; k < colonnes * lignes; k++) {
            for (i = 0; i < colonnes * lignes; i++) {
                for (j = 0; j < colonnes * lignes; j++) {
                    if (tmp[i][k] == 1 && tmp[k][j] == 1) {
                        tmp[i][j] = 1;
                    }
                }
            }
        }

        if (tmp[a][b] == 1 || tmp[b][a] == 1) {
            return true;
        } else {
            return false;
        }



    }


    /**
     * Génère une map aléatoire sans s'occuper de sa validité
     *
     * @param nbDestru   nombre de blocs destructibles
     * @param nbInDestru nombre de blocs indestructibles
     * @param bonus      nombre de case bonus<=nombre de blocs destructibles
     * @return une map
     */
    public Map generatePve(int nbDestru, int nbInDestru, int bonus) {
        Case[][] grille = new Case[15][13];
        int x, y, tmp, tmp1;
        x = (int) (Math.random() * 15);
        y = (int) (Math.random() * 13);


        if (nbDestru > 89) {
            nbDestru = 89;
        }
        if (nbInDestru > 40) {
            nbInDestru = 40;
        }
        if (bonus > nbDestru) {
            bonus = nbDestru-1;
        }
        Case dest[] = new Case[nbDestru];                               //Stocke les cases destructibles sur lesquelles on peut placer des bonus
        int a = 0;
        tmp = nbDestru;
        tmp1 = nbInDestru;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                grille[i][j] = new Case();
                grille[i][j].setposX(i);
                grille[i][j].setposY(j);
                if (j == 0 || i == 0 || j == 12 || i == 14) {
                    grille[i][j].setMur(new MurI());
                }
            }
        }
        while (tmp > 0) {
            while (grille[x][y].getMur() != null && tmp > 0) {          //Choix aléatoire du placement des murs
                x = (int) (Math.random() * 15);
                y = (int) (Math.random() * 13);
            }
            if (tmp > 0) {
                grille[x][y].setMur(new MurD());
                dest[a] = grille[x][y];
                a++;
                tmp--;
            }
        }
        while (tmp1 > 0) {
            while (grille[x][y].getMur() != null && tmp1 > 0) {
                x = (int) (Math.random() * 15);
                y = (int) (Math.random() * 13);
            }
            if (tmp1 > 0) {
                grille[x][y].setMur(new MurI());
                tmp1--;
            }
        }

        int cpt = 2;
        while (cpt > 0) {
            x = (int) (Math.random() * 15);
            y = (int) (Math.random() * 13);
            if (grille[x][y].getMur() == null && cpt == 2) {
                grille[x][y].setPorte(new Porte());
                cpt--;
            }
            if (grille[x][y].getMur() == null && cpt == 1 && grille[x][y].getPorte() == null) {
                grille[x][y].setPersonnage(new Personnage(true, grille[x][y], 2, 1, 5, 0));
                cpt--;
            }
        }

        int compteurb = 0;
        while (compteurb != bonus) {
            x = (int) (Math.random() * dest.length - 1);
            while (dest[x].getBonus() != null) {
                x = (int) (Math.random() * dest.length - 1);
            }
            y = (int) (Math.random() * 4);
            switch (y) {
                case 0:
                    dest[x].setBonus(new BonusBombe(dest[x]));
                    break;
                case 1:
                    dest[x].setBonus(new BonusExplo(dest[x]));
                    break;
                case 2:
                    dest[x].setBonus(new BonusMove(dest[x]));
                    break;
                case 3:
                    dest[x].setBonus(new BonusPousser(dest[x]));
                    break;

            }
            compteurb++;
        }
        Map m = new Map();
        m.setGrille(grille);
        int i;
        int j;
        for (i = 0; i < m.tailleX(); i++) {
            for (j = 0; j < m.tailleY(); j++) {
                m.getGrille()[i][j].setName("Case" + i + j);
                m.addActor(m.getGrille()[i][j]);
                m.getGrille()[i][j].setMap(m);
                m.getGrille()[i][j].setMap(m);

            }

        }

        return m;
    }

    /**
     * Méthode ajouterEnnemis
     * Pour ajouter des ennemis à une map déjà créée
     * @param nombre        nombre d'ennemis
     * @param difficulte    niveau de difficulté
     */
    public void ajouterEnnemis(int nombre ,int difficulte) {
        ArrayList<Case> tab = new ArrayList<Case>();
        for (int i = 1; i < grille.length - 1; i++) {
            for (int j = 1; j < grille[1].length - 1; j++) {
                if (grille[i][j].estVide()) {
                    tab.add(getGrille()[i][j]);
                }
            }
        }
        if (nombre == 0) {
            switch (difficulte) {
                case 1:
                    nombre = 2 + (int) (Math.random() * 2);
                    break;
                case 2:
                    nombre = 4 + (int) (Math.random() * 3);
                    break;
                case 3:
                    nombre = 7 + (int) (Math.random() * 6);
                    break;
            }
        }
        for (int k = 0; k < nombre; k++) {
            boolean ajoute = false;
            ArrayList<Case> tabtemp = new ArrayList<Case>();
            tabtemp.addAll(tab);
            if (Math.random() < 0.3) {                                                      //ajout fantôme
                while (!tabtemp.isEmpty() && !ajoute) {
                    Case potentiel = tabtemp.remove((int) (Math.random() * tabtemp.size()));
                    if (grille[potentiel.posX()][potentiel.posY()].estVide() && (getGrille()[potentiel.posX() - 1][potentiel.posY()].estVide() || getGrille()[potentiel.posX()][potentiel.posY() - 1].estVide() ||
                            getGrille()[potentiel.posX() + 1][potentiel.posY()].estVide() || getGrille()[potentiel.posX()][potentiel.posY() - 1].estVide())) {
                        if (getGrille()[potentiel.posX() - 1][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX() - 1][potentiel.posY()].getPersonnage() == null && getGrille()[potentiel.posX() - 1][potentiel.posY() + 1].getPersonnage() == null
                                && getGrille()[potentiel.posX()][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY()].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY() + 1].getPersonnage() == null
                                && getGrille()[potentiel.posX() + 1][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY() + 1].getPersonnage() == null && getGrille()[potentiel.posX() + 1][potentiel.posY() + 1].getPersonnage() == null) {
                            Ennemis en;
                            if (Math.random() < 0.25 * difficulte) {
                                en = new EnnemiPassifAgressif(true, potentiel, (int) (2 + (difficulte - 1) * 1.5), ((difficulte == 3) ? 10 : 1 + difficulte * 2), false);
                                potentiel.setEnnemi(en);
                                potentiel.addActor(en);
                            } else {
                                en = new EnnemiPassif(true, potentiel, (int) (2 + (difficulte - 1) * 1.5));
                                potentiel.setEnnemi(en);
                                potentiel.addActor(en);
                            }
                            en.getChemin().add(potentiel);
                            if (grille[potentiel.posX()][potentiel.posY()].estVide()) {
                                int a = potentiel.posX() - 1;
                                while (a > 0 && grille[a][potentiel.posY()].getMur() == null) {
                                    en.getChemin().add(getGrille()[a][potentiel.posY()]);
                                    a--;
                                }
                            } else if (getGrille()[potentiel.posX() + 1][potentiel.posY()].estVide()) {
                                int a = potentiel.posX() + 1;
                                while (a < 14 && grille[a][potentiel.posY()].getMur() == null) {
                                    en.getChemin().add(getGrille()[a][potentiel.posY()]);
                                    a++;
                                }
                            } else if (getGrille()[potentiel.posX()][potentiel.posY() - 1].estVide()) {
                                int a = potentiel.posY() - 1;
                                while (a > 0 && grille[potentiel.posX()][a].getMur() == null) {
                                    en.getChemin().add(getGrille()[potentiel.posX()][a]);
                                    a--;
                                }
                            } else if (getGrille()[potentiel.posX()][potentiel.posY() + 1].estVide()) {
                                int a = potentiel.posY() + 1;
                                while (a < 13 && grille[potentiel.posX()][a].getMur() == null) {
                                    en.getChemin().add(getGrille()[potentiel.posX()][a]);
                                    a++;
                                }
                            }
                            ajoute = true;
                            tab.remove(potentiel);
                        }
                    }
                }
            }
            if (!ajoute) {                                                                      //ajout bat
                tabtemp.addAll(tab);
                while (!tabtemp.isEmpty() && !ajoute) {
                    Case potentiel = tabtemp.remove((int) (Math.random() * tabtemp.size()));

                    if (getGrille()[potentiel.posX() - 1][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX() - 1][potentiel.posY()].getPersonnage() == null && getGrille()[potentiel.posX() - 1][potentiel.posY() + 1].getPersonnage() == null
                            && getGrille()[potentiel.posX()][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY()].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY() + 1].getPersonnage() == null
                            && getGrille()[potentiel.posX() + 1][potentiel.posY() - 1].getPersonnage() == null && getGrille()[potentiel.posX()][potentiel.posY() + 1].getPersonnage() == null && getGrille()[potentiel.posX() + 1][potentiel.posY() + 1].getPersonnage() == null) {
                        Ennemis en;
                        if (Math.random() < 0.25 * difficulte) {
                            en = new EnnemiActifAggressif(true, getGrille()[potentiel.posX()][potentiel.posY()], (int) (2 + (difficulte - 1) * 1.5), ((difficulte == 3) ? 10 : 1 + difficulte * 2), false);
                            getGrille()[potentiel.posX()][potentiel.posY()].setEnnemi(en);
                            getGrille()[potentiel.posX()][potentiel.posY()].addActor(en);
                        } else {
                            en = new EnnemiActif(true, getGrille()[potentiel.posX()][potentiel.posY()], (int) (2 + (difficulte - 1) * 1.5));
                            getGrille()[potentiel.posX()][potentiel.posY()].setEnnemi(en);
                            getGrille()[potentiel.posX()][potentiel.posY()].addActor(en);
                        }
                        ajoute = true;
                    }
                }

            }
        }
    }




    /**
     * Méthode réalisant une conversion entre une map et un tableau compréhensible par verifSolo
     * 1 pour un mur indestructible
     * 2 pour une porte
     * 3 pour un personnage
     * 0 pour une case libre
     * @return un tableau d'entier
     */
    int[][] traducteur() {
        int[][] tableau = new int[15][13];
        int i;
        int j;
        for (i = 0; i < 15; i++) {
            for (j = 0; j < 13; j++) {
                if (this.grille[i][j].getMur() instanceof MurI) {
                    tableau[i][j] = 1;
                } else if (this.grille[i][j].getPorte() != null) {
                    tableau[i][j] = 2;
                } else if (this.grille[i][j].getPersonnage() != null) {
                    tableau[i][j] = 2;
                } else {
                    tableau[i][j] = 0;
                }
            }
        }
        return tableau;

    }

    /**Méthode explosion
     * Explose toutes les bombes de la map
     */
    public void explosion() {
        int i;
        int j;
        for (i = 0; i < 15; i++) {
            for (j = 0; j < 13; j++) {
                if (this.getGrille()[i][j].getBombe() != null) {
                    this.getGrille()[i][j].getBombe().explosion();
                }
            }
        }
    }

    /**
     * Méthode générant une map solo aléatoire
     *
     * @param nbDestru   nombre de blocs destructibles
     * @param nbInDestru nombre de blocs indestructibles
     * @param bonus      nombre de bonus
     * @return une map
     */
    public static Map genererMapSolo(int nbDestru, int nbInDestru, int bonus) { //C'est la fonction à appeler pour avoir une map
        Map m = new Map();

        m = m.generatePve(nbDestru, nbInDestru, bonus);
        int t[][] = m.traducteur();
        boolean bool = true;
        while (!m.verifSolo(t) || bool) {                       //Tant que l'on ne trouve pas une carte valide
            m = m.generatePve(nbDestru, nbInDestru, bonus);
            t = m.traducteur();
            int i;
            int j;
            int x = 1;
            int y = 1;
            for (i = 0; i < m.tailleX(); i++) {
                for (j = 0; j < m.tailleY(); j++) {
                    if (m.getGrille()[i][j].getPersonnage() != null) {
                        x = i;
                        y = j;
                        i = 2000;                                               //sortie des for
                        j = 2000;
                    }
                }
            }
            bool = true;
            if (((m.getGrille()[x][y - 1].getMur() == null) && ((m.getGrille()[x - 1][y].getMur() == null) || (m.getGrille()[x + 1][y].getMur() == null))) ||          //On vérifie si le joueur a la place pour poser une bombe
                    ((m.getGrille()[x][y + 1].getMur() == null) && ((m.getGrille()[x - 1][y].getMur() == null) || (m.getGrille()[x + 1][y].getMur() == null)))) {
                bool = false;
            }


        }
        int i;
        int j;
        for (i = 0; i < m.tailleX(); i++) {
            for (j = 0; j < m.tailleY(); j++) {
                m.getGrille()[i][j].setName("Case" + i + j);
                m.getGrille()[i][j].setMap(m);
                m.addActor(m.getGrille()[i][j]);

            }

        }
        return m;
    }


    /**
     * colorie le contour en rouge pour annoncer la reduction
     * @param indiceContour
     */
    public void alertecontour(int indiceContour) {

        for (int i = indiceContour; i <= 14 - indiceContour; i++) {
            grille[i][indiceContour].getBackground().setColor(1,0,0,1);
            grille[i][12-indiceContour].getBackground().setColor(1,0,0,1);

        }
        for (int i = indiceContour + 1; i <= 11 - indiceContour; i++) {
          grille[indiceContour][i].getBackground().setColor(1,0,0,1);
            grille[14-indiceContour][i].getBackground().setColor(1,0,0,1);
        }
    }

    /**
     * remplace les case du contour d'indice indiceContour par des mur indestructible  et renvoie la  liste des joueurs tués
     * @param indiceContour
     * @return  ArrayList<Personnage>
     */
    public ArrayList<Personnage> rapprochementDesMurs(int indiceContour) {
        ArrayList<Personnage> listePersosEcrases = new ArrayList<Personnage>();

        for (int i = indiceContour; i <= 14 - indiceContour; i++) {
            grille[i][indiceContour].setMur(new MurI());
            if (grille[i][indiceContour].getPersonnage() != null) {
                grille[i][indiceContour].getPersonnage().setVivant(false);
                listePersosEcrases.add(grille[i][indiceContour].getPersonnage());
            }

            grille[i][12 - indiceContour].setMur(new MurI());
            if (grille[i][12 - indiceContour].getPersonnage() != null) {
                grille[i][12 - indiceContour].getPersonnage().setVivant(false);
                listePersosEcrases.add(grille[i][12 - indiceContour].getPersonnage());
            }

        }

        for (int i = indiceContour + 1; i <= 11 - indiceContour; i++) {
            grille[indiceContour][i].setMur(new MurI());
            if (grille[indiceContour][i].getPersonnage() != null) {
                grille[indiceContour][i].getPersonnage().setVivant(false);
                listePersosEcrases.add(grille[indiceContour][i].getPersonnage());
            }

            grille[14 - indiceContour][i].setMur(new MurI());
            if (grille[14 - indiceContour][i].getPersonnage() != null) {
                grille[14 - indiceContour][i].getPersonnage().setVivant(false);
                listePersosEcrases.add(grille[14 - indiceContour][i].getPersonnage());
            }

        }

        return listePersosEcrases;
    }

    /**
     * supprime tout les acteurs des cases et de la map
     */
    public void suppActor() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                this.getGrille()[i][j].setPersonnage(null);
                this.getGrille()[i][j].setBombe(null);
                this.getGrille()[i][j].setEnnemi(null);
                this.getGrille()[i][j].setBonus(null);
                this.getGrille()[i][j].setMur(null);
                this.getGrille()[i][j].setMarque(null);
                this.getGrille()[i][j].setPorte(null);
                this.removeActor(this.getGrille()[i][j]);
            }
        }
    }

    /**
     * Transformation d'une map vers un texte avec les conventions suivantes:
     * 0	Case libre (Qu'il y ait un personnage ou un ennemis)
     * 1	MurI
     * 2	MurD
     * 3	MurD + BonusBombe
     * 4	MurD + BonusExplo
     * 5	MurD + BonusMove
     * 6	MurD + BonusPousser
     * 7	Porte
     * A la toute fin, on initialise les ennemis et personnages
     * 1 pour personnage suivi de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm et id (case est formée des coordonnées x et y)
     * 2 pour ennemis passif suivi de vivant, case, pm et une suite de coordonnées de case (x,y) fin par 1010
     * 3 pour ennemiPassifAgressif suivi de vivant,c,pm,portee,agro et une suite de coordonnées de case (x,y) fin par 1010
     * 4 pour ennemiActif suivi de vivant, c,pm
     * 5 pour ennemiActifAgressif suivi vivant, c, pm, portee, agro
     * 9999 Fin du fichier texte
     */
    public String mapToTextN() {
        String s = new String();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                if (this.getGrille()[i][j].getMur() != null) {
                    if (this.getGrille()[i][j].getMur() instanceof MurI) {
                        s = s + "1 ";
                    } else if (this.getGrille()[i][j].getMur() instanceof MurD) {
                        if (this.getGrille()[i][j].getBonus() == null) {
                            s = s + "2 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusBombe) {
                            s = s + "3 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusExplo) {
                            s = s + "4 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusMove) {
                            s = s + "5 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusPousser) {
                            s = s + "6 ";
                        }
                    }


                } else if (this.getGrille()[i][j].getPorte() != null) {
                    s = s + "7 ";
                } else {
                    s = s + "0 ";
                }
            }
            s += "\n";
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                if (this.getGrille()[i][j].getPersonnage() != null) {
                    s = s + "1 " + this.getGrille()[i][j].getPersonnage().isVivant() + " " + this.getGrille()[i][j].getPersonnage().getC().posX() + " " + this.getGrille()[i][j].getPersonnage().getC().posY() + " " + this.getGrille()[i][j].getPersonnage().getTaille() + " " + this.getGrille()[i][j].getPersonnage().getNbBombe() + " " + this.getGrille()[i][j].getPersonnage().getPm() + " " + this.getGrille()[i][j].getPersonnage().getId() + "\n";
                } else if (this.getGrille()[i][j].getEnnemi() != null) {
                    if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiPassif) {
                        s = s + "2 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm();
                        for (Case cas : this.getGrille()[i][j].getEnnemi().getChemin()) {
                            s = s + " " + cas.posX() + " " + cas.posY();
                        }
                        s = s + " 1010\n";
                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiPassifAgressif) {
                        s = s + "3 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm() + " " + this.getGrille()[i][j].getEnnemi().getPortee() + " " + this.getGrille()[i][j].getEnnemi().isAgro() + "\n";
                        for (Case cas : this.getGrille()[i][j].getEnnemi().getChemin()) {
                            s = s + " " + cas.posX() + " " + cas.posY();
                        }
                        s = s + " 1010\n";
                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiActif) {
                        s = s + "4 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm() + "\n";

                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiActifAggressif) {
                        s = s + "5 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm()+" "+ this.getGrille()[i][j].getEnnemi().getPortee() + " " + this.getGrille()[i][j].getEnnemi().isAgro() + "\n";

                    }
                }
            }
        }
        s = s + " 9999";
        return s;
    }

    /**
     * Transformation d'un texte vers une map avec les conventions suivantes:
     * 0	Case libre (Qu'il y ait un personnage ou un ennemis)
     * 1	MurI
     * 2	MurD
     * 3	MurD + BonusBombe
     * 4	MurD + BonusExplo
     * 5	MurD + BonusMove
     * 6	MurD + BonusPousser
     * 7	Porte
     * A la toute fin, on initialise les ennemis et personnages
     * 1 pour personnage suivit de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm et id
     * 2 pour ennemis passif suivi de vivant, case, pm et une suite de coordonnées de case (x,y) fin par 1010
     * 3 pour ennemiPassifAgressif suivi de vivant,c,pm,portee,agro et une suite de coordonnées de case (x,y) fin par 1010
     * 4 pour ennemiActif suivi de vivant, c,pm
     * 5 pour ennemiActifAgressif suivi vivant, c, pm, portee, agro
     */
    public static Map mapFromStringN(String string) {
        Map map = new Map();
        Scanner scan = new Scanner(string);
        Case[][] g = new Case[15][13];
        int choix, a, b;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                choix = scan.nextInt();
                g[i][j] = new Case();
                g[i][j].setposX(i);
                g[i][j].setposY(j);
                g[i][j].setMap(map);
                switch (choix) {
                    case 1:
                        g[i][j].setMur(new MurI());
                        break;
                    case 2:
                        g[i][j].setMur(new MurD());
                        break;
                    case 3:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusBombe(g[i][j]));
                        break;
                    case 4:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusExplo(g[i][j]));
                        break;
                    case 5:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusMove(g[i][j]));
                        break;
                    case 6:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusPousser(g[i][j]));
                        break;
                    case 7:
                        g[i][j].setPorte(new Porte());
                        break;
                }
            }
        }
        choix = scan.nextInt();
        while (choix != 9999) {
            switch (choix) {
                case 1:                                                                                     // personnage suivi de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm et id
                    Boolean vivant = scan.nextBoolean();
                    int xc = scan.nextInt();
                    int yc = scan.nextInt();
                    int taille = scan.nextInt();
                    int nbBombe = scan.nextInt();
                    int pm = scan.nextInt();
                    int id = scan.nextInt();
                    Personnage personnage = new Personnage(vivant, g[xc][yc], taille, nbBombe, pm, id);
                    g[xc][yc].setPersonnage(personnage);
                    break;
                case 2:                                                                                     //ennemis passif suivi de vivant, case, pm et une suite de coordonnées de case (x,y) fin par 1010
                    Boolean vivant1 = scan.nextBoolean();
                    int xc1 = scan.nextInt();
                    int yc1 = scan.nextInt();
                    int pm1 = scan.nextInt();
                    EnnemiPassif ennemiPassif = new EnnemiPassif(vivant1, g[xc1][yc1], pm1);
                    g[xc1][yc1].setEnnemi(ennemiPassif);
                    a = scan.nextInt();
                    while (a != 1010) {
                        b = scan.nextInt();
                        ennemiPassif.getChemin().add(g[a][b]);
                        a = scan.nextInt();
                    }
                    break;
                case 3:                                                                                     //ennemiPassifAgressif suivi de vivant,c,pm,portee,agro et une suite de coordonnées de case (x,y) fin par 1010
                    Boolean vivant2 = scan.nextBoolean();
                    int xc2 = scan.nextInt();
                    int yc2 = scan.nextInt();
                    int pm2 = scan.nextInt();
                    int portee = scan.nextInt();
                    Boolean aggro = scan.nextBoolean();
                    EnnemiPassifAgressif ennemiPassifAgressif = new EnnemiPassifAgressif(vivant2, g[xc2][yc2], pm2, portee, aggro);
                    g[xc2][yc2].setEnnemi(ennemiPassifAgressif);
                    a = scan.nextInt();
                    while (a != 1010) {
                        b = scan.nextInt();
                        ennemiPassifAgressif.getChemin().add(g[a][b]);
                        a = scan.nextInt();
                    }
                    break;
                case 4:                                                                                 //ennemiActif suivi de vivant, c,pm
                    Boolean vivant3 = scan.nextBoolean();
                    int xc3 = scan.nextInt();
                    int yc3 = scan.nextInt();
                    int pm3 = scan.nextInt();
                    EnnemiActif ennemiActif = new EnnemiActif(vivant3, g[xc3][yc3], pm3);
                    g[xc3][yc3].setEnnemi(ennemiActif);
                    break;
                case 5:                                                                                 // ennemiActifAgressif suivi vivant, c, pm, portee, agro
                    Boolean vivant4 = scan.nextBoolean();
                    int xc4 = scan.nextInt();
                    int yc4 = scan.nextInt();
                    int pm4 = scan.nextInt();
                    int portee1 = scan.nextInt();
                    Boolean aggro1 = scan.nextBoolean();
                    EnnemiActifAggressif ennemiActifAggressif = new EnnemiActifAggressif(vivant4, g[xc4][yc4], pm4, portee1, aggro1);
                    g[xc4][yc4].setEnnemi(ennemiActifAggressif);
                    break;
            }
            choix = scan.nextInt();
        }
        map.setGrille(g);
        int i, j;
        for (i = 0; i < map.tailleX(); i++) {
            for (j = 0; j < map.tailleY(); j++) {
                map.getGrille()[i][j].setName("Case" + i + j);
                map.getGrille()[i][j].setMap(map);
                map.addActor(map.getGrille()[i][j]);

            }

        }
        return map;
    }


    /**
     * Transformation d'une map vers un texte avec les conventions suivantes:
     * 0	Case libre (Qu'il y ait un personnage ou un ennemi)
     * 1	MurI
     * 2	MurD
     * 3	MurD + BonusBombe
     * 4	MurD + BonusExplo
     * 5	MurD + BonusMove
     * 6	MurD + BonusPousser
     * 7    Porte
     * 8    BonusBombe
     * 9    BonusExplo
     * 10   BonusMove
     * 11   BonusPousser
     * 12   Bombe qui n'est pas sur un joueur
     * A la toute fin, on initialise les ennemis et personnages
     * 1 pour personnage suivit de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm, id, poussee (case est formée des coordonnées x et y)
     * 2 pour ennemis passif suivi de vivant, case, pm et une suite de coordonnées de case (x,y) fin par 1010
     * 3 pour ennemiPassifAgressif suivi de vivant,c,pm,portee,agro et une suite de coordonnées de case (x,y) fin par 1010
     * 4 pour ennemiActif suivi de vivant, c,pm
     * 5 pour ennemiActifAgressif suivi vivant, c, pm, portee, agro
     * 6 pour Bombe
     * 9999 Fin du fichier texte
     */
    public String mapToTextNP() {
        String s = new String();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                if (this.getGrille()[i][j].getMur() != null) {
                    if (this.getGrille()[i][j].getMur() instanceof MurI) {
                        s = s + "1 ";
                    } else if (this.getGrille()[i][j].getMur() instanceof MurD) {
                        if (this.getGrille()[i][j].getBonus() == null) {
                            s = s + "2 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusBombe) {
                            s = s + "3 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusExplo) {
                            s = s + "4 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusMove) {
                            s = s + "5 ";
                        } else if (this.getGrille()[i][j].getBonus() instanceof BonusPousser) {
                            s = s + "6 ";
                        }
                    }


                } else if (this.getGrille()[i][j].getPorte() != null) {
                    s = s + "7 ";
                }
                else if(this.getGrille()[i][j].getBonus()!=null){
                    if(this.getGrille()[i][j].getBonus() instanceof BonusBombe){
                        s=s+"8 ";
                    }
                    else if(this.getGrille()[i][j].getBonus() instanceof BonusExplo){
                        s=s+"9 ";
                    }
                    else if(this.getGrille()[i][j].getBonus() instanceof BonusMove){
                        s=s+"10 ";
                    }
                    else if(this.getGrille()[i][j].getBonus() instanceof BonusPousser){
                        s=s+"11 ";
                    }
                }
                else if(this.getGrille()[i][j].getBombe()!=null && this.getGrille()[i][j].getPersonnage()==null){
                    s=s+"12 ";
                }
                else {
                    s = s + "0 ";
                }
            }
            s += "\n";
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                if (this.getGrille()[i][j].getPersonnage() != null) {
                    s = s + "1 " + this.getGrille()[i][j].getPersonnage().isVivant() + " " + this.getGrille()[i][j].getPersonnage().getC().posX() + " " + this.getGrille()[i][j].getPersonnage().getC().posY() + " " + this.getGrille()[i][j].getPersonnage().getTaille() + " " + this.getGrille()[i][j].getPersonnage().getNbBombe() + " " + this.getGrille()[i][j].getPersonnage().getPm() + " " + this.getGrille()[i][j].getPersonnage().getId() + " "+this.getGrille()[i][j].getPersonnage().isPoussee()+"\n";
                } else if (this.getGrille()[i][j].getEnnemi() != null) {
                    if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiPassif) {
                        s = s + "2 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm()+" "+((EnnemiPassif) this.getGrille()[i][j].getEnnemi()).pos+" "+((EnnemiPassif) this.getGrille()[i][j].getEnnemi()).retour+"\n";
                        for (Case cas : this.getGrille()[i][j].getEnnemi().getChemin()) {
                            s = s + " " + cas.posX() + " " + cas.posY();
                        }
                        s = s + " 1010\n";
                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiPassifAgressif) {
                        s = s + "3 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm() + " " + this.getGrille()[i][j].getEnnemi().getPortee() + " " + this.getGrille()[i][j].getEnnemi().isAgro() + " "+((EnnemiPassifAgressif) this.getGrille()[i][j].getEnnemi()).pos+" "+((EnnemiPassifAgressif) this.getGrille()[i][j].getEnnemi()).retour+"\n";
                        for (Case cas : this.getGrille()[i][j].getEnnemi().getChemin()) {
                            s = s + " " + cas.posX() + " " + cas.posY();
                        }
                        s = s + " 1010\n";
                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiActif) {
                        s = s + "4 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPm() + "\n";

                    } else if (this.getGrille()[i][j].getEnnemi() instanceof EnnemiActifAggressif) {
                        s = s + "5 " + this.getGrille()[i][j].getEnnemi().isVivant() + " " + this.getGrille()[i][j].getEnnemi().getC().posX() + " " + this.getGrille()[i][j].getEnnemi().getC().posY() + " " + this.getGrille()[i][j].getEnnemi().getPortee() + " " + this.getGrille()[i][j].getEnnemi().isAgro() + "\n";
                    }
                }
            }
        }
        s = s + " 9999 ";
        return s;
    }

    /**
     * Transformation d'un texte vers une map avec les conventions suivantes:
     * 0	Case libre (Qu'il y ait un personnage ou un ennemi)
     * 1	MurI
     * 2	MurD
     * 3	MurD + BonusBombe
     * 4	MurD + BonusExplo
     * 5	MurD + BonusMove
     * 6	MurD + BonusPousser
     * 7	Porte
     * 8    BonusBombe
     * 9    BonusExplo
     * 10   BonusMove
     * 11   BonusPousser
     * 12   Bombe qui n'est pas sur un joueur
     * A la toute fin, on initialise les ennemis et personnages
     *  1 pour personnage suivit de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm, id, poussee (case est formée des coordonnées x et y)
     * 2 pour ennemis passif suivi de vivant, case, pm, pos, retour et une suite de coordonnées de case (x,y) fin par 1010
     * 3 pour ennemiPassifAgressif suivi de vivant,c,pm,portee,agro, pos, retour et une suite de coordonnées de case (x,y) fin par 1010
     * 4 pour ennemiActif suivi de vivant, c,pm
     * 5 pour ennemiActifAgressif suivi vivant, c, pm, portee, agro
     */
    public static Map mapFromStringNP(String string,Jeu jeu) {
        Map map = new Map();
        Scanner scan = new Scanner(string);
        Case[][] g = new Case[15][13];
        LinkedList<Bombe> bombes=new LinkedList<Bombe>();
        LinkedList<Personnage> personnages=new LinkedList<Personnage>();
        int choix, a, b;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 13; j++) {
                choix = scan.nextInt();
                g[i][j] = new Case();
                g[i][j].setposX(i);
                g[i][j].setposY(j);
                g[i][j].setMap(map);
                switch (choix) {
                    case 1:
                        g[i][j].setMur(new MurI());
                        break;
                    case 2:
                        g[i][j].setMur(new MurD());
                        break;
                    case 3:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusBombe(g[i][j]));
                        break;
                    case 4:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusExplo(g[i][j]));
                        break;
                    case 5:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusMove(g[i][j]));
                        break;
                    case 6:
                        g[i][j].setMur(new MurD());
                        g[i][j].setBonus(new BonusPousser(g[i][j]));
                        break;
                    case 7:
                        g[i][j].setPorte(new Porte());
                        break;
                    case 8:
                        g[i][j].setBonus(new BonusBombe(g[i][j]));
                        break;
                    case 9:
                        g[i][j].setBonus(new BonusExplo(g[i][j]));
                        break;
                    case 10:
                        g[i][j].setBonus(new BonusMove(g[i][j]));
                        break;
                    case 11:
                        g[i][j].setBonus(new BonusPousser(g[i][j]));
                        break;
                    case 12:
                        Bombe bo=new Bombe(2,g[i][j]);
                        g[i][j].setBombe(bo);
                        bombes.add(bo);
                        break;
                }
            }
        }
        choix = scan.nextInt();
        while (choix != 9999) {
            switch (choix) {
                case 1:                                                                                                 // personnage suivi de ses paramètres dans l'ordre : vivant, case, taille, nbBombe, pm et id
                    Boolean vivant = scan.nextBoolean();
                    int xc = scan.nextInt();
                    int yc = scan.nextInt();
                    int taille = scan.nextInt();
                    int nbBombe = scan.nextInt();
                    int pm = scan.nextInt();
                    int id = scan.nextInt();
                    Boolean poussee=scan.nextBoolean();
                    Personnage personnage = new Personnage(vivant, g[xc][yc], taille, nbBombe, pm, id);
                    personnage.setPoussee(poussee);
                    personnages.add(personnage);
                    g[xc][yc].setPersonnage(personnage);
                    break;
                case 2:                                                                                                 //ennemis passif suivi de vivant, case, pm, pos, retour et une suite de coordonnées de case (x,y) fin par 1010
                    Boolean vivant1 = scan.nextBoolean();
                    int xc1 = scan.nextInt();
                    int yc1 = scan.nextInt();
                    int pm1 = scan.nextInt();
                    int pos= scan.nextInt();
                    boolean retour=scan.nextBoolean();
                    EnnemiPassif ennemiPassif = new EnnemiPassif(vivant1, g[xc1][yc1], pm1);
                    ennemiPassif.pos=pos;
                    ennemiPassif.retour=retour;
                    g[xc1][yc1].setEnnemi(ennemiPassif);
                    a = scan.nextInt();
                    while (a != 1010) {
                        b = scan.nextInt();
                        ennemiPassif.getChemin().add(g[a][b]);
                        a = scan.nextInt();
                    }
                    break;
                case 3:                                                                                                 //ennemiPassifAgressif suivi de vivant,c,pm,portee,agro,pos,retour et une suite de coordonnées de case (x,y) fin par 1010
                    Boolean vivant2 = scan.nextBoolean();
                    int xc2 = scan.nextInt();
                    int yc2 = scan.nextInt();
                    int pm2 = scan.nextInt();
                    int portee = scan.nextInt();
                    Boolean aggro = scan.nextBoolean();
                    int posi=scan.nextInt();
                    boolean retour1=scan.nextBoolean();
                    EnnemiPassifAgressif ennemiPassifAgressif = new EnnemiPassifAgressif(vivant2, g[xc2][yc2], pm2, portee, aggro);
                    ennemiPassifAgressif.pos=posi;
                    ennemiPassifAgressif.retour=retour1;
                    g[xc2][yc2].setEnnemi(ennemiPassifAgressif);
                    a = scan.nextInt();
                    while (a != 1010) {
                        b = scan.nextInt();
                        ennemiPassifAgressif.getChemin().add(g[a][b]);
                        a = scan.nextInt();
                    }
                    break;
                case 4:                                                                                                 //ennemiActif suivi de vivant, c,pm
                    Boolean vivant3 = scan.nextBoolean();
                    int xc3 = scan.nextInt();
                    int yc3 = scan.nextInt();
                    int pm3 = scan.nextInt();
                    EnnemiActif ennemiActif = new EnnemiActif(vivant3, g[xc3][yc3], pm3);
                    g[xc3][yc3].setEnnemi(ennemiActif);
                    break;
                case 5:                                                                                                 // ennemiActifAgressif suivi vivant, c, pm, portee, agro
                   Boolean vivant4 = scan.nextBoolean();
                    int xc4 = scan.nextInt();
                    int yc4 = scan.nextInt();
                    int pm4 = scan.nextInt();
                    int portee1 = scan.nextInt();
                    Boolean aggro1 = scan.nextBoolean();
                    EnnemiActifAggressif ennemiActifAggressif = new EnnemiActifAggressif(vivant4, g[xc4][yc4], pm4, portee1, aggro1);
                    g[xc4][yc4].setEnnemi(ennemiActifAggressif);
                    break;
            }
            choix = scan.nextInt();
        }
        choix=scan.nextInt();
        while(choix!=111){
            int pmtmp=scan.nextInt();
            int nbtmp=scan.nextInt();
            int bombesurjoueur=scan.nextInt();
            for(Personnage p: personnages){
                if(p.getId()==choix){
                    switch (choix){
                        case 0: jeu.pmtmp1=pmtmp; jeu.nbtmp1=nbtmp; break;
                        case 1: jeu.pmtmp2=pmtmp; jeu.nbtmp2=nbtmp; break;
                        case 2: jeu.pmtmp3=pmtmp; jeu.nbtmp3=nbtmp; break;
                        case 3: jeu.pmtmp4=pmtmp; jeu.nbtmp4=nbtmp; break;
                    }
                    if(bombesurjoueur==1){
                        g[p.getC().posX()][p.getC().posY()].setBombe(new Bombe(p.getTaille(),p.getC()));
                    }
                }
            }
            choix=scan.nextInt();

        }
        choix=scan.nextInt();
        for(Personnage p: personnages){
            if(p.getId()==choix){
                for(Bombe bobo: bombes){
                    g[bobo.getC().posX()][bobo.getC().posY()].setBombe(null);
                    g[bobo.getC().posX()][bobo.getC().posY()].setBombe(new Bombe(p.getTaille(),g[bobo.getC().posX()][bobo.getC().posY()]));
                }
            }

        }

        map.setGrille(g);
        int i, j;
        for (i = 0; i < map.tailleX(); i++) {
            for (j = 0; j < map.tailleY(); j++) {
                map.getGrille()[i][j].setName("Case" + i + j);
                map.getGrille()[i][j].setMap(map);
                map.addActor(map.getGrille()[i][j]);

            }

        }
        return map;
    }
}

