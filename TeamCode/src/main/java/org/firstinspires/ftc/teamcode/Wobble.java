package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Wobble {

    String WOBBLE_CAPTION = "Wobble Status";
    Telemetry telemetry;
    HardwareInnov8Hera hera;
    LinearOpMode opMode;

    public double wobbleDown = 0;
    public double wobbleUp = 1;


    public Wobble(Telemetry telemetry, HardwareInnov8Hera hera, LinearOpMode opMode) {

        this.opMode = opMode;
        this.hera = hera;
        this.telemetry = telemetry;
    }

    public void drop() {
        this.hera.wobbleDropper.setPosition(wobbleUp);
    }
}
