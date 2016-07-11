package CP1;

public class Stage4_Add {
    Stage3_Add s3;
    String fracRes = "";
    String exp = "";
    String res = "";
    int zeroCnt;
    public Stage4_Add(Stage3_Add s3){
        this.s3 = s3;
        this.fracRes = Integer.toBinaryString(s3.s2.bigAluRes).substring(1);
    }

    public void action(){
        this.zeroCnt = s3.zeroCnt;
        while (fracRes.length() < 23 )     // make res fraction 23 bit
            fracRes = fracRes + "0";

        String exp = "";
        if ( s3.s2.oneOrtwo == 1 )
            exp = s3.s2.s1.exponent1;
        else if ( s3.s2.oneOrtwo == 2 )
            exp = s3.s2.s1.exponent2;

        if(zeroCnt>0){
            exp = Integer.toBinaryString(Integer.parseInt(exp, 2)-zeroCnt);
        }

        if ( fracRes.length() > 23 ){
            fracRes = fracRes.substring(0,fracRes.length()-1);
            exp = Integer.toBinaryString(Integer.parseInt(exp,2) + 1);
        }
        while (exp.length() < 8 ){
            exp = "0" + exp;
        }
        res = s3.resSign + exp + fracRes;
    }
    public String getRes(){
        return res;
    }
}
