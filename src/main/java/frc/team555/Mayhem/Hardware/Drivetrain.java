package frc.team555.Mayhem.Hardware;

import edu.wpi.first.wpilibj.CounterBase;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;
import org.montclairrobotics.cyborg.utils.CBPIDErrorCorrection;

public class Drivetrain {

    // Declare Cyborg Robot Class
    private Cyborg cyborg;

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Declare Motors
    private CBDeviceID frontLeftMotor;
    private CBDeviceID frontRightMotor;
    private CBDeviceID backLeftMotor;
    private CBDeviceID backRightMotor;

    // Declare Encoders
    private CBDeviceID leftEncoder;
    private CBDeviceID rightEncoder;

    // Inches Per Tick For Encoders
    private final double INCHES_PER_TICK = 96 / 4499;

    public Drivetrain(Cyborg cyborg,CBHardwareAdapter hardwareAdapter, CBDeviceID pdb){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
    }

    public boolean setup(){

        frontLeftMotor = hardwareAdapter.add(
                new CBTalonSRX(1)
                        .setDeviceName("DriveTrain", "FrontLeft")
                        .setPowerSource(pdb, 0)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        frontRightMotor = hardwareAdapter.add(
                new CBTalonSRX(7)
                        .setDeviceName("DriveTrain", "FrontRight")
                        .setPowerSource(pdb, 1)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        backLeftMotor = hardwareAdapter.add(
                new CBTalonSRX(3)
                        .setDeviceName("DriveTrain", "BackLeft")
                        .setPowerSource(pdb, 2)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        backRightMotor = hardwareAdapter.add(
                new CBTalonSRX(8)
                        .setDeviceName("DriveTrain", "BackRight")
                        .setPowerSource(pdb, 3)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        leftEncoder = hardwareAdapter.add(new CBEncoder(1, 0, CounterBase.EncodingType.k4X, false, INCHES_PER_TICK));
        rightEncoder = hardwareAdapter.add(new CBEncoder(3, 2, CounterBase.EncodingType.k4X, false, INCHES_PER_TICK));

        // setup robot controllers
        cyborg.addRobotController(
                new CBDifferentialDriveController(cyborg)
                        .addLeftDriveModule(
                                new CBDriveModule(
                                        new CB2DVector(-1, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(frontLeftMotor)
                                                        .addSpeedController(backLeftMotor)
                                                        .setEncoder(leftEncoder)
                                                        .setErrorCorrection(
                                                                new CBPIDErrorCorrection()
                                                                        .setConstants(new double[]{1.5, 0, 0.0015}
                                                                        )
                                                        )
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(1, 0), 180)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(frontRightMotor)
                                                        .addSpeedController(backRightMotor)
                                                        .setEncoder(rightEncoder)
                                                        .setErrorCorrection(
                                                                new CBPIDErrorCorrection()
                                                                        .setConstants(new double[]{1.5, 0, 0.0015}
                                                                        )
                                                        )
                                        )
                        )
        );

        return true;
    }

    public CBDeviceID getFrontLeftMotor() {
        return frontLeftMotor;
    }

    public CBDeviceID getFrontRightMotor() {
        return frontRightMotor;
    }

    public CBDeviceID getBackLeftMotor() {
        return backLeftMotor;
    }

    public CBDeviceID getBackRightMotor() {
        return backRightMotor;
    }

    public CBDeviceID getLeftEncoder() {
        return leftEncoder;
    }

    public CBDeviceID getRightEncoder() {
        return rightEncoder;
    }
}
