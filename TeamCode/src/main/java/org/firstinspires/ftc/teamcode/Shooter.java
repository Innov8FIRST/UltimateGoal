package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {

    String SHOOTER_CAPTION = "Shooter Status";
    Telemetry telemetry;
    HardwareInnov8Hera hera;
    LinearOpMode opMode;

    public static double RINGPUSHER_LOAD = .535;
    public static double RINGPUSHER_SHOOT = .0;
    public double shootPower = .8;
    public long postShotTime = 0;
    public boolean shooterMotorIsRunning = false;
    private enum ShootState {LOADING, SHOOTING, SETTING_CHILL_TIME, CHILLING}
    public ShootState shooterState = ShootState.LOADING;

    public Shooter(Telemetry telemetry, HardwareInnov8Hera hera, LinearOpMode opMode) {

        this.opMode = opMode;
        this.hera = hera;
        this.telemetry = telemetry;
    }

    public void shoot() {
        this.hera.ringPusher.setPosition(RINGPUSHER_SHOOT);
        postShotTime = System.currentTimeMillis() + 1000;
        while (System.currentTimeMillis() < postShotTime);
        this.hera.ringPusher.setPosition(RINGPUSHER_LOAD);
        }


    public void teleopUpdate(Gamepad gamepad1, Gamepad gamepad2) {

        if (shooterMotorIsRunning) {
            if (gamepad2.a) {
                hera.shooterMotor.setPower(0);
                shooterMotorIsRunning = false;
            }
        }
        else {
            if (gamepad2.a) {
                hera.shooterMotor.setPower(shootPower);
                shooterMotorIsRunning = true;
            }
        }

        if (gamepad1.x) {
            hera.ringPusher.setPosition(RINGPUSHER_SHOOT);
            shooterState = ShootState.SETTING_CHILL_TIME;
        }


        switch (shooterState) {
            case LOADING:
                hera.ringPusher.setPosition(RINGPUSHER_LOAD);
                if (gamepad2.x && hera.ringTouchSensor.isPressed()) {
                    showData("TOUCH_SENSOR: ", "Touch sensor is pressed");
                    shooterState = ShootState.SHOOTING;
                }
                break;
            case SHOOTING:
                hera.ringPusher.setPosition(RINGPUSHER_SHOOT);
                shooterState = ShootState.SETTING_CHILL_TIME;
                showData("RING_PUSHER: ", "Shooting");
                break;
            case SETTING_CHILL_TIME:
                postShotTime = System.currentTimeMillis() + 2000;
                shooterState = ShootState.CHILLING;
                showData("RING_PUSHER: ", "Setting chill time");
                break;
            case CHILLING:
                if (System.currentTimeMillis() >= postShotTime) {
                    shooterState = ShootState.LOADING;
                    showData("RING_PUSHER: ", "Chilling");
                }
                break;
            default:
                showData("SWITCH_CAPTION: ", "Switch failed");
                break;
        }


    }

    public void showData(String caption, String value) {
        this.telemetry.addData(caption, value);
        this.telemetry.update();
        Log.d(caption, value);
    }


}
