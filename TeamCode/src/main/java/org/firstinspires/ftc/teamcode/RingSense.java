package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "RingSense", group = "Robot")
public class RingSense extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        Hera hera = new Hera(telemetry, hardwareMap, this);
        waitForStart();
        hera.ringSense();

    }
}
