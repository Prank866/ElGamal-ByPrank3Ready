import java.util.Random;

public class ElGamal {

    Random random = new Random();

    public int modulo = 26;
    public int publicKey = 5;
    public int privateKeyAlice = 9;
    public int privateKeyBob = 2;
    public int x = random.nextInt(25)+1;

    public int[] StringPerByteTabINT(String str){
        char[] str2 = str.toCharArray();
        int[] intTab = new int[str2.length];

        for(int i=0; i<str2.length; i++){
            intTab[i] += (byte) str2[i];
        }

        return intTab;
    }


    public String ByteTabINTPerString(int[] intTab){
        char[] charTab = new char[intTab.length];
        String str = new String();

        for(int i=0; i<charTab.length; i++){
            charTab[i] = (char) intTab[i];
            str += charTab[i];
        }

        return str;

    }


    public double Alice(){
        double toReturn = Math.pow(publicKey, privateKeyAlice) % modulo;
        return toReturn;
    }

    public double Bob(){
        double toReturn = Math.pow(publicKey, privateKeyBob) % modulo;
        return toReturn;
    }

    public double intForBobToDecrypt(){
        double valueToDecryptForBob = Math.pow(Bob(), privateKeyAlice) % modulo;
        valueToDecryptForBob = (valueToDecryptForBob * x) % modulo;
        return valueToDecryptForBob;
    }

    public int BobCalculation(){
        double var1 = Math.pow(Alice(), privateKeyBob) % modulo;
        var1 = Math.pow(var1, EulerMethod(modulo)-1) % modulo;
        var1 = var1 * intForBobToDecrypt() % modulo;

        int var2 = (int) var1;

        return var2;
    }



    public String Encrypt(String str, int x){

        int[] intTab = StringPerByteTabINT(str);

        for(int i=0; i<intTab.length; i++){
            intTab[i] = intTab[i] - x;
        }

        String strAfterMultiple = ByteTabINTPerString(intTab);


        return strAfterMultiple;
    }

    public String Decrypt(String str, int BobCalculations){

        int[] intTab = StringPerByteTabINT(str);

        for(int i=0; i<intTab.length; i++){
            intTab[i] = intTab[i] + BobCalculations;
        }

        String strAfterDivide = ByteTabINTPerString(intTab);

        return strAfterDivide;
    }


    public static int Gcd(int a,int b){
        int i, hcf = 0;
        for(i = 1; i <= a || i <= b; i++) {
            if( a%i == 0 && b%i == 0 )
                hcf = i;
        }
        return hcf;
    }

    public int EulerMethod(int n){

        int eulerScore = 0;

        for (int i = 1; i <= n; i++){
            int x = 1;
            for (int j = 2; j < i; j++){
                if (Gcd(j, n) == 1){
                    x++;
                }
            }
            eulerScore = x;
        }
        return eulerScore;
    }

}
