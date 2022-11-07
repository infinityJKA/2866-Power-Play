package org.firstinspires.ftc.teamcode.autonomi;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.dependencies.Terminal;
import org.firstinspires.ftc.teamcode.enums.AllianceSide;


@Autonomous(name="TerminalSideLeft", group="Autonomous")

public class TerminalSideLeft extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException{
        Terminal.run(this, AllianceSide.LEFT);
    }
}