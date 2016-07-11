package CP1;

/**
 * Created by Saleh on 7/2/2016.
 */

public class RegFile {

    public String[] regs = new String[32];

    RegFile(){
        for(int i=0 ; i<32 ; i++){
            regs[i] = "00000000000000000000000000000000";
        }
    }



    public void set(int index, String value, boolean isSingle){
        if(isSingle){
            regs[index] = value;
        }
        else{
            regs[index] = value.substring(0,32);
            if(value.length()>=64)
                regs[index+1] = value.substring(32);
            else
                regs[index+1] = "00000000000000000000000000000000";

        }
    }

    public String get(int index, boolean isSingle){
        String res = regs[index];

        if(!isSingle){
            res += regs[index+1];
        }
        return res;
    }

}
