package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "RightRedLineAuto", group = "Robot")
public class RightRedLineAuto extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        Hera hera = new Hera(telemetry, hardwareMap, this);
        waitForStart();
        hera.rightRedLineAuto();

    }
}