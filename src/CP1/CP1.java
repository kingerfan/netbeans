package CP1;
import HBDMIPS.EXE_MEM;
import HBDMIPS.ID_EXE;
import HBDMIPS.IF;
import memory.Converts;


/**
 * Created by Saleh on 7/5/2016.
 */
public class CP1 {
    public RegFile reg;
    private ID_EXE idexe;
    private EXE_MEM exemem;
    boolean isSingle;
    boolean isFloat;
    boolean zeroFlag;
    IF stageIF;

    public CP1(ID_EXE idexe , EXE_MEM exemem, IF stageIF){
        this.reg = new RegFile();
        this.idexe = idexe;
        this.exemem = exemem;
        this.stageIF = stageIF;

    }

    public void action(){
        isSingle = idexe.getControlBits().charAt(15)=='0'?false:true;
        isFloat = idexe.getControlBits().charAt(14)=='0'?false:true;
        if(!isFloat)
            return;
        System.out.println("isBransh: "+isBranch());
        boolean REG_DEST = (idexe.getControlBits().charAt(7)) == '0' ? false : true;
        //false means RT Should be used,  an I-Type instruction.
        //true means RD Should be used, is a R-Type instruction.
        String ALUOp = idexe.getControlBits().substring(1, 3)+idexe.getControlBits().substring(11, 13);
        System.out.println("ALUOp: "+ALUOp);
        //which is used in ALUControl
        boolean ALU_Src = (idexe.getControlBits().charAt(3)) == '0' ? false : true;// false means use readData is an R-type instruction.
        //true means use signExtend . an I-Type instruction.
        String func_bit = idexe.getSignExt().substring(26, 32);//use SignExtend to
        //detect function bits.
        String alucu_func = alu_cu(func_bit,ALUOp);//initialize ALUControl.
        if (REG_DEST) {
            System.out.println("wtf?!");
            exemem.setWrite_Register(idexe.forthPart);//RD Should be used.
            //is a R-Type.
        } else {//RT should be used.Is a I-Type.
            exemem.setWrite_Register(idexe.RT);///////
        }



        //Else Instruction is a simple ADD, Branch, and or LOAD/Store.
        //Read Data1 from ID/EXE Pipeline Register.
        //Decide on Data2 according to the ALU_SRC[if It's true then We
        //have a Load or something, Otherwise it's R-Type and or branch.]


        String data2 = reg.get(idexe.RT, isSingle);
        String data1 = reg.get(idexe.RD, isSingle);
        System.out.println(idexe.getRT() + "/" + data1 + "        -       " + idexe.getRD() + "/" + data2);
        System.out.println(idexe.RS +" "+idexe.RT+" "+idexe.RD+" "+idexe.forthPart);
//        System.out.println(Converts.singlePToFloat(data1)+"    "+Converts.singlePToFloat(data2));
        if(isBranch() ){
            if(!isNot()){
                if(zeroFlag){
                    stageIF.setPC(stageIF.getPC()+Integer.parseInt(idexe.getSignExt(),2));
                }
            }else{
                System.out.println("in branch");
                System.out.println(zeroFlag);
                if(!zeroFlag){
                    System.out.println("tu ife :|");
                    stageIF.setPC(stageIF.getPC()+Integer.parseInt(idexe.getSignExt(),2));
                }
            }
        }
        if(alucu_func!=null) {
            exemem.CP1_result = alu(data1, data2, alucu_func);
            //Save Zero, PC, RT_DATA[maybe used for Write Data of MEM], Control Bits
            // All to EXE/MEM.
            zeroFlag = Converts.singlePToFloat(exemem.CP1_result) == 0 ? true : false;// 1 means branch occurs! and 0 is not occur!
            exemem.setZERO(zeroFlag);
            System.out.println("zerFlag: "+exemem.getZERO());
        }


//        exemem.setNew_PC(idexe.getPC()+(byte)Long.parseLong(idexe.getSignExt(), 2));
        exemem.setRT_DATA(reg.get(idexe.getRT(), isSingle));
        exemem.setControlBits(idexe.getControlBits());

    }

