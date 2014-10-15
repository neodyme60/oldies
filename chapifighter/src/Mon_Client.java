public class Mon_Client {
    public static void main(String argv[]) {
        Un_Client Mon_Client;
        try {
            Mon_Client = new Un_Client(argv[0], argv[1], argv[2], argv[3]);

        } catch (Exception Telle_E) {
            System.err.println("Ligne de Commande : java Mon_Client [ <Pseudo> <Adr IP> <Port> <Pct_Name> ] ");
        }
    }
}
