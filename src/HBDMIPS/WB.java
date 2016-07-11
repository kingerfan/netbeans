package HBDMIPS;
import CP1.CP1;
/**
 * Represents WriteBack stage.
 * @author HBD
 */
public class WB {

	/**
	 * Uses instance of InstructionDecode initialized
	 * earlier; Because <b>RegisterFile</b> is contained in it! :|
	 */
	ID id;

	/**
	 * MEM/WB Pipeline Register of MEM stage.
	 */

	MEM_WB memwb;//

	/**
	 *  using co-processor1 because it contains Float-RegisterFile
	 */

	CP1 cp1;

	public WB(ID id, CP1 cp1, MEM_WB memwb) {
		this.cp1 = cp1;
		this.id = id;
		this.memwb = memwb;
	}


	/**
	 * Do the job of WriteBack.
	 * This includes:
	 * 1- Get MEM2REG, REG_WRITE controlBits from MEM/WB Pipeline Register.
	 * 2- If something should be written to RegisterFile;
	 *      Then decide according to the MEM2REG which one of
	 *      ALU_Result [R-Type] or READ_Data [Load] should be used.
	 */
	public void action(boolean modebit) {
		boolean MEM2REG = (memwb.getControlBits().charAt(8)) == '0' ? false : true;
		boolean REG_WRITE = (memwb.getControlBits().charAt(0)) == '0' ? false : true;
		boolean isFloat = (memwb.getControlBits().charAt(14))=='0' ? false : true;
		boolean isSingle = (memwb.getControlBits().charAt(15))=='0' ? false : true;

		if (REG_WRITE) {
			System.out.println("memwb: "+memwb.Write_Register);
			if(isFloat){
				if(!MEM2REG) { // saving String (binary form) in float-RegisterFile
					cp1.reg.set(memwb.Write_Register, memwb.getCp1_Result(), isSingle);
				}
				else
					cp1.reg.set(memwb.Write_Register, memwb.getREAD_DATA(), isSingle);
			}
			else{
				if (!MEM2REG) //saving integer in register file
					id.regfile.setReg(memwb.Write_Register, memwb.getALU_result());
				else
					id.regfile.setReg(memwb.Write_Register, Integer.valueOf(memwb.getREAD_DATA(), 2));
			}
		}
	}
}