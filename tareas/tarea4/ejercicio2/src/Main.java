class Main {
    public static void main(String[] args) {
        if(args.length < 2)
            System.out.println("Uso: java -jar ejercicio2.jar <dirección de passwords-salt.txt> <dirección de common-passwords.txt>");
        else {
            NoHash nh = new NoHash();        
            nh.obtenContrasenas(args[0], args[1]);
        }
    }
}
