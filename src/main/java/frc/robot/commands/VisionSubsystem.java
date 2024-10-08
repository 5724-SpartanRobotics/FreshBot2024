package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.DriveTrainInterface;
import frc.robot.Subsystems.DriveTrainSubsystemRick;
import frc.robot.Subsystems.Constant.ControllerConstants;
import frc.robot.Subsystems.Constant.DebugLevel;
import frc.robot.Subsystems.Constant.DebugSetting;
import frc.robot.Subsystems.Constant.DriveConstants;
import frc.robot.Util.Conversions;

public class VisionSubsystem extends SubsystemBase {
    private XboxController controller;
    private DriveTrainSubsystemRick swerveDrive;
    private double[] tag;
    private double[] defaultTag = {-1, -1};

    /**
     * Drive Controller
     * @param swerveDrive The drive train subsystem
     * @param controller A joystick
     */
    public VisionSubsystem(DriveTrainSubsystemRick swerveDrive, XboxController controller){
        this.controller = controller;
        this.swerveDrive = swerveDrive;
    }

    public void setDrivePosition(double targetDistance){
        // TODO better default detection
        // TODO update robot position once the robot slows down     
        tag = SmartDashboard.getNumberArray("aprilTag1", defaultTag);
        SmartDashboard.putNumberArray("aprilTag1", defaultTag);
        if(tag[0] > 0) {
            System.out.println("TAG FOUND");
            System.out.println("Tagang: " + tag[1] + "roboang: " + swerveDrive.getGyroHeading().getDegrees());
            double theta = tag[1] - swerveDrive.getGyroHeading().getDegrees();
            double deltaD = (tag[0] - targetDistance) / 3.28; // Feet to meters
            double deltaX = -deltaD * Math.sin(Math.toRadians(theta));
            double deltaY = deltaD * Math.cos(Math.toRadians(theta));
            swerveDrive.ZeroDriveSensors(new Pose2d(new Translation2d(deltaY, deltaX), new Rotation2d()));
        }
    }
    
    public double getTheta(int tagID) {
        tag = SmartDashboard.getNumberArray("aprilTag1", defaultTag);
        double theta = tag[1] - swerveDrive.getGyroHeading().getDegrees();
        System.out.println("Target Angle: " + theta);
        return theta;
    }

    public boolean haveTag() {
        if(SmartDashboard.getNumberArray("aprilTag1", defaultTag)[0] > 0) {
            return true;
        } else {
            return false;
        }
    }
}