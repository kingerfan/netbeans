package CP1;

public class Stage1_Add {

    String num1;
    String num2;
    String exponent1;
    String exponent2;
    String fraction1;
    String fraction2;
    String sign1;
    String sign2;
    int exponentDiffrence = 0;

    public Stage1_Add(String num1, String num2){
        this.num1 = num1;
        this.num2 = num2;
    }

    public void action(){
        this.exponent1 = num1.substring(1,9);
        this.exponent2 = num2.substring(1,9);
        this.fraction1 = "1" + num1.substring(9);
        this.fraction2 = "1" + num2.substring(9);
        this.sign1 = num1.substring(0,1);
        this.sign2 = num2.substring(0,1);
        this.exponentDiffrence = this.exponentDiffrence();
    }

    private int exponentDiffrence(){
        return (Integer.parseInt(exponent1, 2)) - (Integer.parseInt(exponent2, 2));
    }

}
