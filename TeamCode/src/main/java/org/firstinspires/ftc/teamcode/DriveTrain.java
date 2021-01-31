package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Timer;
import java.util.TimerTask;

public class DriveTrain {
    String DRIVE_TRAIN_CAPTION = "Drive Status";
    Telemetry telemetry;
    HardwareInnov8Hera hera;
    LinearOpMode opMode;
    private double wheelOnePower = 0.4;
    private double wheelTwoPower = 0.4;
    private double wheelThreePower = 0.4;
    private double wheelFourPower = 0.4;
    private static double WHEEL_DIAMETER = 4;
    private static double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    private static double TICKS_IN_A_ROTATION = 28*40; //CPR = 28, 40:1 gearbox on motor
    private static double GEAR_RATIO = 0.5; // 2:1 gear ratio (2 wheel rotations for one motor rotation)
    private static final double TICKS_IN_AN_INCH = ((TICKS_IN_A_ROTATION*GEAR_RATIO)/ WHEEL_CIRCUMFERENCE); // The number of encoder ticks per inch for our wheels
    public static double SIDE_TICKS_IN_INCH = (360/6); // The number of encoder ticks for one inch while travelling sideways, change later
    public DriveTrain(Telemetry telemetry, HardwareInnov8Hera hera, LinearOpMode opMode) {

// Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
// on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
// and named "imu".

        this.opMode = opMode;
        this.hera = hera;
        this.telemetry = telemetry;
        this.telemetry.addData(DRIVE_TRAIN_CAPTION, "Drive train initialized");
        this.telemetry.update();

    }

    public void goForward(double inches, double power) {
        wheelOnePower = power;
        wheelTwoPower = power;
        wheelThreePower = power;
        wheelFourPower = power;
        double startPosition = 0;
        double endPosition = 0;
        showData("DRIVE_TRAIN_CAPTION", "Robot is moving forward");
        this.telemetry.update();
        startPosition = hera.motorTwo.getCurrentPosition();
        endPosition = startPosition + (inches * TICKS_IN_AN_INCH); // How far you need to travel
        Orientation angles;
        angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startingOrientation = angles.firstAngle;
        double error = 0;
        double steer = 0;
        showData("StartPosition", "" + startPosition);
        showData("EndPosition", "" + endPosition);
        showData("CurrentPosition", "" + hera.motorTwo.getCurrentPosition());
        showData("wheel one power", "" + hera.motorOne.getPower());
        showData("wheel two power", "" + hera.motorTwo.getPower());
        showData("wheel three power", "" + hera.motorThree.getPower());
        showData("wheel four power", "" + hera.motorFour.getPower());
        showData("Initial Angle", "" + startingOrientation);
        showData("Current Angle", "" + angles.firstAngle);
        showData("error", "" + error);
        showData("steer", "" + steer);
        while (hera.motorTwo.getCurrentPosition() < endPosition && this.opMode.opModeIsActive()) {
            hera.motorOne.setPower(wheelOnePower);
            hera.motorTwo.setPower(wheelTwoPower);
            hera.motorThree.setPower(wheelThreePower);
            hera.motorFour.setPower(wheelFourPower);
            angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            error = startingOrientation - angles.firstAngle;
            double powerChanger = (error/100)+0.9;
            if(error < 0 && this.opMode.opModeIsActive()){
                hera.motorThree.setPower(wheelThreePower * powerChanger);
                hera.motorFour.setPower(wheelFourPower * powerChanger);
                double increasePower = (Math.abs(error)/100) + 1.1;
                hera.motorOne.setPower(wheelOnePower * increasePower);
                hera.motorTwo.setPower(wheelTwoPower * increasePower);
            }
            if(error > 0 && this.opMode.opModeIsActive()){
                hera.motorThree.setPower(wheelThreePower * powerChanger);
                hera.motorFour.setPower(wheelFourPower * powerChanger);
                double decreasePower = .9 - (error/100);
                hera.motorOne.setPower(wheelOnePower * decreasePower);
                hera.motorTwo.setPower(wheelTwoPower * decreasePower);
            }
            showData("StartPosition", "" + startPosition);
            showData("EndPosition", "" + endPosition);
            showData("CurrentPosition", "" + hera.motorTwo.getCurrentPosition());
            showData("wheel one power", "" + hera.motorOne.getPower());
            showData("wheel two power", "" + hera.motorTwo.getPower());
            showData("wheel three power", "" + hera.motorThree.getPower());
            showData("wheel four power", "" + hera.motorFour.getPower());
            showData("Initial Angle", "" + startingOrientation);
            showData("Current Angle", "" + angles.firstAngle);
            showData("error", "" + error);
            showData("steer", "" + steer);
            this.telemetry.update();
        }
        this.stop();
        this.telemetry.update();
    }


