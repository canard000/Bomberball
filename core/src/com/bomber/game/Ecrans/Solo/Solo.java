package com.bomber.game.Ecrans.Solo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bomber.game.Bomberball;
import com.bomber.game.Bonus.Bonus;
import com.bomber.game.Ecrans.Victoire;
import com.bomber.game.Ennemis.Ennemis;
import com.bomber.game.Ecrans.Etat;
import com.bomber.game.Jeu;
import com.bomber.game.MapetObjet.Map;
import com.bomber.game.MapetObjet.Personnage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe Solo
 * Elle affiche l'écran du jeu lors d'un jeu solo
 *
 */

public class Solo extends Etat implements Screen  {
    int pm=5;                                           //Nombre de pm initialisé
    int nb=1;                                           //Nombre de bombe initial
    Image back;                                         //Image de l'arrière-plan

    ArrayList<Ennemis> ennemis=new ArrayList<Ennemis>();
    Image joueur;                                       //Panneau du joueur
    Image bombe;                                        //Affiche le nombre de bombes
    Image mouvement;                                    //Affiche le nombre des mouvements
    Image pousse;                                       //Affiche si le joueur a la poussée
    Image explosion;                                    //Affiche la portée de l'explosion

    Label nbmvt;                                        //Contient le nombre de points de mouvement actuel
    Label nbBombe;                                      //Contient le nombre de bombes actuel
    Label porteExplo;                                   //Contient la valeur de la portée
    Label poussee;                                      //Contient un X si le joueur peut pousser sinon rien

    TextButton retour;                                  //Bouton retour

    Personnage personnage;                              //Stockage du personnage

    Skin skin;                                          //Caractéristiques des éléments graphiques

    Image player;                                       //Affichage de l'image du joueur

    File f;                                             //Fichier qui sert pour la pause ou pour recommencer un niveau
    File frecommencer;                                  //Fichier qui permet de sauvegarder le début d'une partie
    FileWriter fw;                                      //Cela permet d'écrire dans le fichier f
    FileWriter fwr;                                     //Cela permet d'écrire dans le fichier frecommencer

    private Bomberball bombaaaagh;                      //Instance de la classe principale
    /**
     * Constructeur de la classe Solo
     * @param bombaaaagh
     * @param jeu
     */
    public Solo(Bomberball bombaaaagh,Jeu jeu) {
        super(jeu);
        this.bombaaaagh=bombaaaagh;
        f = Gdx.files.internal("./SaveTempo/tmp.txt").file();
        frecommencer =Gdx.files.internal("./SaveTempo/debut.txt").file();
    }

