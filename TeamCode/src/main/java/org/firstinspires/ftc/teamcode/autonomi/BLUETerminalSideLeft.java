package org.firstinspires.ftc.teamcode.autonomi;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.dependencies.Terminal;
import org.firstinspires.ftc.teamcode.enums.AllianceSide;
import org.firstinspires.ftc.teamcode.enums.ColorSide;


@Autonomous(name="BLUETerminalSideLeft", group="Autonomous")

public class BLUETerminalSideLeft extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException{
        Terminal.run(this, AllianceSide.LEFT, ColorSide.BLUE);
    }
}