    public void goBackward(double inches, double power) {
        wheelOnePower = power;
        wheelTwoPower = power;
        wheelThreePower = power;
        wheelFourPower = power;
        double startPosition = 0;
        double endPosition = 0;
        showData("DRIVE_TRAIN_CAPTION", "Robot is moving backwards");
        startPosition = hera.motorTwo.getCurrentPosition();
        endPosition = startPosition - (inches * TICKS_IN_AN_INCH); // How far you need to travel
        Orientation angles;
        angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startingOrientation = angles.firstAngle;
        double error = 0;
        double steer = 0;
        showData("SOUP", "SOUP!!!");
        showData("Inches", "" + inches);
        showData("Ticks in an Inch", "" + TICKS_IN_AN_INCH);
        showData("StartPosition", "" + startPosition);
        showData("EndPosition", "" + endPosition);
        showData("CurrentPosition", "" + hera.motorTwo.getCurrentPosition());
        showData("wheel one power", "" + hera.motorOne.getPower());
        showData("wheel two power", "" + hera.motorTwo.getPower());
        showData("wheel three power", "" + hera.motorThree.getPower());
        showData("wheel four power", "" + hera.motorFour.getPower());
        showData("Initial Angle", "" + startingOrientation);
        showData("Current Angle", "" + angles.firstAngle);
        showData("error", "" + error);
        showData("steer", "" + steer);
        while (hera.motorTwo.getCurrentPosition() > endPosition && this.opMode.opModeIsActive()) {
            hera.motorOne.setPower(-wheelOnePower);
            hera.motorTwo.setPower(-wheelTwoPower);
            hera.motorThree.setPower(-wheelThreePower);
            hera.motorFour.setPower(-wheelFourPower);
            angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            error = startingOrientation - angles.firstAngle;
            double powerChanger = (error/100)+1;
            if(error > 0 && this.opMode.opModeIsActive()){
                hera.motorThree.setPower(-wheelThreePower * powerChanger);
                hera.motorFour.setPower(-wheelFourPower * powerChanger);
                double increasePower = (Math.abs(error)/100) + 1;
                hera.motorOne.setPower(-wheelOnePower * increasePower);
                hera.motorTwo.setPower(-wheelTwoPower * increasePower);
            }
            if(error < 0 && this.opMode.opModeIsActive()){
                hera.motorThree.setPower(-wheelThreePower * powerChanger);
                hera.motorFour.setPower(-wheelFourPower * powerChanger);
                double decreasePower = 1 - (error/100);
                hera.motorOne.setPower(-wheelOnePower * decreasePower);
                hera.motorTwo.setPower(-wheelTwoPower * decreasePower);
            }
            showData("StartPosition", "" + startPosition);
            showData("EndPosition", "" + endPosition);
            showData("CurrentPosition", "" + hera.motorThree.getCurrentPosition());
            showData("wheel one power", "" + hera.motorOne.getPower());
            showData("wheel two power", "" + hera.motorTwo.getPower());
            showData("wheel three power", "" + hera.motorThree.getPower());
            showData("wheel four power", "" + hera.motorFour.getPower());
            showData("Initial Angle", "" + startingOrientation);
            showData("Current Angle", "" + angles.firstAngle);
            showData("error", "" + error);
            showData("steer", "" + steer);
        }
        this.stop();
    }

