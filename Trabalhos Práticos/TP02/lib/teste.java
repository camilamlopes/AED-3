package lib;

class classe {
    int[] x;
    
    classe() {
        this.x = new int[5]; 
        for(int i = 0; i < 5; i++) this.x[i] = i; 
    }

    void display () {
        System.out.println(x);
    }

}

public class teste {
    public static void main (String[] args) {
        classe c = new classe();

        int[] nx = c.x;
        classe nc = new classe();

        for(int i = 0; i < c.x.length; i++) {
            nc.x[i] = nx[i];
            c.x[i] = 0;
        }
    }    
}
