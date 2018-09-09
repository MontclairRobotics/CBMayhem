package frc.team555.Mayhem.Hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.behaviors.CBStdDriveBehavior;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.data.CBControlData;
import org.montclairrobotics.cyborg.data.CBRequestData;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;

public class Intake {

    // Declare Cyborg Robot Class
    private Cyborg cyborg;

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Data For Mappers
    private CBControlData controlData;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Declare Motors
    private CBDeviceID leftMotor;
    private CBDeviceID rightMotor;

    public Intake(Cyborg cyborg, CBHardwareAdapter hardwareAdapter, CBDeviceID pdb, CBControlData controlData){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
        this.controlData = controlData;
    }

    public boolean setup(){

        leftMotor = hardwareAdapter.add(
                new CBTalonSRX(10)
                        .setDeviceName( "Intake", "LeftMotor")
                        .setPowerSource(pdb, 8)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        rightMotor = hardwareAdapter.add(
                new CBTalonSRX(5)
                        .setDeviceName("Intake", "RightMotor")
                        .setPowerSource(pdb, 9)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        // yup again with the second drivetrain for the intake
        cyborg.addRobotController(
                new CBDifferentialDriveController(cyborg)
                        .addLeftDriveModule(
                                new CBDriveModule(new CB2DVector(-6, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(leftMotor)
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(6, 0), -180)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(rightMotor)
                                        )
                        )
                        .setControlData(((ControlData) controlData).intake)
        );


        return true;
    }

    public CBDeviceID getLeftMotor() {
        return leftMotor;
    }

    public CBDeviceID getRightMotor() {
        return rightMotor;
    }
}