    /**
     * Méthode appelée pour afficher la fenêtre
     */
    @Override
    public void show() {
        /** Fonctionnement similaire à Campagne **/
        Bomberball.stg.addActor(this);
        Bomberball.stg.setKeyboardFocus(this);
        Bomberball.input.addProcessor(this);
        jeu.removeActor(jeu.map);
        skin=new Skin(Gdx.files.internal("uiskin.json"));
        back= new Image(new Texture(Gdx.files.internal("backmain.png")) );
        back.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        if(f.exists()){
            jeu.removeActor(jeu.map);
            this.removeActor(jeu);
            jeu.map=null;
            if(jeu.recommencer){
                jeu.map=Map.mapFromStringN(Bomberball.loadFile(f));
                jeu.recommencer=false;
                for (int i=0;i<15;i++){
                    for(int j=0;j<13;j++){
                        if(jeu.map.getGrille()[i][j].getPersonnage()!=null){
                            pm=jeu.map.getGrille()[i][j].getPersonnage().getPm();
                            nb=jeu.map.getGrille()[i][j].getPersonnage().getNbBombe();
                        }
                    }
                }
                f.delete();
                try {
                    fwr = new FileWriter(frecommencer);
                    fwr.write(jeu.map.mapToTextN());
                    fwr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                jeu.map=Map.mapFromStringNP(Bomberball.loadFile(f),this.jeu);
                f.delete();
                pm=jeu.pmtmp1;                                                          //Remise à jour des valeurs de pm et du nb de bombes restantes
                jeu.pmtmp1=-1;
                nb=jeu.nbtmp1;
                jeu.nbtmp1=-1;
            }

        }
        else if(jeu.map==null){                                                         //Création de map avec des paramètres personnalisés
            if(jeu.nbBonus!=-1 && jeu.nbBlocD!=-1){
                jeu.map=Map.genererMapSolo(jeu.nbBlocD,10, jeu.nbBonus);
                jeu.nbBonus=-1;
                jeu.nbBlocD=-1;
            }
            else if(jeu.nbBonus!=-1 && jeu.nbBlocD==-1){
                jeu.map=Map.genererMapSolo(40,10, jeu.nbBonus);
                jeu.nbBonus=-1;
            }
            else if(jeu.nbBonus==-1 && jeu.nbBlocD!=-1){
                jeu.map=Map.genererMapSolo(jeu.nbBlocD, 10,12);
                jeu.nbBlocD=-1;
            }
            else{
                jeu.map=Map.genererMapSolo(40,10,12);
            }
            if(jeu.nbEnnemis!=-1 && jeu.difficulte!=-1){
                jeu.map.ajouterEnnemis(jeu.nbEnnemis,jeu.difficulte);
                jeu.difficulte=-1;
                jeu.nbEnnemis=-1;

            }
            else if(jeu.nbEnnemis!=-1 ){
                jeu.map.ajouterEnnemis(jeu.nbEnnemis,2);
                jeu.nbEnnemis=-1;

            }
            else if (jeu.difficulte!=-1){
                jeu.map.ajouterEnnemis(0,jeu.difficulte);
                jeu.difficulte=-1;

            }
            else {
                jeu.map.ajouterEnnemis(0,2);
            }

            try {
                fwr = new FileWriter(frecommencer);
                fwr.write(jeu.map.mapToTextN());
                fwr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i=0;i<15;i++){
                for(int j=0;j<13;j++){
                    if(jeu.map.getGrille()[i][j].getPersonnage()!=null){
                        if(jeu.nbBombe!=-1){
                            jeu.map.getGrille()[i][j].getPersonnage().setNbBombe(jeu.nbBombe);
                        }
                        if(jeu.nbDeplaP!=-1){
                            jeu.map.getGrille()[i][j].getPersonnage().setPm(jeu.nbDeplaP);
                        }

                        pm=jeu.map.getGrille()[i][j].getPersonnage().getPm();
                        nb=jeu.map.getGrille()[i][j].getPersonnage().getNbBombe();
                    }
                }
            }

        }
        else{
            try {
                fwr = new FileWriter(frecommencer);
                fwr.write(jeu.map.mapToTextN());
                fwr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i=0;i<15;i++){
            for (int j=0;j<13;j++){
                if(jeu.map.getGrille()[i][j].getBonus()!=null){


                    Bonus b=jeu.map.getGrille()[i][j].getBonus();
                    jeu.map.getGrille()[i][j].setBonus(null);
                    jeu.map.getGrille()[i][j].setBonus(b);
                    jeu.map.getGrille()[i][j].getBonus().setScale(0.5f);
                }
            }
        }
        jeu.map.setPosition(Gdx.graphics.getWidth()-(jeu.map.getGrille().length+2f)*Bomberball.taillecase ,0);
        jeu.map.setScaleY(27f/26f);
        joueur=new Image(new Texture(Gdx.files.internal("Panneau_joueur.png")));
        joueur.setWidth(jeu.map.getX()+2f*Bomberball.taillecase);
        joueur.setHeight(3*Bomberball.taillecase);
        joueur.setPosition(0,Gdx.graphics.getHeight()-3*Bomberball.taillecase);

        personnage=jeu.map.findActor("Personnage");

        if(jeu.porteeBombe!=-1){
            personnage.setTaille(jeu.porteeBombe);
        }
        if(jeu.nbBombe!=-1){
            personnage.setNbBombe(jeu.nbBombe);
        }
        if(jeu.nbDeplaP!=-1){
            personnage.setPm(jeu.nbDeplaP);
        }






        mouvement = new Image(new Texture(Gdx.files.internal("Nombre_mouvement.png")));
        mouvement.setWidth(jeu.map.getX()+2f*Bomberball.taillecase);
        mouvement.setHeight(2*Bomberball.taillecase);
        mouvement.setPosition(0,Gdx.graphics.getHeight()-5*Bomberball.taillecase);

        nbmvt= new Label(""+personnage.getPm(),skin);
        nbmvt.setBounds(Bomberball.taillecase*6+50,Gdx.graphics.getHeight()-4*Bomberball.taillecase-20,nbmvt.getWidth(),nbmvt.getHeight()); //Positionnement à la main

        bombe=new Image(new Texture(Gdx.files.internal("Nombre_bombe.png")));
        bombe.setWidth(jeu.map.getX()+2f*Bomberball.taillecase);
        bombe.setHeight(2*Bomberball.taillecase);
        bombe.setPosition(0,Gdx.graphics.getHeight()- 7*Bomberball.taillecase);

        nbBombe= new Label(""+personnage.getNbBombe(),skin);
        nbBombe.setBounds(Bomberball.taillecase*6+50,Gdx.graphics.getHeight()-6*Bomberball.taillecase-20,nbBombe.getWidth(),nbBombe.getHeight()); //Positionnement à la main

        explosion=new Image(new Texture(Gdx.files.internal("Portée_bombe.png")));
        explosion.setWidth(jeu.map.getX()+2f*Bomberball.taillecase);
        explosion.setHeight(2*Bomberball.taillecase);
        explosion.setPosition(0,Gdx.graphics.getHeight()-9*Bomberball.taillecase);

        porteExplo = new Label(""+personnage.getTaille(),skin);
        porteExplo.setBounds(Bomberball.taillecase*6+50,Gdx.graphics.getHeight()-8*Bomberball.taillecase-20,porteExplo.getWidth(),porteExplo.getHeight()); //Positionnement à la main


        pousse=new Image(new Texture(Gdx.files.internal("icone_Bonus_pousser.png")));
        pousse.setWidth(jeu.map.getX()+2f*Bomberball.taillecase);
        pousse.setHeight(2*Bomberball.taillecase);
        pousse.setPosition(0,Gdx.graphics.getHeight()-11*Bomberball.taillecase);
        if(personnage.isPoussee()){
            poussee = new Label("X",skin);
        }
        else{
            poussee = new Label("",skin);
        }
        poussee.setBounds(Bomberball.taillecase*6+50,Gdx.graphics.getHeight()-10*Bomberball.taillecase-20,poussee.getWidth(),poussee.getHeight());


        player=new Image(Bomberball.multiTexture[4]);
        player.setBounds(6*Bomberball.taillecase,Gdx.graphics.getHeight()-2*Bomberball.taillecase,Bomberball.taillecase,Bomberball.taillecase);

        retour = new TextButton("Retour",skin);
        retour.setBounds(0,0,retour.getWidth(),retour.getHeight());







        retour.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jeu.map.suppActor();
                jeu.removeActor(jeu.map);
                jeu.map=null;
                bombaaaagh.jeuSolo.removeActor(jeu);
                Bomberball.input.removeProcessor(bombaaaagh.jeuSolo);
                frecommencer.delete();

                jeu.nbDeplaP=-1;
                jeu.porteeBombe=-1;
                jeu.nbBombe=-1;


                jeu.setEtat(bombaaaagh.menuSolo);
                bombaaaagh.setScreen(bombaaaagh.menuSolo);
            }
        });

