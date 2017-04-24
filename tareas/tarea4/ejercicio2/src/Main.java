class Main {
    public static void main(String[] args) {
        NoHash nh = new NoHash();
        nh.obtenContrasenas("passwords-salt.txt", "common-passwords.txt");
        // System.out.println(nh.hash("hola"));
    }
}
