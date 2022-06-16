
import java.io.*;
import java.net.*;
import java.util.Scanner;



public class Klient{
    public static int port = 8888;

    public static void main(String[] args) throws Exception {new Klient();}


    public Klient() throws Exception{
        Socket socket = new Socket("localhost", port);
        Socket socketForFiles = new Socket("localhost", 4444);


        if (socket.isConnected() && socketForFiles.isConnected()) {
            System.out.println("Połączono z serwerem");
            System.out.println("Jesli chcesz wysłać plik naciśnij ENTER !  - pliki zapisywane są do pliku txt C:/plik.txt");

        }

        ElGamal elGamal = new ElGamal();


        Thread thread1 = new Thread(() -> {


            while (true){
                try {


                    //wysyłanie
                    Scanner scannerZnakow = new Scanner(System.in);
                    String wiadomoscDlaKlienta = scannerZnakow.nextLine();

                    if (wiadomoscDlaKlienta.isEmpty()) {

                        //wysyłanie pliku
                        System.out.println("Podaj sciezke do pliku: ");
                        String str = scannerZnakow.nextLine();
                        if(str == ""){
                            System.out.println("Podaj poprawną sciezke! ");
                        }else{
                            FileInputStream fis = new FileInputStream(str);
                            byte[] bytes = new byte[4096];
                            OutputStream os = socketForFiles.getOutputStream();
                            System.out.println("Wysyłanie...");
                            fis.read(bytes, 0, bytes.length);
                            os.write(bytes, 0, bytes.length);
                            os.flush();

                            System.out.println("Wysyłanie pliku zakonczone !");
                        }

                    }else{

                        String str = elGamal.Encrypt(wiadomoscDlaKlienta,elGamal.x);
                        int key = elGamal.x;

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(str);
                        oos.writeObject(key);
                        //System.out.print("         --- wysłano String: "+str+" i klucz: "+key);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        socketForFiles.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }


        });



        Thread thread2 = new Thread(() -> {


            while (true){
                try {

                    //odbieranie wiadomsoci
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    String str = (String) ois.readObject();
                    int key = (int) ois.readObject();

                    String decryptedMessage = elGamal.Decrypt(str,key);
                    System.out.println("Serwer: "+decryptedMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        socketForFiles.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }




        });

        Thread thread3 = new Thread(() -> {

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("C:/plik.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            while (true) {
                try {

                    //odbieranie pliku


                    InputStream is = socketForFiles.getInputStream();


                    byte[] bytes = new byte[4096];
                    is.read(bytes, 0, bytes.length);
                    fos.write(bytes, 0, bytes.length);
                    fos.flush();

                    System.out.println("Plik nadpisany!");


                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        socketForFiles.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }


            }




        });

        thread1.start();
        thread2.start();
        thread3.start();


    }



}

