package frc.robot.subsys;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Misc;
import frc.robot.Ports;



public class Drivetrain {
    
    private DriveSpeed DSpeed = DriveSpeed.FAST;
    
    private static CANSparkMax rightFront;
    private static CANSparkMax rightBack;
    private static CANSparkMax leftFront;
    private static CANSparkMax leftBack;

    private static Drivetrain instance;
    
    public enum DriveSpeed {
        SLOW(0.15),
        FAST(.5);

        public final double speed;

        private DriveSpeed(double speed) {
            this.speed = speed;
        }
    }


    public Drivetrain(){
    rightFront = new CANSparkMax(Ports.DRIVE_RIGHT_1, MotorType.kBrushed);
    rightFront.setInverted(true);
    rightFront.setOpenLoopRampRate(0.5);
    rightFront.setClosedLoopRampRate(0.5);
    rightFront.burnFlash();

    rightBack = new CANSparkMax(Ports.DRIVE_RIGHT_2, MotorType.kBrushed);
    rightBack.setInverted(true);
    rightBack.setOpenLoopRampRate(0.5);
    rightBack.setClosedLoopRampRate(0.5);
    rightBack.burnFlash();

    leftFront = new CANSparkMax(Ports.DRIVE_LEFT_1, MotorType.kBrushed);
    leftFront.setInverted(false);
    leftFront.setOpenLoopRampRate(0.5);
    leftFront.setClosedLoopRampRate(0.5);
    leftFront.burnFlash();

    leftBack = new CANSparkMax(Ports.DRIVE_LEFT_2, MotorType.kBrushed);
    leftBack.setInverted(false);
    leftBack.setOpenLoopRampRate(0.5);
    leftBack.setClosedLoopRampRate(0.5);
    leftBack.burnFlash();
    }

    public void drive(double forward, double turn) {
        double leftSpeed = Misc.clamp((forward * DSpeed.speed) - (turn * .15), -1, 1);
        double rightSpeed = Misc.clamp((forward * DSpeed.speed) + (turn * .15), -1, 1);

        SmartDashboard.putNumber("RIGHT SPEED", (int) (rightSpeed * 100));
        SmartDashboard.putNumber("LEFT SPEED", (int) (leftSpeed * 100));
        SmartDashboard.putNumber("LEFT 1 OUTPUT", leftFront.getOutputCurrent());
        SmartDashboard.putNumber("LEFT 2 OUTPUT", leftBack.getOutputCurrent());
        SmartDashboard.putNumber("RIGHT 1 OUTPUT", rightFront.getOutputCurrent());
        SmartDashboard.putNumber("RIGHT 2 OUTPUT", rightBack.getOutputCurrent());

        leftFront.set(leftSpeed);
        leftBack.set(leftSpeed);

        rightFront.set(rightSpeed);
        rightBack.set(rightSpeed);
    }

    public void setDriveSpeed(DriveSpeed driveSpeed) {
        this.DSpeed = driveSpeed;
    }

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }
}
