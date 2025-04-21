package cr.ac.una.tournamentcontrolsystem;

public class Main {

    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.toString(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