    //<editor-fold defaultstate="collapsed" desc=" boolean methods ">
    /**
     * Is it Jump Register Instruction?
     * Detect by functionBit which is contained in 6 low value bits
     * of SignExtend.
     *
     * @return isJReg - Boolean representing detection of JR.
     */
    public boolean isJumpReg(){
        return idexe.getControlBits().charAt(13)=='1';
    }


    /**
     * Detects If Instruction contains value which
     * should be written back IN RegisterFile? [LW..]
     * It uses ID/EXE Control Bits to detect.[bit 1]
     * @return isRW - Boolean representing detection of RegisterWrite.
     */
    public boolean isRegwrite(){
        return idexe.getControlBits().charAt(0)=='1';
    }


    /**
     * Detects if Instruction is Jump ??????????????????????????????????????
     * It uses ID/EXE Control Bits to detect.[bit 11]
     * @return isJ - Boolean representing detection of Jump.
     */
    public boolean isJump(){
        return idexe.getControlBits().charAt(10)=='1';
    }



    /**
     * Detects if Instruction is Branch Equal?[beq]
     * It uses ID/EXE Control Bits to detect.[bit 7]
     * @return
     */
    public boolean isBranch(){
        System.out.println("in isBrach() control bits: "+idexe.getControlBits());
        return idexe.getControlBits().charAt(6)=='1';
    }


    /**
     * Detects if Instruction is Branch not Equal[bnq].
     * It uses ID/EXE Control Bits to detect.[bit 10]
     * @return isBnq - Boolean representing if its branch not equal.
     */
    public boolean isNot(){
        return idexe.getControlBits().charAt(9)=='1';
    }

    //</editor-fold>


    /**
     *
     * @param func_bit - 6bit function come from sgnExtend lower bits.
     * @param Aluop - 2 of 2bits ALUop come from controlUnit.
     * @return op - Appropriate operation.
     */
    public String alu_cu(String func_bit, String Aluop) {
        System.out.println("aluOP: "+Aluop);
        switch (Aluop) {
            case "1000":
                switch (func_bit) {
                    case "000000": //add
                        return "0010";
                    case "000001": //sub
                        return "0110";
//                    case "100100": //and
//                        return "0000";
//                    case "100101": //or
//                        return "0001";
//                    case "100111"://nor
//                        return "1001";
//                    case "101010":
//                        return "0111";
                    case "000010": // mul
                        return "1110";
                    case "000011": // div
                        return "1111";
//                    case "001000": // jump register (jr)
//                        return "0010"; // it's a fake add
//                    case "001100": // syscall
//                        return "0010"; // it's a fake add
                    default:
                        break;
                }
                break;
//            case "0000": // add operation used for addi,lw,sw
//                return "0010";
            case "0100": // sub operation used for c.eq.s & c.eq.d
                System.out.println("is Branch!");
                return "0110";
//            case "1100": // it's used for ORi
//                return "0001";
//            case "1110":
//                return "0000";
            default:
                break;
        }
        return null;

    }

    /**
     *
     * @param data_1 - input 1
     * @param data_2 - input 2
     * @param op - Operation that should be taken decided by ALUcontrol.
     * @return result - Operation taken result.
     */
    public String alu(String data_1, String data_2, String op) {
        switch (op) {
            case "0010":
                return addF(data_1, data_2);
            case "0110":
                System.out.println("sub!!!");
                return subF(data_1,data_2);
            case "1110":
                return mulFS(data_1,data_2);
            case "1111":
                return divF(data_1,data_2);
//            case "0000":
//                return data_1 & data_2;
//            case "0001":
//                return data_1 | data_2;
//            case "0111":
//                return data_1 < data_2 ? 0 : 1;
//            case "1110": // $d = $t << h
//                return data_1 << data_2;
//            case "1111": // $d = $t >> h
//                return data_1 >> data_2;
            default:
                break;
        }
        return null;
    }

