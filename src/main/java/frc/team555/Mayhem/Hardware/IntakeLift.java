package frc.team555.Mayhem.Hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;

public class IntakeLift {

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Declare Motors
    private CBDeviceID motor;

    // Declare Encoders
    private CBDeviceID encoder;

    public IntakeLift(CBHardwareAdapter hardwareAdapter, CBDeviceID pdb){
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
    }

    public boolean setup(){

        motor = hardwareAdapter.add(
                new CBTalonSRX(9)
                        .setDeviceName("Intake", "LiftMotor")
                        .setPowerSource(pdb, 6)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        encoder = hardwareAdapter.add(
                new CBEncoder(motor, FeedbackDevice.QuadEncoder, false, 1)
        );

        return true;
    }

    public CBDeviceID getMotor() {
        return motor;
    }

    public CBDeviceID getEncoder() {
        return encoder;
    }
}
