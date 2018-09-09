package frc.team555.Mayhem.Controls;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBButton;
import org.montclairrobotics.cyborg.devices.CBDeviceID;

public class OperatorControls {

    // Cyborg hardware adapter
    CBHardwareAdapter hardwareAdapter;

    // Joystick USB Port
    private final int STICK_ID = 1;

    // Declare Joystick axis and buttons
    private CBDeviceID operXAxis;
    private CBDeviceID operYAxis;
    private CBDeviceID shootCubeButton;
    private CBDeviceID intakeLiftUpButton;
    private CBDeviceID intakeLiftDownButton;
    private CBDeviceID mainLiftUpButton;
    private CBDeviceID mainLiftDownButton;
    
    public OperatorControls(CBHardwareAdapter hardwareAdapter){
        this.hardwareAdapter = hardwareAdapter;
    }

    public boolean setup(){
        operXAxis = hardwareAdapter.add(
                new CBAxis(STICK_ID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        operYAxis = hardwareAdapter.add(
                new CBAxis(STICK_ID, 0)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        // Add button assignments to hardware adapter
        shootCubeButton      = hardwareAdapter.add(new CBButton(STICK_ID, 1));
        intakeLiftUpButton   = hardwareAdapter.add(new CBButton(STICK_ID, 2));
        intakeLiftDownButton = hardwareAdapter.add(new CBButton(STICK_ID, 3));
        mainLiftUpButton     = hardwareAdapter.add(new CBButton(STICK_ID, 4));
        mainLiftDownButton   = hardwareAdapter.add(new CBButton(STICK_ID, 5));

        return true;
    }

    public CBDeviceID getOperXAxis() {
        return operXAxis;
    }

    public CBDeviceID getOperYAxis() {
        return operYAxis;
    }

    public CBDeviceID getShootCubeButton() {
        return shootCubeButton;
    }

    public CBDeviceID getIntakeLiftUpButton() {
        return intakeLiftUpButton;
    }

    public CBDeviceID getIntakeLiftDownButton() {
        return intakeLiftDownButton;
    }

    public CBDeviceID getMainLiftUpButton() {
        return mainLiftUpButton;
    }

    public CBDeviceID getMainLiftDownButton() {
        return mainLiftDownButton;
    }
}
