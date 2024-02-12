// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.subsys.Drivetrain;
import frc.robot.subsys.Drivetrain.DriveSpeed;
import frc.robot.subsys.Launcher.FlickerState;
import frc.robot.subsys.Launcher.LauncherState;
import frc.robot.subsys.Launcher;
import frc.robot.subsys.Roof;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

private PS4Controller driver;
private PS4Controller operator;

private Drivetrain drivetrain;
private Launcher launcher;
private Roof roof;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    drivetrain = Drivetrain.getInstance();
    launcher = Launcher.getInstance();
    roof = Roof.getInstance();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    driver = new PS4Controller(0);
    operator = new PS4Controller(1);
  }

  @Override
  public void teleopPeriodic() {

    //drive controls

    if (driver.getRawButton(Controller.PS_L1)) 
       drivetrain.setDriveSpeed(DriveSpeed.SLOW);
       else
       drivetrain.setDriveSpeed(DriveSpeed.FAST);
       

  double forward = driver.getRawAxis(Controller.PS_AXIS_LEFT_Y);
  double turn = driver.getRawAxis(Controller.PS_AXIS_LEFT_X);


  drivetrain.drive(forward, turn);

  //launcher controls
    

  if (operator.getRawButton(Controller.PS_CIRCLE)){
    launcher.setFlickState(FlickerState.SHOOT);
  } else {
    launcher.setFlickState(FlickerState.OFF);
  }
  
  if (operator.getRawButton(Controller.PS_SQUARE)){
    launcher.setLaunchState(LauncherState.SHOOT);
  } else if (operator.getRawButton(Controller.PS_TRIANGLE)){
    launcher.setFlickState(FlickerState.INTAKE);
    launcher.setLaunchState(LauncherState.INTAKE);
  }
  else if(operator.getRawButton(Controller.PS_CROSS)){
    launcher.setFlickState(FlickerState.AMP);
    launcher.setLaunchState(LauncherState.AMP);
  }
  else {
    launcher.setLaunchState(LauncherState.OFF);
  }

  launcher.updateState();

  boolean roofopen = operator.getRawButton(Controller.PS_L1);
  boolean roofclose = operator.getRawButton(Controller.PS_R1);

  roof.move(roofopen,roofclose);


  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
