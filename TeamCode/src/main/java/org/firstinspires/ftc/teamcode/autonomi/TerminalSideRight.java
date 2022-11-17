package org.firstinspires.ftc.teamcode.autonomi;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.dependencies.Terminal;
import org.firstinspires.ftc.teamcode.enums.AllianceSide;
import org.firstinspires.ftc.teamcode.enums.ColorSide;


@Autonomous(name="TerminalSideRight", group="Autonomous")

public class TerminalSideRight extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException{
        Terminal.run(this, AllianceSide.RIGHT, ColorSide.BLUE);
    }
}