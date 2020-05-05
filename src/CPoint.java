public class CPoint {
    private double x,y;

    public CPoint() {
         x = 0;
         y = 0;
    }

    void affecte_point(double vx, double vy){
        x = vx;
        y = vy;
    }
    double abcisse(){
        return this.x;
    }
    double ordonnee(){
        return this.y;
    }
}
