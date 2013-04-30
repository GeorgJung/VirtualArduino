
public class VoltagePin extends Pin {
	
	private double voltage;
	
	public VoltagePin() {
		setVoltage(0);
	}
	
	public VoltagePin(int voltage) {
		this.setVoltage(voltage);
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}
	
	
	
}
