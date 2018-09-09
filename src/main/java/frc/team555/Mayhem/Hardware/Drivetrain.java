package frc.team555.Mayhem.Hardware;

import edu.wpi.first.wpilibj.CounterBase;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;

public class Drivetrain {

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

    public Drivetrain(CBHardwareAdapter hardwareAdapter, CBDeviceID pdb){
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