    /**
     * @param degreesToTurn Number of degrees to turn. If negative, turns right. If positive, turns left.
     */
    public void turn(double degreesToTurn, double power) {
        wheelOnePower = power;
        wheelTwoPower = power;
        wheelThreePower = power;
        wheelFourPower = power;
        showData("Turn status: ", "Entered turn method");
        telemetry.addData("Degrees to Turn: ", degreesToTurn);
        Orientation angles;
        angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngleIn360;
        if(angles.firstAngle <=0){
            currentAngleIn360 = angles.firstAngle + 360; //puts starting angle in terms of 360 degree circle (always positive)
        }
        else{
            currentAngleIn360 = angles.firstAngle;
        }
        showData("Turn Info", "current angle: " + currentAngleIn360);
        double targetAngle = currentAngleIn360 + degreesToTurn; // calculates angle the robot is trying to turn to
        if(targetAngle > 360){
            targetAngle -= 360; //keeps target angle within 1-360 degree range
        }
        showData("Turn Info", "target angle: " + targetAngle);
        double degreesLeftToTurn = targetAngle - currentAngleIn360;
        if(degreesLeftToTurn < -180){
            degreesLeftToTurn += 360; // makes sure robot always tries to turn shortest distance between it and the target angle
        }
        else if(degreesLeftToTurn > 180){
            degreesLeftToTurn -=360;
        }
        showData("Turn Info", "Degrees Left to turn: " + degreesLeftToTurn);
        if (degreesLeftToTurn < 0) {
            while ((degreesLeftToTurn <= 0) && this.opMode.opModeIsActive()) {
                double generalPower = (degreesToTurn - angles.firstAngle)/(degreesToTurn);
                if (generalPower < 0.5) {
                    generalPower = 0.5;
                }
                showData("General power: ", ""+ generalPower);
                hera.motorOne.setPower(generalPower * wheelOnePower);
                hera.motorTwo.setPower(generalPower * wheelTwoPower);
                hera.motorThree.setPower(-1 * generalPower * wheelThreePower);
                hera.motorFour.setPower(-1 * generalPower * wheelFourPower);
                showData("wheelPowers: ", "" + (generalPower * wheelOnePower) + ", " + (generalPower * wheelTwoPower)
                                        + ", " + (-1 * generalPower * wheelThreePower)  + ", " +(-1 * generalPower * wheelFourPower));
                angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                if(angles.firstAngle <=0){
                    currentAngleIn360 = angles.firstAngle + 360; //puts current angle in terms of 360 degree circle (always positive)
                }
                else{
                    currentAngleIn360 = angles.firstAngle;
                }
                degreesLeftToTurn = targetAngle - currentAngleIn360;
                if(degreesLeftToTurn < -180){
                    degreesLeftToTurn += 360; // makes sure robot always tries to turn shortest distance between it and the target angle
                }
                else if(degreesLeftToTurn > 180){
                    degreesLeftToTurn -=360;
                }
                String turnInfo = "angles: " + angles.firstAngle + ", " + angles.secondAngle + ", " + angles.thirdAngle;
                showData("Turning", turnInfo);
                showData("Turn Info", "degrees Left To Turn: "+degreesLeftToTurn);
            }
        } else if (degreesLeftToTurn > 0){ // changed the if statement so that it will keep turning back and forth until it reaches the target angle
            while ((degreesLeftToTurn >= 0) && this.opMode.opModeIsActive()) {
                double generalPower = (degreesToTurn - angles.firstAngle)/(degreesToTurn);
                if (generalPower < 0.5) {
                    generalPower = 0.5;
                }
                hera.motorOne.setPower(-1 * generalPower * wheelOnePower);
                hera.motorTwo.setPower(-1 * generalPower * wheelTwoPower);
                hera.motorThree.setPower(generalPower * wheelThreePower);
                hera.motorFour.setPower(generalPower * wheelFourPower);
                angles = hera.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                if(angles.firstAngle <=0){
                    currentAngleIn360 = angles.firstAngle + 360; //puts starting angle in terms of 360 degree circle (always positive)
                }
                else{
                    currentAngleIn360 = angles.firstAngle;
                }
                degreesLeftToTurn = targetAngle - currentAngleIn360;
                if(degreesLeftToTurn < -180){
                    degreesLeftToTurn += 360; // makes sure robot always tries to turn shortest distance between it and the target angle
                }
                else if(degreesLeftToTurn > 180){
                    degreesLeftToTurn -=360;
                }
                showData("DegreesLeftToTurn", degreesLeftToTurn+"");
                String turnInfo = "angles: " + angles.firstAngle + ", " + angles.secondAngle + ", " + angles.thirdAngle;
                showData("Turning", turnInfo);
            }
        }
        this.stop();

    }

