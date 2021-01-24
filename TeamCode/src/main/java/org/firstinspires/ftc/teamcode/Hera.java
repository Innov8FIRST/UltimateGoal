package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Hera {
    Telemetry telemetry;
    String HERA_CAPTION = "Hera Status";
    DriveTrain driveTrain;
    Shooter shooter;
    Conveyer conveyer;
    Intake intake;
    OpenCV openCV;
    Wobble wobble;
    RingPosition position;
    LinearOpMode opMode;
    Gamepad gamepad1 = new Gamepad();
    Gamepad gamepad2 = new Gamepad();
    HardwareInnov8Hera hwmap;
    double defaultPower = 0.4;

    public Hera(Telemetry telemetry, HardwareMap hwmap, LinearOpMode opMode) {
        this.opMode = opMode;
        this.hwmap = new HardwareInnov8Hera(hwmap, opMode);
        this.telemetry = telemetry;
        driveTrain = new DriveTrain(this.telemetry, this.hwmap, this.opMode);
        shooter = new Shooter(this.telemetry, this.hwmap, this.opMode);
        conveyer = new Conveyer(this.telemetry, this.hwmap, this.opMode);
        intake = new Intake(this.telemetry, this.hwmap, this.opMode);
        driveTrain = new DriveTrain(this.telemetry, this.hwmap, this.opMode);
        openCV = new OpenCV(this.telemetry, this.hwmap, this.opMode);
        wobble = new Wobble(this.telemetry, this.hwmap, this.opMode);
        this.telemetry.addData(HERA_CAPTION, "ready to go");
        this.telemetry.update();
    }

    public void stop() {
        this.telemetry.addData(HERA_CAPTION, "free");

        this.telemetry.update();
    }

    public void teleop(Gamepad gamepad1, Gamepad gamepad2) {

        while (this.opMode.opModeIsActive()) {
            this.telemetry.addData(HERA_CAPTION, "teleop-ing");
            driveTrain.teleopUpdate(gamepad1, gamepad2);
            shooter.teleopUpdate(gamepad1, gamepad2);
            conveyer.teleopUpdate(gamepad1, gamepad2);
            intake.teleopUpdate(gamepad1, gamepad2);
            wobble.teleopUpdate(gamepad1, gamepad2);
            this.telemetry.update();
        }
    }
    

    public void forwardTurn() {
        if(this.opMode.opModeIsActive()){
            /*
            driveTrain.turn(90);
            driveTrain.goForward(12);
            driveTrain.turn(-90);
            */
//            driveTrain.goBackward(36, defaultPower);
//            this.opMode.sleep(2000);
            driveTrain.goForward(48, defaultPower);
//            this.opMode.sleep(2000);
//            driveTrain.turn(15);
//            this.opMode.sleep(2000);
//            driveTrain.turn(-15);
        }
    }

    public void redAutoShoot() {

        if(this.opMode.opModeIsActive()) {
            showData("Step One: ", "Start shooter motor");
            this.hwmap.shooterMotor.setPower(.8);
            showData("Step Two: ", "Get ring number");
            position = openCV.getRingNumber();
            showData("Step Three: ", "Go backwards");
            driveTrain.goBackward(10, defaultPower);
            showData("Step Four: ", "Turn 15 degrees");
            driveTrain.turn(7, defaultPower);
            showData("Step Five: ", "SHOOT");
            shooter.shoot();
            showData("Step Six: ", "Beddy Bye");
            this.opMode.sleep(2000);
            showData("Step Seven: ", "Turn -15 degrees");
            driveTrain.turn(-7, defaultPower);
            showData("Step Eight: ", "" + position);
            if (position == RingPosition.ONE) {
                // Drive to 2nd square
                driveTrain.goBackward(30, defaultPower);
                driveTrain.goLeft(26, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(28, defaultPower);
                wobble.reset();
                showData("Ring Position", "One");
            } else if (position == RingPosition.FOUR) {
                // Drive to 3rd square
                driveTrain.goBackward(54, defaultPower);
                driveTrain.goLeft(50, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(52, defaultPower);
                wobble.reset();
                showData("Ring Position", "Four");
            } else {
                // Drive to 1st square
                driveTrain.goBackward(26, defaultPower);
                driveTrain.goLeft(50, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(24, defaultPower);
                wobble.reset();
                showData("Ring Position", "None");
            }
        }
    }
    public void redAutoNoShoot() {
        if(this.opMode.opModeIsActive()) {
            position = openCV.getRingNumber();
            driveTrain.goForward(10, defaultPower);
            driveTrain.turn(-180, defaultPower);
            if (position == RingPosition.ONE) {
                // Drive to 2nd square
                driveTrain.goBackward(30, defaultPower);
                driveTrain.goLeft(26, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(28, defaultPower);
                wobble.reset();
                showData("Ring Position", "One");
            } else if (position == RingPosition.FOUR) {
                // Drive to 3rd square
                driveTrain.goBackward(54, defaultPower);
                driveTrain.goLeft(50, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(52, defaultPower);
                wobble.reset();
                showData("Ring Position", "Four");
            } else {
                // Drive to 1st square
                driveTrain.goBackward(26, defaultPower);
                driveTrain.goLeft(50, defaultPower);
                wobble.drop();
                // Park over line
                driveTrain.goForward(24, defaultPower);
                wobble.reset();
                showData("Ring Position", "None");
            }
        }
    }

    public void parkOnLine(){
        driveTrain.goForward(7*12, defaultPower);
    }


    public void showData(String caption, String value) {
        this.telemetry.addData(caption, value);
        this.telemetry.update();
        Log.d(caption, value);
    }

}
