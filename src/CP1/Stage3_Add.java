package CP1;
import memory.Converts;

public class Stage3_Add {
    Stage2_Add s2;
    String resSign = "";
    int zeroCnt = 0;
    public Stage3_Add(Stage2_Add s2){
        this.s2 = s2;
    }

    public void action(){
        if( s2.s1.sign1.equals("1") && s2.s1.sign2.equals("1")){
            s2.bigAluRes = s2.f1 + s2.f2;
            resSign = "1";
        }
        else if( s2.s1.sign1.equals("1") && s2.s1.sign2.equals("0")){
            s2.bigAluRes = Math.abs(s2.f1 - s2.f2);
            if ( Math.abs(Converts.singlePToFloat(s2.s1.num2)) < Math.abs(Converts.singlePToFloat(s2.s1.num1)) )
                resSign = "1";
            else
                resSign = "0";
        }
        else if( s2.s1.sign1.equals("0") && s2.s1.sign2.equals("1")){
            s2.bigAluRes = Math.abs(s2.f1 - s2.f2);
            if (Math.abs(Converts.singlePToFloat(s2.s1.num2)) > Math.abs(Converts.singlePToFloat(s2.s1.num1)))
                resSign = "1";
            else
                resSign = "0";
        }
        else if( s2.s1.sign1.equals("0") && s2.s1.sign2.equals("0")){
            s2.bigAluRes = Math.abs(s2.f1 + s2.f2);
            resSign = "0";
        }
        zeroCnt = 24 - Integer.toBinaryString(s2.bigAluRes).length();
    }
}
