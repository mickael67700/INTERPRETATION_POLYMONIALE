public class Interpolation {
    public static void main(String[] args){
        int NON_TROUVE = -1;
        int retour;
        CInterpolation Liste_Points = new CInterpolation();
        retour = Liste_Points.chargement_points_connus();
        if (retour != NON_TROUVE){
            Liste_Points.saisie_points_a_interpoler();
            Liste_Points.interpolation();
            Liste_Points.affiche_points_interpoles();
        }
    }
}
