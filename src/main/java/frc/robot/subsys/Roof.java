package frc.robot.subsys;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Ports;
public class Roof {
    //ansh
    private static CANSparkMax roofmotor;
    public static Roof instance;

    public Roof()
    {
        roofmotor = new CANSparkMax(Ports.ROOF, MotorType.kBrushless);
        roofmotor.setInverted(false);
        roofmotor.setIdleMode(IdleMode.kBrake);
        roofmotor.burnFlash();
    }

    public void move(boolean roofopen, boolean roofclose) 
    {
        if(roofopen)
            roofmotor.set(.2);
        else if(roofclose)
            roofmotor.set(-.2);
        else roofmotor.set(0);
    }

    public static Roof getInstance() {
        if (instance == null) {
            instance = new Roof();
        }
        return instance;
    }
}
