package memory;


/**
 * Created by Saleh on 7/2/2016.
 */
public class Converts {

    //  float to single precision
    public static String  floatToSingleP(float f){
        String sign = f<0 ? "1": "0";
        String res = Integer.toBinaryString(Float.floatToIntBits(f));
        while(res.length()<31){
            res = "0"+res;
        }
        return sign+res;

    }

    //  float to double precision
    public static String floatToDoubleP(double d){
        String sign = d<0 ? "1": "0";
        String res = Long.toBinaryString(Double.doubleToLongBits(d));
        while(res.length()<63){
            res = "0"+res;
        }
        return sign+res;
    }

    //  single precision to double
    public static double singlePToFloat(String bin){
        if(bin.equals("00000000000000000000000000000000")){
            return 0;
        }
        char sign = bin.charAt(0);
        String exp = bin.substring(1,9);
        String fraction = "1"+bin.substring(9);

        double res = 0;
        for(int i=0 ; i<fraction.length() ; i++){
            res = fraction.charAt(i)=='1' ? res+Math.pow(2,-i) : res;
        }
        res *= Math.pow(2,Integer.parseInt(exp,2)-127);
        res *= sign=='0'? 1:-1;
        return res;
    }

    //  double precision to double
    public static double doublePToFloat(String bin){
        if(bin.equals("0000000000000000000000000000000000000000000000000000000000000000")){
            return 0;
        }
        char sign = bin.charAt(0);
        String exp = bin.substring(1,12);
        String fraction = "1"+bin.substring(12);

        double res = 0;
        for(int i=0 ; i<fraction.length() ; i++){
            res = fraction.charAt(i)=='1' ? res+Math.pow(2,-i) : res;
        }
        res *= Math.pow(2,Integer.parseInt(exp,2)-1023);
        res *= sign=='0'? 1:-1;
        return res;
    }

}
