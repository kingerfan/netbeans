package HBDMIPS;


/**
 * Represents Control Unit in stage InstructionDecode. 
 * @author HBD
 */
public class CU {
    String opcode;
    String out;

    public CU() {

    }


    /**
     * Do the job of CU.
     * This includes:
     * 1- Gets 6bit Opcode from 26 to 31.
     *    [0 to 5 in our convention from left to right :| ]
     * 2- Outputs 13bits of Control.
     * @param op - 5bits of opcode.
     * @return out - 13bits of Control represented in String.
     */
    public String action(String op,String ins) {
        opcode = op;
        out = decode(Integer.parseInt(op, 2),ins);
        return out;
    }



    /**
     *
     * @param op - 5bits opcode represented in String.
     * @return out - 13bits of CotrolBits represented in String.
     */
    public String decode(int op,String ins) {
        // regwrite aluop(2) alusrc memread
        // memwrite branch regdest mem2reg
        // this bit added optionaly not compatible with book :
        // notbits
        // jump
        // second aluop (2)
        // jr bit
        // CP1 process
        // is Single (single=1 , double=0)
        String isSingle = "1";
        if(ins.substring(6,11).equals("00000")){ // is Single
            isSingle = "1";
        }
        else if(ins.substring(6,11).equals("00001")){ //is Double
            isSingle = "0";
        }
        System.out.println("OP: "+op);
        switch (op) {
            case 0:
                if("001000".equals(ins.substring(26,32)))
                    return "1100000100000101";
                // RType
                return "1100000100000001";
            case 13:
                //ORI
                return "1111000000000001";
            case 12:
                //ANDI
                return "1111000000010001";
            case 8:
                // ADDI
                return "1001000000000001";
            case 35:
                // LW
                return "1001100010000001";
            case 43:
                // SW
                return "0001010100000001";
            case 4:
                // BEQ
                return "0010001000000001";
            case 5:
                // BNE
                return "0010001001000001";
            case 2:
                // JUMP
                return "0000000000100001";
            case 3:
                // JAL
                return "1000000000100001";
            case 49:
                // LWC1
                return "1001100010000011";
            case 53:
                // LDC1
                return "1001100010000010";
            case 57:
                // SWC1
                return "0001010010000011";
            case 61:
                // SDC1
                return "0001010010000010";

            case 17:
                int funct = Integer.parseInt(ins.substring(26),2);
                if(ins.substring(6,11).equals("01000")){  // is Breanch
                    System.out.println("LLLLLLL"+ins);
                    if(ins.substring(11,16).equals("00000")){   // c flag not sett
                        return "0000001001000011";
                    }
                    else if(ins.substring(11,16).equals("00001")){ // c flag set
                        return "0000001000000011";
                    }
                    else{
                        return null;
                    }
                }
                System.out.println(funct);
                switch(funct){

                    case 50:

                        return "001000000000001" + isSingle;
                    default:
                        return "110000010000001" + isSingle;

                }
            default:
                return null;
        }

    }


    /**
     * Set Opcode which CU should decide on.
     * @param opcode - 5bits Opcode represented in String.
     */
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }
}