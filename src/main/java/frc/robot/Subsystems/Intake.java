package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Constant.IntakeConstants;

public class Intake extends SubsystemBase{
    private VictorSPX intakeTop = new VictorSPX(IntakeConstants.TopMotorCanId);
    private VictorSPX intakeBot = new VictorSPX(IntakeConstants.BottomBeltsMotorCanId);
    
    public Intake(){
        //restore all settings
        intakeTop.configAllSettings(new VictorSPXConfiguration());
        intakeBot.configAllSettings(new VictorSPXConfiguration());

        intakeTop.setNeutralMode(NeutralMode.Brake);
        intakeBot.setNeutralMode(NeutralMode.Brake);

        intakeBot.setInverted(true);
    }

    public void RunIntake(double topspeed, double bottomspeed)
    {
        intakeTop.set(VictorSPXControlMode.PercentOutput, topspeed);
        intakeBot.set(VictorSPXControlMode.PercentOutput, bottomspeed);
    }
}
