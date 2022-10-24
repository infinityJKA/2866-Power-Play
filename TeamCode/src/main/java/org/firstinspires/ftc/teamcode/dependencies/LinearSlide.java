package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.teamcode.enums.Levels;

public class LinearSlide {
    private RobotParameters rP;
    private DcMotor linearSlideMotor;
    private Servo claw;
    public Levels level;

    public LinearSlide(DcMotor linearSlideMotor, Servo claw){
        this.linearSlideMotor = linearSlideMotor;
        this.claw = claw;
    }
}