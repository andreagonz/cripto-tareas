class Main {
    public static void main(String[] args) {
        if(args.length < 1)
            System.out.println("Uso: java -jar chon-hacker.jar <num de hilos>");
        else new ChonHacker(Integer.parseInt(args[0]));        
    }
}
