package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Wobble {

    String WOBBLE_CAPTION = "Wobble Status";
    Telemetry telemetry;
    HardwareInnov8Hera hera;
    LinearOpMode opMode;

    public double dropperDown = .75;
    public double dropperUp = 0;
    public double grabberOpen = 0;
    public double grabberClose = 1;


    public Wobble(Telemetry telemetry, HardwareInnov8Hera hera, LinearOpMode opMode) {

        this.opMode = opMode;
        this.hera = hera;
        this.telemetry = telemetry;
    }

    public void drop() {
        this.hera.wobbleDropper.setPosition(dropperUp);
    }

    public void teleopUpdate(Gamepad gamepad1, Gamepad gamepad2) {
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            this.hera.wobbleGrabber.setPosition(grabberClose);
        }

        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            this.hera.wobbleGrabber.setPosition(grabberOpen);
        }

        if (Math.abs(gamepad1.left_trigger) > .5 || Math.abs(gamepad2.left_trigger) > .5) {
            this.hera.wobbleArm.setPower(.2);
        }

        else if (Math.abs(gamepad1.right_trigger) > .5 || Math.abs(gamepad2.right_trigger) > .5) {
            this.hera.wobbleArm.setPower(-.2);
        }

        else {
            this.hera.wobbleArm.setPower(0);
        }

        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            this.hera.wobbleDropper.setPosition(dropperUp);
        }

        if (gamepad1.dpad_down || gamepad2.dpad_down) {
            this.hera.wobbleDropper.setPosition(dropperDown);
        }
    }
}
