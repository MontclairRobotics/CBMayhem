package frc.team555.Mayhem.Controls;

import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.Mappers.OperatorMapper;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.data.CBRequestData;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBButton;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;

import static org.montclairrobotics.cyborg.Cyborg.requestData;

public class OperatorControls {

    //Cyborg Robot Class
    Cyborg cyborg;

    // Cyborg Hardware Adapter
    CBHardwareAdapter hardwareAdapter;

    // Data For Mapper
    CBRequestData requestData;

    // Joystick USB Port
    private final int STICK_ID = 1;

    // Declare Joystick axis and buttons
    private CBDeviceID xAxis;
    private CBDeviceID yAxis;
    private CBDeviceID shootCubeButton;
    private CBDeviceID intakeLiftUpButton;
    private CBDeviceID intakeLiftDownButton;
    private CBDeviceID mainLiftUpButton;
    private CBDeviceID mainLiftDownButton;
    
    public OperatorControls(Cyborg cyborg, CBHardwareAdapter hardwareAdapter, CBRequestData requestData){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;
        this.requestData = requestData;
    }

    public boolean setup(){
        xAxis = hardwareAdapter.add(
                new CBAxis(STICK_ID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        yAxis = hardwareAdapter.add(
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

        // Here is a hack:
        // create a second "drivetrain" to operate the intake
        // because they work the same way...
        cyborg.addTeleOpMapper(
                new CBArcadeDriveMapper(cyborg)
                        .setAxes(yAxis, null, xAxis)
                        .setRequestData(((RequestData) requestData).intake)
        );

        cyborg.addTeleOpMapper(
                new OperatorMapper(cyborg)
                        .setIntakeLiftDownButton(intakeLiftDownButton)
                        .setIntakeLiftUpButton(intakeLiftUpButton)
                        .setMainLiftDownButton(mainLiftDownButton)
                        .setMainLiftUpButton(mainLiftUpButton)
                        .setShootCubeButton(shootCubeButton)
        );

        return true;
    }

    public CBDeviceID getxAxis() {
        return xAxis;
    }

    public CBDeviceID getyAxis() {
        return yAxis;
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
