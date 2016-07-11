
package HBDMIPS;

import java.util.ArrayList;
import java.util.List;
import memory.AddressAllocator;
import memory.Converts;

/**
 * Represents Memory Stage.
 * @author HBD
 */
public class MEM {

	/**
	 * Stored as a List of String Represents Memory.
	 */
	List<String> data_mem;
	/**
	 * EXE/MEM Pipeline Register of MEM stage.
	 */
	EXE_MEM exemem;
	/**
	 * MEM/WB Pipeline Register of MEM stage.
	 */
	MEM_WB memwb;
	/**
	 * ???????????????????????????????????????????????????????????????????
	 */
	IF stage_if;

	public MEM(EXE_MEM exemem, MEM_WB memwb, IF stage_If, ArrayList<String> Data) {
		this.data_mem = new ArrayList<String>(Data);
		this.exemem = exemem;
		this.memwb = memwb;
		this.stage_if = stage_If;
//		data_mem = new ArrayList<>(FileHandler.FileIO.FiletoStringArray("dataCache.txt"));
	}
	/**
	 * Do the job of Memory.
	 * This includes:
	 * 1- Get controlBits from EXE/MEM Pipeline Register.
	 * 2- If MEM_Write is Set then we should write Data which
	 *    is stored in RT_DATA in EXE/MEM, to the address of
	 *    memory that ALU_Result points to.
	 * 3- If MEM_READ is Set then we should read Data which
	 *    ALU_Result points to its address in memory.
	 * 4- Store ALU_Result, WriteRegister, controlBits in
	 *    in MEM/WB Pipeline Register.
	 */
	public void action(boolean modebit,memory.AddressAllocator aa) {
		boolean MEM_READ = (exemem.getControlBits().charAt(4)) == '0' ? false : true;
		boolean MEM_WRITE = (exemem.getControlBits().charAt(5)) == '0' ? false : true;
		boolean isFloat = (exemem.getControlBits().charAt(14))=='0' ? false : true;
		boolean isSingle = (exemem.getControlBits().charAt(15))=='0' ? false : true;


		// MEM_WRITE
		if (MEM_WRITE) {
			data_mem.set(exemem.getALU_result(), exemem.getRT_DATA());
		}
		// MEM_READ
		if (MEM_READ) {

			String binData;
			System.out.println(data_mem);
			System.out.println("loading from: "+ exemem.getALU_result());

			binData = data_mem.get(exemem.getALU_result());
			if(!isSingle && binData.length()<64)
				binData += (data_mem.get(exemem.getALU_result())).substring(0,32);
			System.out.println("loading "+binData);
// old!
//			if(modebit){
//				int i =0 ;
//				// get binary string
//				System.out.println("modebit mem read");
//				binData = aa.getMemory().get(aa.parse8DigitHex(i));
//				if(isFloat && !isSingle && binData.length()<64)
//					binData += (aa.getMemory().get(aa.parse8DigitHex(i+1))).substring(0,32);
//
//				// old
////				data = Integer.getInteger(aa.getMemory().get(aa.parse8DigitHex(i)),2).toString();
//			}
//			else{
//				// get binary string
//				binData = data_mem.get(exemem.getALU_result());
//				if(isFloat && !isSingle && binData.length()<64)
//					binData += (data_mem.get(exemem.getALU_result())).substring(0,32);
//				// old
////				data = Integer.getInteger(data_mem.get(exemem.getALU_result()),2).toString();
//			}

			memwb.setREAD_DATA(binData);
		}
		memwb.setCp1_Result(exemem.CP1_result);
		memwb.setALU_result(exemem.getALU_result());
		memwb.setWrite_Register(exemem.getWrite_Register());
		memwb.setControlBits(exemem.getControlBits());
	}


	/**
	 * View current Memory.
	 * @return Print - memory in a String each cell in a line formatted.
	 */
	public String print() {
		String print="";
		for (int i = 0; i < data_mem.size(); i++) {
			print+="Cell  [" + i + "] : " + Integer.parseInt(data_mem.get(i)) +"\n";
		}
		return print;
	}
}