    private String addF(String d1, String d2){
        if(d1.equals("00000000000000000000000000000000"))
            return d2;
        if(d2.equals("00000000000000000000000000000000"))
            return d1;

        Stage1_Add s1 = new Stage1_Add(d1,d2);
        s1.action();
        Stage2_Add s2 = new Stage2_Add(s1);
        s2.action();
        Stage3_Add s3 = new Stage3_Add(s2);
        s3.action();
        Stage4_Add s4 = new Stage4_Add(s3);
        s4.action();
//        System.out.println("add res : "+s4.getRes());
        return s4.getRes();
    }

    public String subF(String num1, String num2){
        if(num1.equals(num2)){
            return "00000000000000000000000000000000";
        }
//        System.out.println(num1+"------"+num2);
        if ( num2.substring(0,1).equals("1"))
            num2 = "0" + num2.substring(1);
        else if ( num2.substring(0,1).equals("0"))
            num2 = "1" + num2.substring(1);
        return this.addF(num1 , num2);
    }

    public String mulFS(String num1, String num2){
        if(num1.equals("00000000000000000000000000000000") || num2.equals("00000000000000000000000000000000"))
            return "00000000000000000000000000000000";
        Stage1_Add a = new Stage1_Add(num1,num2);
        a.action();
        String signRes = (Integer.parseInt(a.sign1)^Integer.parseInt(a.sign2)) + "";
        Long f1 = Long.parseLong(a.fraction1,2);
        Long f2 = Long.parseLong(a.fraction2,2);
        int ex1 = Integer.parseInt(a.exponent1,2) - 127;
        int ex2 = Integer.parseInt(a.exponent2,2) - 127;
        long bigAluRes = f1 * f2;
        String tmp = round(Long.toBinaryString(bigAluRes));
        tmp = tmp.substring(1);
        String exp = Integer.toBinaryString(ex1 + ex2+127);
        if(tmp.length()>23){
            tmp = tmp.substring(0,tmp.length()-1);
            exp = Integer.toBinaryString(Integer.parseInt(exp,2) + 1);
        }

        while (exp.length() < 8)
            exp = "0" + exp;
        return signRes + exp + tmp;
    }

    private String round(String s) {

        if ( s.length() > 24 ) {
            if ( s.charAt(25) == '1'){
                if (s.substring(26).contains("1")){
                    s = s.substring(0,24);
                    int i = s.length()-1;
                    while(s.charAt(i)=='1'){
                        if(i<0)
                            break;
                        s = s.substring(0,i)+'0'+s.substring(i+1);
                    }
                    if(i>=0)
                        s = s.substring(0,i)+'1'+s.substring(i+1);
                    else
                        s = '1'+ s;

                }
                else{
                    s = s.substring(0,24);
                }
            }
            else{
                s = s.substring(0,24);
            }
        }
        else {
            while (s.length() < 24)
                s = s + "0";
        }
        return s;
    }

    public String divF(String num1, String num2){
        if(num1.equals("00000000000000000000000000000000"))
            return "00000000000000000000000000000000";
        if(num2.equals("00000000000000000000000000000000")){
            System.out.println("div by zero!!");
            return "00000000000000000000000000000000";
        }
        String signRes = (Integer.parseInt(num1.substring(0,1))^Integer.parseInt(num2.substring(0,1))) + "";
        String tmp = num1;
        String res = "";
        int i = 0;
        while ( true ){
            num1 = tmp;
            tmp = this.subF(tmp,num2);
            if ( tmp.charAt(0) == '1')
                break;
            i++;
        }
        System.out.println("i = " + i);
        res = Integer.toBinaryString(i);
        res += ".";

        while (res.length() < 24 ) {
            num1 = this.mulFS(num1, Converts.floatToSingleP(2));
            tmp = this.subF(num1, num2);
            if (tmp.charAt(0) == '0') {
                res = res + "1";
                num1 = tmp;
            }
            else
                res = res + "0";
        }

        System.out.println("res = " + res);
        int indexOfDot = res.indexOf(".");
        res = res.replace(".","");
        res = res.substring(1);
        System.out.println("res1 = " + res);

        String exp = Integer.toBinaryString(indexOfDot +127 - 1);

        while (exp.length() < 8 )
            exp = "0" + exp;

        while (res.length() < 23 )
            res = res + "0";

        return signRes + exp + res;
    }
}
