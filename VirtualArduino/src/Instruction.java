import org.omg.Dynamic.Parameter;


public class Instruction {
	String type;
	String[] parameters;
	static String JMP = "1001010kkkkk110k,kkkkkkkkkkkkkkkk";
	static String SBC = "000010rdddddrrrr";
	static String AND = "001000rdddddrrrr";
	public Instruction(String ins) {
		/*
		 0C 94 61 00
		 0000 1100 1001 0100 0110 0001 0000 0000
		 =
		 94 0C
	 	 1001 0100 0000 1100
		 00 61
		 0000 0000 0110 0001
		 */
		String instruction = hexToBinary(ins);
		String line1 = instruction.substring(8,16) + instruction.substring(0,8);
		String line2 = instruction.substring(24) + instruction.substring(16,24);
		
		applyMasks(line1, line2);
		
	}
	
	private void applyMasks(String line1, String line2) {
		boolean flag = true;
		
		// JMP instruction
		String[] mask = JMP.split(",");
		String k = "";	
		for (int i=0; i<mask[0].length(); i++) {
			if (mask[0].charAt(i) == 'k') {
				k = k + line1.charAt(i);
				continue;
			}
			if (mask[0].charAt(i) != line1.charAt(i)) {
				flag = false;
				break;
			}
		}
		if (flag) {
			for (int i=0; i<mask[1].length(); i++) {
				if (mask[1].charAt(i) == 'k') {
					k = k + line2.charAt(i);
					continue;
				}
				if (mask[1].charAt(i) != line2.charAt(i)) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			type = "JMP";
			parameters = new String[1];
			parameters[0] = binaryToHex(shiftLeft(k));
			return;
		}
		
		
		// SBC instruction
		flag = true;
		mask = SBC.split(",");
		String r = "";
		String d = "";
		for (int i=0; i<mask[0].length(); i++) {
			if (mask[0].charAt(i) == 'r') {
				r = r + line1.charAt(i);
				continue;
			}
			if (mask[0].charAt(i) == 'd') {
				d = d + line1.charAt(i);
				continue;
			}
			if (mask[0].charAt(i) != line1.charAt(i)) {
				flag = false;
				break;
			}
		}
		if (flag) {
			type = "SBC";
			parameters = new String[2];
			parameters[0] = binaryToHex(d);
			parameters[1] = binaryToHex(r);
			return;
		}
		
		
		// AND instruction
		flag = true;
		mask = AND.split(",");
		r = "";
		d = "";
		for (int i=0; i<mask[0].length(); i++) {
			if (mask[0].charAt(i) == 'r') {
				r = r + line1.charAt(i);
				continue;
			}
			if (mask[0].charAt(i) == 'd') {
				d = d + line1.charAt(i);
				continue;
			}
			if (mask[0].charAt(i) != line1.charAt(i)) {
				flag = false;
				break;
			}
		}
		if (flag) {
			type = "AND";
			parameters = new String[2];
			parameters[0] = binaryToHex(d);
			parameters[1] = binaryToHex(r);
			return;
		}
		
		
		if (!flag)
			type = "NA";
		
	}
	
	
	private String shiftLeft(String s) {
		s += "0";
		return s.substring(1);
	}

	public static String hexToBinary(String s) {
		String r = "";
		for (int i=0; i<s.length(); i++) {
			switch(s.charAt(i)) {
				case '0': r = r + "0000";break;
				case '1': r = r + "0001";break;
				case '2': r = r + "0010";break;
				case '3': r = r + "0011";break;
				case '4': r = r + "0100";break;
				case '5': r = r + "0101";break;
				case '6': r = r + "0110";break;
				case '7': r = r + "0111";break;
				case '8': r = r + "1000";break;
				case '9': r = r + "1001";break;
				case 'A': r = r + "1010";break;
				case 'B': r = r + "1011";break;
				case 'C': r = r + "1100";break;
				case 'D': r = r + "1101";break;
				case 'E': r = r + "1110";break;
				case 'F': r = r + "1111";break;
				default:;
			}
		}
		return r;
	}
	
	public static String binaryToHex(String s) {
		String r = "";
		for (int i=s.length()-1; i>=0; i--) {
			int tmp = 0;
			try {
				if (s.charAt(i) == '1')
					tmp += 1;
				if (s.charAt(i-1) == '1')
					tmp += 2;
				if (s.charAt(i-2) == '1')
					tmp += 4;
				if (s.charAt(i-3) == '1')
					tmp += 8;
			} catch (Exception e) {
				;
			}
			
			switch(tmp) {
			case 10 : r = 'A' + r;break;
			case 11 : r = 'B' + r;break;
			case 12 : r = 'C' + r;break;
			case 13 : r = 'D' + r;break;
			case 14 : r = 'E' + r;break;
			case 15 : r = 'F' + r;break;
			default : r = tmp + r;break;
			}
			i-=3;
			tmp = 0;
		}
		return r;
	}
	
	public static void main(String[] args) {
		Instruction a = new Instruction("10200102");
		System.out.println(a.type+","+a.parameters[0]+","+a.parameters[1]);
	}
	
}
