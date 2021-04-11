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
    double defaultPower = 0.3;

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

    public void teleop(Gamepad gamepad1, Gamepad gamepad2){
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
        if (this.opMode.opModeIsActive()) {
            /*
            driveTrain.turn(90);
            driveTrain.goForward(12);
            driveTrain.turn(-90);
            */
            driveTrain.goForward(18, 0.3);
        }
    }

    public void wobbleTester(){
        if(this.opMode.opModeIsActive()){
            double armStartPos = this.hwmap.wobbleArm.getCurrentPosition();
            double armEndPos = armStartPos + 330;
            while(this.hwmap.wobbleArm.getCurrentPosition() < armEndPos && this.opMode.opModeIsActive()){
                this.hwmap.wobbleArm.setPower(0.2);
                showData("wobble arm position", "" + this.hwmap.wobbleArm.getCurrentPosition());
            }
            this.hwmap.wobbleArm.setPower(0);
            this.wobble.release();
            this.opMode.sleep(1000);
            this.driveTrain.turn(45, 0.2);
            this.opMode.sleep(1000);
            this.wobble.grab();
            this.opMode.sleep(200);
            this.driveTrain.turn(-45, 0.2);
            while(this.hwmap.wobbleArm.getCurrentPosition() > armStartPos){
                this.hwmap.wobbleArm.setPower(-0.2);
            }
            this.hwmap.wobbleArm.setPower(0);

        }
    }

    public void rightRedLineAuto(){
        if(this.opMode.opModeIsActive()){
            position = openCV.getRingNumber();
            this.shooter.startShooter();
            showData("Now starting... ", "to go backwards!");
            this.driveTrain.goBackward(18+24, 0.3);
            this.opMode.sleep(100);
            showData("Now starting...", "to turn!");
            this.driveTrain.turn(15, 0.3);
            this.opMode.sleep(300);
            showData("Now starting... ", "to shoot!");
            this.shooter.shoot();
            showData("Now starting... ", "the conveyor!");
            this.opMode.sleep(500);
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 2000;
            while((!this.hwmap.ringTouchSensor.isPressed() && System.currentTimeMillis()<endTime)&&this.opMode.opModeIsActive()) {
                showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
                showData("CurrentTimeinMillis", "" + System.currentTimeMillis());
                showData("EndTime", "" + endTime);
                this.hwmap.conveyerMotor.setPower(-.6);
            }
            showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
            showData("Now starting... ", "to shoot ring #2!");
            shooter.shoot();

            this.opMode.sleep(1000);
            showData("Now starting... ", "to move that intake!");
            this.hwmap.intakeMotor.setPower(.4);
            this.opMode.sleep(1000);
            showData("Now starting... ", "to shoot ring ring #3!");
            shooter.shoot();

            this.hwmap.intakeMotor.setPower(0);
            this.hwmap.conveyerMotor.setPower(0);
            this.opMode.sleep(750);
            this.driveTrain.turn(-15, 0.3);
            if(position == RingPosition.ONE){ // drive to 2nd square
                this.driveTrain.goBackward(58, 0.3);
                this.opMode.sleep(100);
                this.driveTrain.turn(90, 0.3);
                this.opMode.sleep(100);
                this.driveTrain.goBackward(6, 0.3);
                this.opMode.sleep(100);
                this.wobble.drop();
                this.opMode.sleep(100);
                this.driveTrain.goRight(6, 0.3);
            }
            else if(position == RingPosition.FOUR){
                this.driveTrain.goBackward(108, 0.3);
                this.opMode.sleep(500);
                this.wobble.drop();
                this.opMode.sleep(500);
                this.driveTrain.goForward(45, 0.3);
            }
            else{
                this.driveTrain.goBackward(40, 0.3);
                this.opMode.sleep(500);
                this.wobble.drop();
                this.opMode.sleep(500);
                this.driveTrain.goRight(3, 0.3);
            }
        }
    }

    public void twoWobbleTester(){
        if(this.opMode.opModeIsActive()) {
            position = openCV.getRingNumber();
            this.shooter.startShooter();
            this.driveTrain.goBackward(6, 0.3);
            this.opMode.sleep(200);
            this.driveTrain.turn(-10, 0.3);
            this.driveTrain.goBackward(18 + 24, 0.3);
            this.opMode.sleep(100);
            this.driveTrain.turn(15, 0.3);
            this.opMode.sleep(200);
            this.shooter.shoot();
            //this.opMode.sleep(500);
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 2000;
            while ((!this.hwmap.ringTouchSensor.isPressed() && System.currentTimeMillis() < endTime) && this.opMode.opModeIsActive()) {
                showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
                showData("CurrentTimeinMillis", "" + System.currentTimeMillis());
                showData("EndTime", "" + endTime);
                this.hwmap.conveyerMotor.setPower(-.6);
            } //conveyor for 2nd shot
            showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
            shooter.shoot(); // 2nd shot

            this.opMode.sleep(1000);
            showData("Now starting... ", "to move that intake!");
            this.hwmap.intakeMotor.setPower(.4);
            this.opMode.sleep(2000);
            showData("Now starting... ", "to shoot ring ring #3!");
            shooter.shoot();

            this.hwmap.intakeMotor.setPower(0);
            this.hwmap.conveyerMotor.setPower(0);
            //this.opMode.sleep(750);
            this.driveTrain.turn(-15, 0.3);
            //this.opMode.sleep(500);adb
            this.driveTrain.goBackward(48, 0.3); //to 1st box
            this.opMode.sleep(100);
            this.wobble.drop(); //drop 1st wobble
            this.driveTrain.goRight(3, 0.3);
            this.opMode.sleep(200);
            this.driveTrain.turn(155, 0.2);
            this.driveTrain.goBackward(68, 0.3); //back to 2nd wobble
            this.wobble.release();
            double armStartPos = this.hwmap.wobbleArm.getCurrentPosition();
            double armEndPos = armStartPos + 330;
            while(this.hwmap.wobbleArm.getCurrentPosition() < armEndPos && this.opMode.opModeIsActive()){
                this.hwmap.wobbleArm.setPower(0.2);
                showData("wobble arm position", "" + this.hwmap.wobbleArm.getCurrentPosition());
            } //lower wobble arm
            this.hwmap.wobbleArm.setPower(0);
            //this.opMode.sleep(1000);
            this.driveTrain.turn(30, 0.3);
            //this.opMode.sleep(1000);
            this.wobble.grab(); //grab 2nd wobble
            //this.opMode.sleep(200);
            this.driveTrain.turn(-40, 0.3);
            //this.opMode.sleep(200);
            this.driveTrain.turn(180, 0.3);
            this.driveTrain.goLeft(4, 0.3);
            this.driveTrain.goBackward(60,3);
            this.wobble.release(); //drop 2nd wobble
            this.driveTrain.goRight(18, 0.3); //park on line
        }
    }
    public void redAutoShoot() {

        if (this.opMode.opModeIsActive()) {
            position = openCV.getRingNumber();
            this.autoShoot();
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 2000;
            while((!this.hwmap.ringTouchSensor.isPressed() && System.currentTimeMillis()<endTime)&&this.opMode.opModeIsActive()) {
                showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
                showData("CurrentTimeinMillis", "" + System.currentTimeMillis());
                showData("EndTime", "" + endTime);
                this.hwmap.conveyerMotor.setPower(-.6);
            }
            showData("touch sensor status", "" + this.hwmap.ringTouchSensor.isPressed());
            this.opMode.sleep(500);
            this.driveTrain.turn(-3, defaultPower);
            this.opMode.sleep(1000);
            shooter.shoot();

            this.hwmap.intakeMotor.setPower(.4);
            this.opMode.sleep(1000);
            shooter.shoot();

            this.hwmap.intakeMotor.setPower(0);
            this.hwmap.conveyerMotor.setPower(0);
            this.opMode.sleep(750);
            this.driveTrain.turn(-5, defaultPower);
            showData("Step Eight: ", "" + position);
            if (position == RingPosition.ONE) {
                // Drive to 2nd square
                driveTrain.turn(-20, defaultPower);
                showData("Step Nine a ", "take it back now ya'll");
                driveTrain.goBackward(6 * 12, defaultPower);
                showData("Step Ten a ", "drop it like it's hot");
                this.opMode.sleep(1500);
                wobble.drop();
                this.opMode.sleep(1000);
                // Park over line
                showData("Step Eleven a", "Jeevan goes forward 28 inches");
                driveTrain.goForward(6, defaultPower);
                showData("Step Twelve a", "wobble dropper RESET whoop");
                wobble.reset();
                showData("Ring Position", "One");
            } else if (position == RingPosition.FOUR) {
                // Drive to 3rd square
                showData("Step Nine b", "Take it back now ya'll");
                driveTrain.turn(-15, defaultPower);
                driveTrain.goBackward(9.5 * 12, defaultPower);
                showData("Step Ten b", "We be turning!(90 degrees to be exact)");
                driveTrain.turn(90, defaultPower);
                showData("Step Eleven b", "goin back to the box!");
                driveTrain.goForward(30, defaultPower);
                showData("Step Twelve b", "drop it like it's hot 2 electric boogaloo");
                this.opMode.sleep(1500);
                wobble.drop();
                this.opMode.sleep(1000);                // Park over line
                showData("Step Thirteen b", "Turn right 90 degrees");
                driveTrain.turn(90, defaultPower);
                showData("Step Fourteen b", "Go forward to park on line");
                driveTrain.goForward(52, defaultPower);
                wobble.reset();
                showData("Ring Position", "Four");

            } else {
                // Drive to 1st square
                driveTrain.goForward(5, defaultPower);
                driveTrain.goRight(5, defaultPower);
                this.driveTrain.goBackward(74, defaultPower);
                this.opMode.sleep(1500);
                driveTrain.goLeft(3, defaultPower);
                this.opMode.sleep(1500);
                wobble.drop();
                this.opMode.sleep(1000);
                // Park over line
                wobble.reset();
                showData("Ring Position", "None");
            }
        }
    }
    public void autoShoot(){
        showData("Step One: ", "Start shooter motor");
        this.shooter.startShooter();
        showData("Step Three: ", "Go backwards");
        driveTrain.goBackward(3, defaultPower);
        showData("Step Four: ", "Turn 7 degrees");
        driveTrain.turn(17, defaultPower);
        showData("Step Five: ", "SHOOT");
        this.opMode.sleep(2000);
        shooter.shoot();
        showData("Step Six: ", "Beddy Bye");
        this.opMode.sleep(2000);
        showData("Step Seven: ", "Turn -7 degrees");
        //driveTrain.turn(-10, defaultPower);
    }

    public void redAutoShootAndPark() {
        if (this.opMode.opModeIsActive()) {
            this.autoShoot();
            driveTrain.goForward(5, defaultPower);
            driveTrain.goRight(5, defaultPower);
            this.opMode.sleep(1000);
            this.driveTrain.goBackward(74, defaultPower);
            wobble.drop();

        }
    }

    public void redAutoNoShoot() {
        if (this.opMode.opModeIsActive()) {
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

    public void parkOnLine() {
        driveTrain.goForward(7 * 12, defaultPower);
    }


    public void showData(String caption, String value) {
        this.telemetry.addData(caption, value);
        this.telemetry.update();
        Log.d(caption, value);
    }

    public void shootThree() {

    }

}
