package os_procesess;

public class Console {
    
    /* Método que limpa a tela */
    public static void clearConsole(){
        try {
            String os = System.getProperty("os.name");

            if(os.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process p = pb.inheritIO().start();
                p.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process p = pb.inheritIO().start();
                p.waitFor();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }   // end of catch
    }   // end of clean
    
    /* Método de pergunta do console */
    public static boolean ask(String question) {
        boolean end = true;
        boolean ans = false;
        String opcao;

        do {
            System.out.print(question);
            System.out.print(" [S/N]: ");

            opcao = (IO.readString()).toLowerCase();

            if(opcao.length() == '1') {
                if(opcao.charAt(0) == 'n') {
                    end = false;
                    ans = true;
                } else if(opcao.charAt(0) == 's') {
                    end = false;
                    ans = false;
                } else {
                    System.out.println("OPÇÃO INVÁLIDA! CARACTERE INSERIDO NÃO É OPÇÃO, INSIRA 'S' PARA SIM OU 'N' PARA NÃO!");
                }
            } else {
                System.out.println("OPÇÃO INVÁLIDA! INSIRA SOMENTE UM CARACTERE!");
            }
        } while (end);

        return ans;
    }
}
