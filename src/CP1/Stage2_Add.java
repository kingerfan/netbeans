package CP1;


public class Stage2_Add {

    Stage1_Add s1;
    int exponentDifference = 0;
    int f1 = 0;
    int f2 = 0;
    int bigAluRes = 0;
    int oneOrtwo = 0;



    public Stage2_Add(Stage1_Add s1){
        this.s1 = s1;
        this.exponentDifference = s1.exponentDiffrence;
        this.f1 = Integer.parseInt(s1.fraction1,2);
        this.f2 = Integer.parseInt(s1.fraction2,2);
    }

    public void action() {
        if (exponentDifference <= 0) {
            f1 = Integer.parseInt(Integer.toBinaryString(f1 >> Math.abs(exponentDifference)), 2);
            oneOrtwo = 2;
        } else if (exponentDifference > 0) {
            f2 = Integer.parseInt(Integer.toBinaryString(f2 >> exponentDifference), 2);
            oneOrtwo = 1;
        }
    }



}
