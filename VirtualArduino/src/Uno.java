import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Uno {
	
	Pin[] digital;
	Pin[] analog;
	Pin[] power;
	Pin reset, aref,gnd;
	boolean powerLed, txLed, rxLed, testLed;
	
	byte[] flashMemory_program;
	byte[] flashMemory_bootloader;
	byte[] eeprom;
	
	// SRAM
	byte[] registers;
	byte[] IORegisters;
	byte[] extIORegisters;
	byte[] internalSram;
	
	byte dataBus;
	int pc;
	
	
	public Uno() {
		
		digital = new Pin[14];
		analog = new Pin[6];
		power = new Pin[5];
		//32768 flash memory : 27648 for program, 5120 for boatloader
		flashMemory_program = new byte[27648];
		flashMemory_bootloader = new byte[5120];
		
		registers = new byte[32];
		IORegisters = new byte[64];
		extIORegisters = new byte[160];
		internalSram = new byte[1792];
		
		eeprom = new byte[1024];
		
		dataBus = 0;
		pc = 0;

	}
	
	
	public void LoadProgram() throws FileNotFoundException, IOException {
		File program = new File("program.hex");
		HexFileParser hfp = new HexFileParser(program);
		flashMemory_program = hfp.parseFile();
	}

//	// X-Register getter
//	private int[] getXReg() {
//		int[] x = new int[16];
//		for (int i=0; i<8; i++) {
//			x[i] = registers[26][i];
//		}
//		for (int i=0; i<8; i++) {
//			x[8+i] = registers[27][i];
//		}
//		return x;
//	}
//	// X-Register setter
//	private void setXReg(int[] x) {
//		for (int i=0; i<8; i++) {
//			registers[26][i] = x[i];
//		}
//		for (int i=0; i<8; i++) {
//			registers[27][i] = x[8+i];
//		}
//	}
//	
//	// Y-Register getter
//	private int[] getYReg() {
//		int[] y = new int[16];
//		for (int i=0; i<8; i++) {
//			y[i] = registers[28][i];
//		}
//		for (int i=0; i<8; i++) {
//			y[8+i] = registers[29][i];
//		}
//		return y;
//	}
//	// Y-Register setter
//	private void setYReg(int[] x) {
//		for (int i=0; i<8; i++) {
//			registers[28][i] = x[i];
//		}
//		for (int i=0; i<8; i++) {
//			registers[29][i] = x[8+i];
//		}
//	}
//	
//	// Z-Register getter
//	private int[] getZReg() {
//		int[] z = new int[16];
//		for (int i=0; i<8; i++) {
//			z[i] = registers[30][i];
//		}
//		for (int i=0; i<8; i++) {
//			z[8+i] = registers[31][i];
//		}
//		return z;
//	}
//	// Z-Register setter
//	private void setZReg(int[] x) {
//		for (int i=0; i<8; i++) {
//			registers[30][i] = x[i];
//		}
//		for (int i=0; i<8; i++) {
//			registers[31][i] = x[8+i];
//		}
//	}
//	
	
	// 16-bit Instructions
	// each lines contains the following:-
	// - 
	public void addInstructions(String filename) throws FileNotFoundException {

		FileReader instructions = new FileReader(filename);
		BufferedReader reader = new BufferedReader(instructions);
//		:10 0000 00 0C946100 0C947E00 0C947E00 0C947E00 95

		
		while(true) {
			
			try {
				
				String line = reader.readLine();
				
				if (line.charAt(0) == ':') {
					// Byte count, 16 (0x10) or 32 (0x20)
					if (line.substring(1,3).equals("10")) {
						String address = line.substring(3,7);
						
					}
					if (line.substring(1,3).equals("20")) {
						String address = line.substring(3,7);
					}
					
				}
				
			} catch(Exception e) {
				break;
			}

			
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {

	}
}
