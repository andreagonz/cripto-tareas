class Main {
    public static void main(String[] args) {
        if(args.length < 3)
            System.out.println("Uso: java -jar passwords.jar <dirección de passwords-salt.txt> <dirección de common-passwords.txt> <Núm de hilos>");
        else {
            NoHash nh = new NoHash(Integer.parseInt(args[2]));
            nh.obtenContrasenas(args[0], args[1]);
        }
    }
}
