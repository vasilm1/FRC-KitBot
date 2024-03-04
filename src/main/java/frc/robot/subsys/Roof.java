package frc.robot.subsys;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Misc;
import frc.robot.Ports;
public class Roof {
    //ansh
    private static CANSparkMax roofmotor;
    public static Roof instance;

    private PIDController roofPID = new PIDController(.3, 0, .25);

    public RoofState state = RoofState.LOW;

    public void setState(RoofState state) {
        this.state = state;
    }

    public enum RoofState{
        HIGH(17,1),
        LOW(5,1);

        public final double pose, volts;

        private RoofState(double pose, double volts){
            this.pose = pose;
            this.volts = volts;
        }
    }
    public Roof()
    {
        roofmotor = new CANSparkMax(Ports.ROOF, MotorType.kBrushless);
        roofmotor.setInverted(true);
        roofmotor.burnFlash();
    }

    public void update()
    {
        double reqPower = roofPID.calculate(roofmotor.getEncoder().getPosition(), state.pose);

        reqPower = Misc.clamp(reqPower, -state.volts, state.volts);
        roofmotor.set(reqPower);

        if(hasReachedTargetPose(2))
        roofmotor.setVoltage(reqPower);
    }

    public boolean hasReachedTargetPose(double tolerance) {
        return Math.abs(roofmotor.getEncoder().getPosition() - state.pose) <= tolerance;
    }

    

    public static Roof getInstance() {
        if (instance == null) {
            instance = new Roof();
        }
        return instance;
    }
}
