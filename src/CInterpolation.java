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
}
