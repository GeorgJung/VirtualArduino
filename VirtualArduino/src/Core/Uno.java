package Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import avrora.core.Disassembler;
import avrora.core.Instr;
import avrora.core.Program;
import avrora.monitors.IORegMonitor;
import avrora.monitors.RealTimeMonitor;
import avrora.sim.ActiveRegister;
import avrora.sim.GenInterpreter;
import avrora.sim.InterpreterFactory;
import avrora.sim.Simulator;
import avrora.sim.clock.ClockDomain;
import avrora.sim.mcu.ATMega32;
import avrora.sim.mcu.MicrocontrollerFactory;
import avrora.syntax.atmel.AtmelProgramReader;

public class Uno {

	private Simulator simulator;
	private ActiveRegister PORTB, PORTC, PORTD;

	public Uno() throws Exception {
		
		String[] file = new String[1];
		file[0] = "Blink.asm";
		
		ClockDomain cd = new ClockDomain(16000000);
        cd.newClock("external", 1000000);
        
        AtmelProgramReader reader = new AtmelProgramReader();

		Program p = reader.read(file);
        
		MicrocontrollerFactory f = new ATMega32.Factory();

		InterpreterFactory intF = new GenInterpreter.Factory();
		
		simulator = new Simulator(0, intF, f.newMicrocontroller(0, cd, intF, p), p);
		
		IORegMonitor monitor1 = new IORegMonitor();
		monitor1.newMonitor(simulator);

		RealTimeMonitor monitor = new RealTimeMonitor();
		monitor.newMonitor(simulator);

		PORTB = simulator.getInterpreter().getIOReg(0x18);
		PORTC = simulator.getInterpreter().getIOReg(0x15);
		PORTD = simulator.getInterpreter().getIOReg(0x12);
		
	}

	public boolean uploadCode(byte[] code) throws Exception {
		try {
			
			Disassemble(code);
			
			ClockDomain cd = new ClockDomain(16000000);
	        cd.newClock("external", 1000000);
			
			String[] file = new String[1];
			file[0] = "Code.asm";
			
			AtmelProgramReader reader = new AtmelProgramReader();
	
			Program p = reader.read(file);
			
			MicrocontrollerFactory f = new ATMega32.Factory();
	
			InterpreterFactory intF = new GenInterpreter.Factory();
			
			simulator = new Simulator(0, intF, f.newMicrocontroller(0, cd, intF, p), p);
			
			IORegMonitor monitor1 = new IORegMonitor();
			monitor1.newMonitor(simulator);
	
			RealTimeMonitor monitor = new RealTimeMonitor();
			monitor.newMonitor(simulator);
	
			PORTB = simulator.getInterpreter().getIOReg(0x18);
			PORTC = simulator.getInterpreter().getIOReg(0x15);
			PORTD = simulator.getInterpreter().getIOReg(0x12);
			
			System.out.println(file[0]);
			
			return true;
			
		} catch (Exception e) {
			System.out.println("upload failed");
			return false;
		}
	}

	public void Disassemble(byte[] code) throws IOException {

		FileWriter out = new FileWriter(new File("Code.asm"));
		Disassembler da = new Disassembler();
		for (int index = 0; index < code.length;) {
			try {
				Instr i = da.disassemble(0, code, index);
				out.write(i.toString() + "\n");
				System.out.println(i.toString());
				index += i.getSize();
			} catch (Exception e) {
				System.out.println("exception");
				index += 2;
			}
		}
		out.close();
	}

	public void run() {
		try {
			simulator.start();
		} catch (Exception e) {
			System.out.println("No program found on the board!");
		}
	}
	
	public boolean getDigitalPin0() {
		return PORTD.readBit(0);
	}
	
	public boolean getDigitalPin1() {
		return PORTD.readBit(1);
	}
	
	public boolean getDigitalPin2() {
		return PORTD.readBit(2);
	}
	
	public boolean getDigitalPin3() {
		return PORTD.readBit(3);
	}
	
	public boolean getDigitalPin4() {
		return PORTD.readBit(4);
	}
	
	public boolean getDigitalPin5() {
		return PORTD.readBit(5);
	}
	
	public boolean getDigitalPin6() {
		return PORTD.readBit(6);
	}
	
	public boolean getDigitalPin7() {
		return PORTD.readBit(7);
	}
	
	public boolean getDigitalPin8() {
		return PORTB.readBit(0);
	}
	
	public boolean getDigitalPin9() {
		return PORTB.readBit(1);
	}
	
	public boolean getDigitalPin10() {
		return PORTB.readBit(2);
	}
	
	public boolean getDigitalPin11() {
		return PORTB.readBit(3);
	}
	
	public boolean getDigitalPin12() {
		return PORTB.readBit(4);
	}
	
	public boolean getDigitalPin13() {
		return PORTB.readBit(5);
	}
	
	public boolean getGND() {
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		Uno board = new Uno();
		board.run();
	}
	
}
