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

    public static double RINGPUSHER_LOAD = .5;
    public static double RINGPUSHER_LOAD_TELEOP = 0.6;
    public static double RINGPUSHER_SHOOT = .05;
    public static double SHOOT_VELOCITY = 1750; // middle goal: 1600 if shooting from the back wall
    public long postShotTime = 0;

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
        while ((System.currentTimeMillis() < postShotTime) && this.opMode.opModeIsActive()) {
        }
        this.hera.ringPusher.setPosition(RINGPUSHER_LOAD);
        showData("Shoot status", "Shooting");
    }


    public void teleopUpdate(Gamepad gamepad1, Gamepad gamepad2) {
        if (gamepad2.a) {
            this.startShooter();
        }

        if (gamepad2.x) {
            this.hera.shooterMotor.setPower(0);
        }

        if (gamepad1.a) {
            hera.ringPusher.setPosition(0);
        }

        if (gamepad1.b) {
            hera.ringPusher.setPosition(.5);
        }

        if (gamepad1.y) {
            hera.ringPusher.setPosition(1);
        }

        if (gamepad1.x) {
            hera.ringPusher.setPosition(RINGPUSHER_SHOOT);
            shooterState = ShootState.SHOOTING;
        }

        if (gamepad1.dpad_left && hera.ringPusher.getPosition() < 1) {
            hera.ringPusher.setPosition(hera.ringPusher.getPosition() + .05);
        }

        if (gamepad1.dpad_right && hera.ringPusher.getPosition() > 0) {
            hera.ringPusher.setPosition(hera.ringPusher.getPosition() - .05);
        }

        showData("Ring Pusher Position", "" + hera.ringPusher.getPosition());


        switch (shooterState) {
            case LOADING:
                hera.ringPusher.setPosition(RINGPUSHER_LOAD_TELEOP);
                if (gamepad2.x && hera.ringTouchSensor.isPressed()) {
                    showData("Is touch sensor pressed? ", "" + hera.ringTouchSensor.isPressed());
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

    public void startShooter() {
        this.hera.shooterMotor.setVelocity(SHOOT_VELOCITY);
    }

    public void showData(String caption, String value) {
        this.telemetry.addData(caption, value);
        this.telemetry.update();
        Log.d(caption, value);
    }


}
