package frc.team555.Mayhem.Hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.CounterBase;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.*;

public class MainLift {
    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Declare Motors
    private CBDeviceID frontMotor;
    private CBDeviceID backMotor;

    // Declare Encoders
    private CBDeviceID encoder;

    // Declare Limit Switch
    private CBDeviceID limitSwitch;

    public MainLift(CBHardwareAdapter hardwareAdapter, CBDeviceID pdb){
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
    }

    public boolean setup(){

        frontMotor = hardwareAdapter.add(
                new CBTalonSRX(4)
                        .setDeviceName("MainLift", "Front")
                        .setPowerSource(pdb, 4)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        backMotor = hardwareAdapter.add(
                new CBTalonSRX(2)
                        .setDeviceName("MainLift", "Back")
                        .setPowerSource(pdb, 5)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        encoder = hardwareAdapter.add(
                new CBEncoder(4, 5, CounterBase.EncodingType.k4X, false, 1)
        );

        limitSwitch = hardwareAdapter.add(new CBDigitalInput(9));

        return true;
    }

    public CBDeviceID getFrontMotor() {
        return frontMotor;
    }

    public CBDeviceID getBackMotor() {
        return backMotor;
    }

    public CBDeviceID getEncoder() {
        return encoder;
    }

    public CBDeviceID getLimitSwitch() {
        return limitSwitch;
    }
}