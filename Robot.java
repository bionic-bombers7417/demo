/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final PWMVictorSPX leftone = new PWMVictorSPX(0);
  private final PWMVictorSPX rightone = new PWMVictorSPX(2);
  private final PWMVictorSPX lefttwo = new PWMVictorSPX(1);
  private final PWMVictorSPX righttwo = new PWMVictorSPX(3);
  private final Spark elevator = new Spark(9);
  private final Spark grabber = new Spark(8);
  private final Spark grabber2 = new Spark(5);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_joy = new Joystick(1);
  private final Timer m_timer = new Timer();
  private final DigitalInput outlimitSwitch = new DigitalInput(6);
  private final DigitalInput inlimitSwitch = new DigitalInput(7);
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    leftone.set(-m_stick.getRawAxis(1)*.7 + m_stick.getRawAxis(4)*.5);
    lefttwo.set(-m_stick.getRawAxis(1)*.7 + m_stick.getRawAxis(4)*.5);
    rightone.set(m_stick.getRawAxis(1)*.7 + m_stick.getRawAxis(4)*.5);
    righttwo.set(m_stick.getRawAxis(1)*.7 + m_stick.getRawAxis(4)*.5);

    //limit switches for grabber
    if(outlimitSwitch.get()){
      m_timer.reset();
      m_timer.start();
      while(true){
        if (m_timer.get() < .05) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }
    if(inlimitSwitch.get()){
      m_timer.reset();
      m_timer.start();
      while(true){
        if (m_timer.get() < .05) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }
    //press right RB to Lift to Ball Stages
    if(m_joy.getRawButton(3)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 1.05) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          elevator.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    //press left LB to Lift to Hatch Panel First Stage
    if(m_joy.getRawButton(4)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 0.7) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          elevator.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    


    //BALL OPEN
    //press Left on DPAD to Open/Release Grabber for Ball
    if(m_joy.getRawButton(7)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 0.04) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }
      
    //BALL GRAB
    //press Right on DPAD to Close Grabber on Ball
    if(m_joy.getRawButton(8)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 0.04) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    //HATCH GRAB
    //press UP on DPAD to Release HATCH
    if(m_joy.getRawButton(9)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 0.03) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    // Start Position
    //HATCH RELEASE
    /// press DOWN on DPAD to Grab Hatch
    if(m_joy.getRawButton(10)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < 0.03) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    //HATCH RELEASE to BALL RELEASE
    /// press DOWN on LEFT Joystick
    if(m_stick.getRawButton(11)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < .4) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    //BALL RELEASE to HATCH RELEASE
    /// press DOWN on RIGHT Joystick
    if(m_stick.getRawButton(12)){
      m_timer.reset();
      m_timer.start();
      while(true){
        
        if (m_timer.get() < .35) {
          //m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
          grabber.set(-1);
        } else {
          //m_robotDrive.stopMotor(); // stop robot
          elevator.set(-.08);
          break;
        }
      }
    }

    //motor for the elevator on Logitech Game Controller
    if(m_stick.getRawButton(1)){
      elevator.set(1);
    }else if(m_stick.getRawButton(2)){
      elevator.set(-1);
    }else{
      //stops the elevator from gradually decreasing in height
      elevator.set(-.08);
    }
    if(m_joy.getRawButton(1)){
      elevator.set(1);
    }else if(m_joy.getRawButton(2)){
      elevator.set(-1);
    }else{
      //stops the elevator from gradually decreasing in height
      elevator.set(-.08);
    }
    
    //ball
    /*if(m_stick.getRawButton(3)){
      m_timer.start();
      if(m_timer.get() < .4){
        grabber.set(1);
      }else if(m_timer.get() < .8){
        grabber.set(-1);
      }
      grabber.set(0);
      m_timer.stop();
      m_timer.reset();
    }
    //hatch panel
    if(m_stick.getRawButton(4)){
      m_timer.start();
      if(m_timer.get() < .2){
        grabber.set(-1);
      }else if(m_timer.get() < .4){
        grabber.set(1);
      }
      grabber.set(0);
      m_timer.stop();
      m_timer.reset();
    }*/
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
