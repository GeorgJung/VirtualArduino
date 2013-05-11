import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import exceptions.InstructionNotFoundException;


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
	int clock;
	
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
		clock = 0;
	}
	
	
	public void LoadProgram() throws FileNotFoundException, IOException {
		File program = new File("program.hex");
		HexFileParser hfp = new HexFileParser(program);
		flashMemory_program = hfp.parseFile();
	}
	
	public void execProgram() throws InstructionNotFoundException {
		
		pc = 0;
		clock = 0;
		byte leastSig, mostSig;
		byte nextLeastSig, nextMostSig;
		
		while(pc < 27648) {
			
			leastSig = flashMemory_program[pc];
			mostSig = flashMemory_program[pc+1];
			
			if (match16BitInstruction(mostSig, leastSig))
				pc += 2;
			else {
				nextLeastSig = flashMemory_program[pc+2];
				nextMostSig = flashMemory_program[pc+3];
				
				if (match32BitInstruction(mostSig, leastSig, nextMostSig, nextLeastSig))
					pc += 4;
				else {
					throw new InstructionNotFoundException();
				}
			}
			
		}
		
	}
	
	public boolean match16BitInstruction(byte mostSig, byte leastSig) {
		return true;
	}
	
	public boolean match32BitInstruction(byte mostSig, byte leastSig, byte nextMostSig, byte nextLeastSig) {
		return true;
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

	
	public static void main(String[] args) throws IOException {

	}
}
