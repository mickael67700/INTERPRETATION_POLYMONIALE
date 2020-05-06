import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class CInterpolation {
    static final int MAX_POINTS = 10000;
    static final int NON_TROUVE = -1;
    Scanner Entree;
    int nbpts_connus , nbpts_a_interpoler;
    CPoint[] tabpts_connus, tabpts_a_interpoler;

    public CInterpolation() {
        tabpts_connus = new CPoint[MAX_POINTS];
        nbpts_connus = 0;
        tabpts_a_interpoler = new CPoint[MAX_POINTS];
        nbpts_a_interpoler = 0;
        Entree = new Scanner(System.in);
        Entree.useLocale(Locale.FRENCH);
    }
    int chargement_points_connus(){
        String NomFichier;
        double x , y;
        int code_retour = 0;
        System.out.println("Nom du fichier contenant les coordonnées (x,y) connus : ");
        NomFichier = Entree.next();
        int i = 0;
        // Chargement fichier
        try {
            Scanner fpts_connus = new Scanner(new File(NomFichier));
            fpts_connus.useLocale(Locale.FRENCH);
            while (fpts_connus.hasNextDouble()){
                x = fpts_connus.nextDouble();
                y = fpts_connus.nextDouble();
                // Chargement points
                CPoint Point = new CPoint();
                Point.affecte_point(x , y);
                tabpts_connus[i++] = Point;
            }
            nbpts_connus = i;
            fpts_connus.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Fichier %s non trouvé\n" , NomFichier);
            code_retour =NON_TROUVE;
            e.printStackTrace();
        }
        return code_retour;
    }
    void saisie_points_a_interpoler(){
        double x;
        System.out.println("Entrez le nombre de points à interpoler : ");
        nbpts_a_interpoler = Entree.nextInt();
        for (int i = 0; i<nbpts_a_interpoler; i++){
            System.out.printf("abcisse du point x%d : ", i);
            x = Entree.nextDouble();
            CPoint Point = new CPoint();
            Point.affecte_point(x , 0);
            tabpts_a_interpoler[i] = Point;
        }
    }

    void interpolation(){
        int i, j, k;
        double terme;
        CPoint Point_Connu = new CPoint();
        CPoint Point_Connu_i = new CPoint();
        CPoint Point_Connu_j = new CPoint();
        //Allocation dynamique tableau des diffs divisées
        double tab_diff[] = new double[nbpts_connus];

        //Allocation dynamique du tableau des coefficients
        double tab_coef[] = new double[nbpts_connus];

        //Initialisation de la table des diff divisées
        for ( i = 0; i< nbpts_connus; i++ ){
            // La table est initialisée avec les valeurs y0 connus , donc f(x0)
            Point_Connu = tabpts_connus[i];
            tab_diff[i] = Point_Connu.ordonnee();
        }
        System.out.println("---Table des coefficients--- \n");
        /**
         * Calcul des coefficients du polynôme d'interpolation.
         * Le premier coefficient contenu dans la case 0 de table_ceof[]
         * est initialisé par le contenu de la case 0 de table_dif[], soit la valeur de f(x0)
         */
        tab_coef[0] = tab_diff[0];
        System.out.printf("k=%d table_coef[%d]=%f\n",0,0, tab_coef[0]);
        /**
         * Boucle de compteur k calcul les coefficients successifs qui sont rangés dans la table_coef[]
         */
        for (k = 1; k < nbpts_connus; k++){
            /**
             * boucle de compteur i calcule chaque différence divisée de cette itération
             */
            for (i = 0 ; i < nbpts_connus ; i++) {
                j = i + k;
                Point_Connu_j = tabpts_connus[j];
                Point_Connu_i = tabpts_connus[i];
                tab_diff[i] = (tab_diff[i + 1] - tab_diff[i]) / (Point_Connu_j.abcisse() - Point_Connu_i.abcisse());
            }
                /**
                 *Une fois toutes les différences de cette itération calculées, on range le contenu de la case 0 de la table des différence
                 * dans la case k de la table des coefficients
                 */
                tab_coef[k] = tab_diff[0];
                System.out.printf("k=%d  table_coef[%d]=%f\n",k,k,tab_coef[k]);
            }
            /**
             * Calcul polynôme d'interpolation pour les points à interpoler.
             * La boucle k construit chaque termes du polynôme et l'additionne au calcul précédent.
             */
            System.out.println("--- Calcul du polynôme ---\n");
            for(k =0; k < nbpts_a_interpoler; k++){
                CPoint Point_a_Interpoler = new CPoint();
                Point_a_Interpoler = tabpts_a_interpoler[k];
                double abscisse_pt_a_Interpoler = Point_a_Interpoler.abcisse();
                double ordonnee_pt_a_Interpoler = Point_a_Interpoler.ordonnee();
                for (j = 0; j < nbpts_connus; j++){
                    /**
                     * On évalue chaque terme du polynôme, par exemple: pour j=2 :
                     * f[x0,x1,x2]*(z-x0)*(z-x1)
                     * On récupère d'adord le coefficient de ce terme ,
                     * ensuite boucle de calcul (z-x0)*(z-x1)*....
                     */
                    terme = tab_coef[j];
                    for (i = 0 ; i < j; i++){
                        Point_Connu = tabpts_connus[i];
                        double abscisse_pt_connu = Point_Connu.abcisse();
                        /**
                         * On multiplie la valeur actuelle du terme par (z-xi) pour construire
                         * ((z-x0)*(z-x1)*....*(z-xi)
                         */
                        terme = terme*(abscisse_pt_a_Interpoler - abscisse_pt_connu);
                    }
                    /**
                     * On additionne la valeur du terme trouvé à la valeur actuelle du polynôme
                     * donc à la valeur de pn(z)
                     */
                    ordonnee_pt_a_Interpoler = ordonnee_pt_a_Interpoler + terme;
                    Point_a_Interpoler.affecte_point(abscisse_pt_a_Interpoler, ordonnee_pt_a_Interpoler);
                    tabpts_a_interpoler[k] = Point_a_Interpoler;
                    System.out.printf("j=%d  valeur de p(%f) = %f\n", j, Point_a_Interpoler.abcisse(), Point_a_Interpoler.ordonnee());
                }
            }
        }
    }