    public void goLeft(double inches, double power) {
        wheelOnePower = power;
        wheelTwoPower = power;
        wheelThreePower = power;
        wheelFourPower = power;
        double startPosition = 0;
        double endPosition = 0;
        showData("DRIVE_TRAIN_CAPTION", "Robot is moving left");
        startPosition = hera.motorOne.getCurrentPosition();
        endPosition = startPosition - (inches * SIDE_TICKS_IN_INCH); // How far you need to travel
        while (hera.motorOne.getCurrentPosition() > endPosition && this.opMode.opModeIsActive()) {
            showData("going Left", "End Position: " + endPosition);
            showData("going Left", "Start Pos: " + startPosition);
            showData("going Left", "Current Pos: " + hera.motorOne.getCurrentPosition());

            hera.motorOne.setPower(-wheelOnePower);
            hera.motorTwo.setPower(wheelTwoPower);
            hera.motorThree.setPower(wheelThreePower);
            hera.motorFour.setPower(-wheelFourPower);
        }
        this.stop();
    }

    public void goRight(double inches, double power) {
        wheelOnePower = power;
        wheelTwoPower = power;
        wheelThreePower = power;
        wheelFourPower = power;
        double startPosition = 0;
        double endPosition = 0;
        showData("DRIVE_TRAIN_CAPTION", "Robot is moving right");
        startPosition = hera.motorOne.getCurrentPosition();
        endPosition = startPosition + (inches * SIDE_TICKS_IN_INCH); // How far you need to travel
        while (hera.motorOne.getCurrentPosition() < endPosition && this.opMode.opModeIsActive()) {
            showData("goRight", "start position is " + startPosition);
            showData("goRight", "end position is " + endPosition);
            showData("goRight", "current position is " + hera.motorFour.getCurrentPosition());
            hera.motorOne.setPower(wheelOnePower);
            hera.motorTwo.setPower(-wheelTwoPower);
            hera.motorThree.setPower(-wheelThreePower);
            hera.motorFour.setPower(wheelFourPower);
        }
        this.stop();
    }

    public void stop() {
        showData("DRIVE_TRAIN_CAPTION", "Stopping the drive train");
        this.telemetry.addData("wheel power", hera.motorOne.getPower());
        this.telemetry.update();
        hera.motorOne.setPower(0);
        hera.motorTwo.setPower(0);
        hera.motorThree.setPower(0);
        hera.motorFour.setPower(0);
        this.telemetry.addData(DRIVE_TRAIN_CAPTION, "Drive train is stopped");
        this.telemetry.addData("wheel power", hera.motorOne.getPower());
        this.telemetry.update();
    }

    public void teleopUpdate(Gamepad gamepad1, Gamepad gamepad2) {

        showData("DRIVE_TRAIN_CAPTION", "Teleop updated");
        telemetry.addData("1_left_stick_x", gamepad1.left_stick_x);
        telemetry.addData("1_left_stick_y", gamepad1.left_stick_y);
        telemetry.addData("1_right_stick_x", gamepad1.right_stick_x);
        telemetry.addData("1_right_stick_y", gamepad1.right_stick_y);

        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * 1.5; // 1.5 is to counteract imperfect strafing
        double rx = gamepad1.right_stick_x;
        double directionalMultiplier = 1;

        showData("rx value","" + rx);

        hera.motorOne.setPower(directionalMultiplier * (y + x + rx));
        hera.motorTwo.setPower(directionalMultiplier * (y - x + rx));
        hera.motorThree.setPower(directionalMultiplier * (y - x - rx));
        hera.motorFour.setPower(directionalMultiplier * (y + x - rx));

        if (gamepad1.dpad_left) {
            directionalMultiplier = -1;
        }

        if (gamepad1.dpad_right) {
            directionalMultiplier = 1;
        }

    }

    // Print data to both telemetry and log
    public void showData(String caption, String value) {
        this.telemetry.addData(caption, value);
        this.telemetry.update();
        Log.d(caption, value);
    }

}
