package frc.team555.Mayhem.Hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;

public class Intake {

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Declare Motors
    private CBDeviceID leftMotor;
    private CBDeviceID rightMotor;

    public Intake(CBHardwareAdapter hardwareAdapter, CBDeviceID pdb){
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
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

        return true;
    }

    public CBDeviceID getLeftMotor() {
        return leftMotor;
    }

    public CBDeviceID getRightMotor() {
        return rightMotor;
    }
}
