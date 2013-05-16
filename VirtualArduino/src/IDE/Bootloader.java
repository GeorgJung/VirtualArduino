package IDE;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Bootloader {

	final static byte[] STK_GET_SYNC = { 48, 32 };// decimal
	final static byte[] STK_OK = { 20, 16 };
	final static byte[] STK_OKK = { 20, 00, 16 };
	final static byte[] STK_GET_PARAMETER_HARDWARE = { 65, -128, 32 };
	final static byte[] STK_GET_PARAMETER_FIRMWARE_MAJOR = { 65, -127, 32 };
	final static byte[] STK_GET_PARAMETER_FIRMWARE_MINOR = { 65, -126, 32 };
	final static byte[] STK_GET_PARAMETER_SYS_CLOCK = { 65, -104, 32 };
	final static byte[] STK_GET_PARAMETER_EXT_1 = { 65, -124, 32 };
	final static byte[] STK_GET_PARAMETER_EXT_2 = { 65, -123, 32 };
	final static byte[] STK_GET_PARAMETER_EXT_3 = { 65, -122, 32 };
	final static byte[] STK_GET_PARAMETER_EXT_4 = { 65, -121, 32 };
	final static byte[] STK_GET_PARAMETER_EXT_5 = { 65, -119, 32 };
	final static byte[] STK_SET_DEVICE = { 66, -122, 00, 00 };
	final static byte[] STK_SET_DEVICE_EXT = { 69, 05, 04, -41 };
	final static byte[] STK_ENTER_PROGMODE = { 80, 32 };
	final static byte[] STK_READ_SIGN = { 117, 32 };
	final static byte[] STK_UNIVERSAL_1 = { 86, -96, 03, -4 };
	final static byte[] STK_UNIVERSAL_2 = { 86, -96, 03, -3 };
	final static byte[] STK_UNIVERSAL_3 = { 86, -96, 03, -2 };
	final static byte[] STK_UNIVERSAL_4 = { 86, -96, 03, -1 };
	final static byte[] STK_LOAD_ADDRESS = { 85 };
	/*
	 * final static byte[] STK_LOAD_ADDRESS_1 = { 85, 64, 00, 32 };// final
	 * static byte[] STK_LOAD_ADDRESS_2 = { 85, -128, 00, 32 };// final static
	 * byte[] STK_LOAD_ADDRESS_3 = { 85, -64, 00, 32 };// final static byte[]
	 * STK_LOAD_ADDRESS_4 = { 85, 00, 01, 32 };// final static byte[]
	 * STK_LOAD_ADDRESS_5 = { 85, 64, 01, 32 };// final static byte[]
	 * STK_LOAD_ADDRESS_6 = { 85, -128, 01, 32 };// final static byte[]
	 * STK_LOAD_ADDRESS_7 = { 85, -64, 01, 32 };// final static byte[]
	 * STK_LOAD_ADDRESS_8 = { 85, 00, 02, 32 };//
	 */
	final static byte[] STK_PROG_PAGE = { 100, 00 };// , -128, 70
	// final static byte[] STK_PROG_PAGE_END = { 100, 00, 60, 70 };
	// final static byte[] STK_READ_PAGE = { 116, 00, -128, 70, 32 };
	final static byte[] STK_LEAVE_PROGMODE = { 81, 32 };
	final static byte[] STK_READ_PAGE = { 116, 00 };

	final static byte[] HARDWARE_VERSION = { 20, 03, 16 };
	final static byte[] FIRMWARE_VERSION_MAJOR = { 20, 04, 16 };
	final static byte[] FIRMWARE_VERSION_MINOR = { 20, 04, 16 };
	final static byte[] SYS_CLOCK_DUR = { 20, 03, 16 };
	final static byte[] PARAMETER_EXT_1 = { 20, 03, 16 };
	final static byte[] PARAMETER_EXT_2 = { 20, 04, 16 };
	final static byte[] DEVICE_SIGNATURE = { 20, 30, -107, 15, 16 };
	final static byte IN_SYNC = 20;
	final static byte EOC = 16;

	static LinkedList<Byte> code = new LinkedList<Byte>();
	static LinkedList<Integer> count = new LinkedList<Integer>();
	static LinkedList<byte[]> verify = new LinkedList<byte[]>();

	public static void main(String[] args) throws IOException {

		SerialPort serialPort = new SerialPort("COM5");
		try {
			serialPort.openPort();// Open serial port
			serialPort.setParams(9600, 8, 1, 0);// Set params.
			while (true) {

				byte[] buffer = new byte[2000];

				buffer = serialPort.readBytes();
				if (buffer != null) {

					byte[] out = getReply(buffer);

					serialPort.writeBytes(out);
					if (equate2(buffer, STK_LEAVE_PROGMODE)) {
						serialPort.closePort();// Close serial port
						break;
					}

				}

			}

		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
		// for testing
		/*
		 * for (int i = 0; i < code.size(); i++) { byte block = code.get(i);
		 * System.out.printf("%02X", block); System.out.println("");
		 * 
		 * }
		 */
	}

	public static byte[] getReply(byte[] buffer) throws IOException {
		// / comparing to variables, when matched return reply(byte[])
		if (equate2(buffer, STK_GET_SYNC)) {
			return STK_OK;
		}
		if (equate3(buffer, STK_GET_PARAMETER_HARDWARE)) {
			return HARDWARE_VERSION;
		}
		if (equate3(buffer, STK_GET_PARAMETER_FIRMWARE_MAJOR)) {
			return FIRMWARE_VERSION_MAJOR;
		}
		if (equate3(buffer, STK_GET_PARAMETER_FIRMWARE_MINOR)) {
			return FIRMWARE_VERSION_MINOR;
		}
		if (equate3(buffer, STK_GET_PARAMETER_SYS_CLOCK)) {
			return SYS_CLOCK_DUR;
		}

		if (equate3(buffer, STK_GET_PARAMETER_EXT_1)) {
			return PARAMETER_EXT_1;
		}
		if (equate3(buffer, STK_GET_PARAMETER_EXT_2)) {
			return PARAMETER_EXT_1;
		}
		if (equate3(buffer, STK_GET_PARAMETER_EXT_3)) {
			return PARAMETER_EXT_1;
		}
		if (equate3(buffer, STK_GET_PARAMETER_EXT_4)) {
			return PARAMETER_EXT_1;
		}
		if (equate3(buffer, STK_GET_PARAMETER_EXT_5)) {
			return PARAMETER_EXT_1;
		}
		if (equate4(buffer, STK_SET_DEVICE)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_SET_DEVICE_EXT)) {
			return STK_OK;
		}
		if (equate2(buffer, STK_ENTER_PROGMODE)) {
			return STK_OK;
		}
		if (equate2(buffer, STK_READ_SIGN)) {
			return DEVICE_SIGNATURE;
		}
		if (equate4(buffer, STK_UNIVERSAL_1)) {
			return STK_OKK;
		}
		if (equate4(buffer, STK_UNIVERSAL_2)) {
			return STK_OKK;
		}
		if (equate4(buffer, STK_UNIVERSAL_3)) {
			return STK_OKK;
		}
		if (equate4(buffer, STK_UNIVERSAL_4)) {
			return STK_OKK;
		}
		if (equate1(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate4(buffer, STK_LOAD_ADDRESS)) {
			return STK_OK;
		}
		if (equate2(buffer, STK_PROG_PAGE)) {
			byte[] block = new byte[(buffer.length) - 5];

			System.arraycopy(buffer, 4, block, 0, (buffer.length) - 5);

			verify.add(block);

			for (int i = 0; i < block.length; i++)
				code.add(block[i]);
			return STK_OK;
		}
		if (equate2(buffer, STK_PROG_PAGE)) {
			return STK_OK;
		}
		if (equate2(buffer, STK_READ_PAGE)) {
			byte[] blockX = verify.pop();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(IN_SYNC);
			outputStream.write(blockX);
			outputStream.write(EOC);

			byte[] back = outputStream.toByteArray();
			return back;
		}
		if (equate2(buffer, STK_LEAVE_PROGMODE)) {
			return STK_OK;
		}
		return buffer;

	}

	public static boolean equate4(byte[] command, byte[] buffer) {

		for (int i = 0; i < 4; i++) {
			if (command[i] != buffer[i])
				return false;

		}
		return true;
	}

	public static boolean equate3(byte[] command, byte[] buffer) {

		for (int i = 0; i < 3; i++) {
			if (command[i] != buffer[i])
				return false;

		}
		return true;
	}

	public static boolean equate2(byte[] command, byte[] buffer) {

		for (int i = 0; i < 2; i++) {
			if (command[i] != buffer[i])
				return false;

		}
		return true;
	}

	public static boolean equate1(byte[] command, byte[] buffer) {

		if (command[0] != buffer[0])
			return false;
		else
			return true;
	}

}
