/* 
SREG: Status Register
C: Carry Flag
Z: Zero Flag
N: Negative Flag
V: Two's complement overflow indicator S: N (XOR) V, For signed tests
H: Half Carry Flag
T: Transfer bit used by BLD and BST instructions I: Global Interrupt Enable/Disable Flag
*/




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.InstructionNotFoundException;


public class Atmega328 {
	
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
	
	int SREG;
	
	byte dataBus;
	int pc;
	int clock;
	
	public Atmega328() {
		
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
		
		SREG = 0;
		
		dataBus = 0;
		pc = 0;
		clock = 0;
	}
	
	
	public void LoadProgram() throws FileNotFoundException, IOException {
		File program = new File("program.hex");
		HexFileParser hfp = new HexFileParser(program);
		flashMemory_program = hfp.parseFile();
	}
	
	//SREG - C
	public boolean SREG_getC() {
		
		int c = SREG & 1;
		return c==0?false:true;

	}
	
	public void SREG_setC(boolean set) {
		
		SREG = SREG | (set?1:0);
		
	}
	
	//SREG - Z
	public boolean SREG_getZ() {
		
		int z = SREG&2;
		return z==0?false:true;
		
	}
	
	public void SREG_setZ(boolean set) {
		
		SREG = SREG | (set?2:0);
		
	}
	
	//SREG - N
	public boolean SREG_getN() {
		
		int n = SREG&4;
		return n==0?false:true;
		
	}
	
	public void SREG_setN(boolean set) {
		
		SREG = SREG | (set?4:0);
		
	}
	
	//SREG - V
	public boolean SREG_getV() {
		
		int n = SREG&8;
		return n==0?false:true;
		
	}
	
	public void SREG_setV(boolean set) {
		
		SREG = SREG | (set?8:0);
		
	}
	
	//SREG - S
	public boolean SREG_getS() {
		
		int n = SREG&16;
		return n==0?false:true;
		
	}
	
	public void SREG_setS(boolean set) {
		
		SREG = SREG | (set?16:0);
		
	}
	
	//SREG - H
	public boolean SREG_getH() {
		
		int n = SREG&32;
		return n==0?false:true;
		
	}
	
	public void SREG_setH(boolean set) {
		
		SREG = SREG | (set?32:0);
		
	}
	
	//SREG - T
	public boolean SREG_getT() {
		
		int n = SREG&64;
		return n==0?false:true;
		
	}
	
	public void SREG_setT(boolean set) {
		
		SREG = SREG | (set?64:0);
		
	}
	
	//SREG - I
	public boolean SREG_getI() {
		
		int n = SREG&128;
		return n==0?false:true;
		
	}
	
	public void SREG_setI(boolean set) {
		
		SREG = SREG | (set?128:0);
		
	}
	
	public void execProgram() throws InstructionNotFoundException {
		
		pc = 0;
		clock = 0;
		byte leastSig, mostSig;
		byte nextLeastSig, nextMostSig;
		
		while(pc < 27648) {
			
			leastSig = flashMemory_program[pc];
			mostSig = flashMemory_program[pc+1];
			
			matchInstruction(mostSig, leastSig);
			
			//nextLeastSig = flashMemory_program[pc+2];
			//nextMostSig = flashMemory_program[pc+3];
				
			
		}
		
	}
	
	
	public boolean matchInstruction(byte mostSig, byte leastSig) {
		
		int mostValue;
		if (mostSig < 0)
			mostValue = -mostSig + 128;
		else
			mostValue = mostSig;
		
		// most significant 4bits fitch
		int first = mostValue & 240;
		
		// 2nd most significant 4 bits fitch
		int second = mostValue & 15;
		
		
		int leastValue;
		if (leastSig < 0)
			leastValue = -leastSig + 128;
		else
			leastValue = leastSig;
		
		// 3rd most significant 4bits fitch
		int third = leastValue & 240;
				
		// 4th most significant 4 bits fitch
		int fourth = leastValue & 15;
		
				
		switch (mostValue & 210){
			case 128: switch (leastValue & 8){
						case 0: // LD(LDD) (iv) index z
						case 1: // LD(LDD) (iv) index y
						default: return false;
					}
			case 130: switch (leastValue & 8){
						case 0: // ST(STD) (iv) index z
						case 1: // ST(STD) (iv) index y
						default: return false;
					}
		}
				
		switch (first) {
			case 0: switch (second){
						case 0: // NOP
						case 1: // MOVW
						case 2:	// MULS
						case 3: switch(leastValue & 136){ // FMUL, FMULS, MULSU, FMULSU
									case 0: // MULSU
									case 8: // FMUL
									case 128: // FMULS
									case 136: // FMULSU
									default: return false;
								}
						default: switch(second & 12){
									case 4: // CPC
									case 8: // SBC
									case 12: // ADD, LSL
									default: return false;
								}
					}
			case 16: switch (second & 12){
						case 0: // CPSE
						case 4: // CP
						case 8: // SUB
						case 12: // ADC, ROL
						default: return false;
					}
			case 32: switch (second & 12){
						case 0: // TST, AND
						case 4: // CLR, EOR
						case 8: // OR
						case 12: // MOV
						default: return false;
					}
			case 48: // CPI
			case 64: // SBCI
			case 80: // SUBI
			case 96: // ORI, SBR
			case 112: // ANDI
			case 128: switch (second & 14){
						case 0: switch (leastValue & 15) {
									case 0: // LD(LDD) (i) index z
									case 1: // LD(LDD) (i) index y
									default: return false;
								}
						case 1: switch (leastValue & 15) {
									case 0: // ST(STD) (i) index z
									case 1: // ST(STD) (i) index y
									default: return false;
								}
					}
			case 144: // 1001 instructions
			case 160: switch (second & 8) {
						case 0: // LDS(16-bit)
						case 8: // STS(16-bit)
						default: return false;
					}
			case 176: switch (second & 8) {
						case 0: // IN
						case 8: // OUT
						default: return false;
					}
			case 192: // RJMP
			case 208: // RCALL
			case 224: // LDI, SER
			case 240: switch (second & 12) {
						case 0: // BRBS
						case 4: // BRBC
						default: if ((fourth & 8)==0){
										switch (second & 14) {
											case 8: // BLD
											case 10: // BST
											case 12: // SBRC
											case 14: // SBRS
										}
								} else return false;
					}
			default: return false;
		}
		
	}
	
	
	public boolean NOP() {
		//0000 0000 0000 0000
		pc ++;
		clock ++;
		return true;
	}
	
	
	public boolean MOVW(int instruction) {
		//0000 0001 dddd rrrr
		try {
			int r = instruction & 15;
			int d = instruction & 240;
			d = d>>4;
			
			registers[d] = registers[r];
			registers[d+1] = registers[r+1];
			
			pc++;
			clock++;
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
	public boolean MULS(int instruction) {
		//0000 0010 dddd rrrr
		try {
			int r = instruction & 15;
			int d = instruction & 240;
			d = d>>4;
			
			byte rr = registers[r];
			byte dd = registers[d];
			
			int result = dd*rr;
			
			if (result<0)
				result = -result+32768;
			
			registers[0] = (byte)(result & 255);
			registers[1] = (byte)(result>>8 & 255);
			
			
			
			pc++;
			clock += 2;
			
			return true;
			
		} catch(Exception e) {
			return false;
		}
	}
	
	
	

}