        ennemis.clear();
        for (int i=1;i<jeu.map.getGrille().length-1;i++){
            for (int j=1;j<jeu.map.getGrille()[0].length-1;j++){
                if (jeu.map.getGrille()[i][j].getEnnemi()!=null){
                    ennemis.add(jeu.map.getGrille()[i][j].getEnnemi());
                }
            }
        }


        this.addActor(back);
        this.addActor(joueur);
        this.addActor(bombe);
        this.addActor(mouvement);
        this.addActor(explosion);
        this.addActor(pousse);
        this.addActor(player);
        this.addActor(nbmvt);
        this.addActor(nbBombe);
        this.addActor(porteExplo);
        this.addActor(poussee);
        this.addActor(retour);
        jeu.addActor(jeu.map);
        this.addActor(jeu);
        jeu.pmtmp1=-1;
        jeu.pmtmp2=-1;
        jeu.pmtmp3=-1;
        jeu.pmtmp4=-1;
        jeu.nbtmp1=-1;
        jeu.nbtmp2=-1;
        jeu.nbtmp3=-1;
        jeu.nbtmp4=-1;


    }

    @Override
    /**
     * Effectue une action en fonction de la touche appuyée sur le clavier
     * @param keycode code de la touche appuyée
     * @return boolean
     */
    public boolean keyDown( int keycode) {//déplacement = flèche pas encore implementée
        Personnage joueur = personnage;
        if(jeu.findActor("explo")==null) {

            if (joueur.isVivant() && (!joueur.hasActions())) {
                boolean b ;
                if (keycode == Input.Keys.RIGHT) {
                    if (pm > 0) {
                        b = joueur.deplacerDroite();
                        pm = ((b) ? pm - 1 : pm);
                        this.removeActor(nbmvt);
                        nbmvt.setText(""+pm);
                        this.addActor(nbmvt);

                    }
                }

                if (keycode == Input.Keys.LEFT) {
                    if (pm > 0) {
                        b = joueur.deplacerGauche();
                        pm = ((b) ? pm - 1 : pm);
                        this.removeActor(nbmvt);
                        nbmvt.setText(""+pm);
                        this.addActor(nbmvt);
                    }
                }
                if (keycode == Input.Keys.DOWN) {
                    if (pm > 0) {
                        b = joueur.deplacerBas();
                        pm = ((b) ? pm - 1 : pm);
                        this.removeActor(nbmvt);
                        nbmvt.setText(""+pm);
                        this.addActor(nbmvt);
                    }
                }
                if (keycode == Input.Keys.UP) {
                    if (pm > 0) {
                        b = joueur.deplacerHaut();
                        pm = ((b) ? pm - 1 : pm);
                        this.removeActor(nbmvt);
                        nbmvt.setText(""+pm);
                        this.addActor(nbmvt);
                    }
                }
                if (keycode == Input.Keys.SPACE) {
                    if (nb > 0) {
                        b = joueur.poserBombe();
                        nb = ((b) ? nb - 1 : nb);
                        nbBombe.setText(""+nb);
                    }
                }
                if (keycode == Input.Keys.ENTER) {
                    jeu.map.explosion();
                    porteExplo.setText("" + personnage.getTaille());
                    if (joueur.isPoussee()) {
                        poussee.setText("X");
                    }
                    if (joueur.getC().getPorte() != null) {
                        jeu.removeActor(joueur);
                        jeu.removeActor(jeu.map);
                        this.removeActor(jeu);
                        jeu.map = null;
                        bombaaaagh.victoire = new Victoire(bombaaaagh, jeu, "                           Victoire");
                        jeu.setEtat(bombaaaagh.victoire);
                        bombaaaagh.setScreen(bombaaaagh.victoire);
                        return true;

                    }
                    pm = joueur.getPm();
                    nb = joueur.getNbBombe();
                    nbBombe.setText("" + nb);
                    nbmvt.setText("" + pm);
                    if (joueur.isVivant()) {

                        if(ennemis.size()!=0) {
                            Bomberball.input.removeProcessor(this);
                            tourEnnemi();
                        }

                    }
                    else {
                        for (Ennemis en : ennemis) {
                            if (en.isVivant()) {
                                en.setAnimationdefaite();
                            }
                        }
                        jeu.addAction(new Action() {
                            float time = 0;

                            @Override
                            public boolean act(float delta) {
                                time += delta;
                                if (time > 3) {
                                    jeu.removeActor(jeu.map);
                                    jeu.map = null;
                                    bombaaaagh.defaite = new Defaite(bombaaaagh, jeu);
                                    bombaaaagh.defaite.setEtat(bombaaaagh.jeuSolo);
                                    bombaaaagh.jeuSolo.removeActor(jeu);
                                    jeu.setEtat(bombaaaagh.defaite);
                                    bombaaaagh.setScreen(bombaaaagh.defaite);
                                    return true;
                                }
                                return false;
                            }
                        });
                    }

                }
            }
        }
        if (keycode == Input.Keys.ESCAPE) {
            try {
                fw = new FileWriter(f);
                fw.write(jeu.map.mapToTextNP());
                if(joueur.getC().getBombe()!=null){
                    fw.write(joueur.getId()+" "+pm+" "+nb+" 1\n"); //Le 1 indique qu'une bombe est sur la position du joueur
                }
                else{
                    fw.write(joueur.getId()+" "+pm+" "+nb+" 0\n"); //Le 0 indique qu'il n'y a pas de bombe sur la position du joueur
                }

                fw.write("111 "); //Symbole de fin pour la fin de la mise à jour des personnages
                fw.write(""+joueur.getId());
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            jeu.map.suppActor();
            jeu.removeActor(jeu.map);
            jeu.map=null;
            bombaaaagh.jeuSolo.removeActor(jeu);
            bombaaaagh.menuPause.setEtatAnterieur(this);
            jeu.setEtat(bombaaaagh.menuPause);
            bombaaaagh.setScreen(bombaaaagh.menuPause);
        }


        return true;
    }

    /**
     * fait jouer tous les ennemis
     */
    public void tourEnnemi() {

        this.addAction(new Action() {                                                               //on ajoute une action à solo
            Ennemis en=ennemis.get(0);
            int i=-1;
            float time=0;

            @Override
            public boolean act(float delta) {
                time+=delta;
                if ((time>1.5 ||jeu.findActor("explo")==null)&&en.getActions().size==1) {   //on attend la fin des explosions et/ou que l'ennemis en cours
                    i++;                                                                            //ait fini son action
                    if (i == ennemis.size()) {                                                      //si on a terminé
                        Bomberball.input.addProcessor((Solo) target);                               //on remet les inputs
                        if (!personnage.isVivant()) {

                            for (Ennemis en : ennemis) {
                                if (en.isVivant()) {
                                    en.setAnimationdefaite();                                       //les ennemis gagnent si le joueur est mort
                                }
                            }
                            jeu.addAction(new Action() {
                                float time = 0;

                                @Override
                                public boolean act(float delta) {
                                    time += delta;
                                    if (time > 3) {
                                        jeu.removeActor(jeu.map);
                                        jeu.map = null;
                                        bombaaaagh.defaite = new Defaite(bombaaaagh, jeu);
                                        bombaaaagh.defaite.setEtat(bombaaaagh.jeuSolo);
                                        jeu.setEtat(bombaaaagh.defaite);
                                        bombaaaagh.setScreen(bombaaaagh.defaite);
                                        return true;
                                    }
                                    return false;
                                }
                            });

                        }
                        return true;
                    }
                    en = ennemis.get(i);
                    if (en.isVivant()) {                                                            //si l'ennemis est vivant

                        en.deplacer();                                                              //il se deplace
                    }
                }
                return false;
            }
        });
    }

    /**
     * Met à jour l'affichage
     * @param delta: Intervalle de temps entre deux affichages
     */
    @Override
    public void render(float delta) {

    }

    /**
     * Gère le changement de taille de la fenêtre d'affichage
     *
     * @param width : largeur nouvelle fenêtre
     * @param height : hauteur nouvelle fenêtre
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Fonction nécessaire à l'implémentation de l'écran. On n'utilise pas cette fonctionnalité par la suite.
     */
    @Override
    public void pause() {

    }

    /**
     * Fonction nécessaire à l'implémentation de l'écran. On n'utilise pas cette fonctionnalité par la suite.
     */
    @Override
    public void resume() {

    }

    /**
     * Fonction appelée lors d'un changement d'écran.
     */
    @Override
    public void hide() {
        Bomberball.stg.clear();
        jeu.removeActor(jeu.map);
        Bomberball.input.removeProcessor(this);

    }

    /**
     * Fonction nécessaire à l'implémentation de l'écran. On n'utilise pas cette fonctionnalité par la suite.
     */
    @Override
    public void dispose() {

    }

    /**
     * Fonction détectant un mouvement de la souris. On n'utilise pas cette fonctionnalité par la suite.
     * @param screenX: récupère la position x du pointeur.
     * @param screenY : récupère la position x du pointeur.
     * @return false: on ne traite pas cet appui
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Indique l'action à effectuer lorsqu'on clique avec la souris en fonction de l'élément sur lequel on a cliqué
     * @param screenX abscisse du pointeur sur l'écran
     * @param screenY ordonnée du pointeur sur l'écran
     * @param pointer pointeur de l'événement (jamais utilisé)
     * @param button bouton de la souris appuyé
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
