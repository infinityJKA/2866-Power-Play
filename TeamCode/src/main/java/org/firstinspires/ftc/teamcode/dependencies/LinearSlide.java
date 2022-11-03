package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.enums.Levels;

public class LinearSlide {
    private RobotParameters rP;
    private DcMotor linearSlideMotor;
    private Servo claw;
    public Levels level;
    private LinearOpMode linearOp;

    public enum LinearPosition {
        ZERO(0), ONE(300), TWO(600), THREE(900);
        public int ticks;
        LinearPosition(int i){this.ticks = i;}
    }

    public LinearSlide(DcMotor linearSlideMotor, Servo claw, LinearOpMode linearOp){
        this.linearSlideMotor = linearSlideMotor;
        this.claw = claw;
        this.linearOp = linearOp;
        if (linearSlideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            linearSlideMotor.setTargetPosition(0);
            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void moveToPosition(LinearPosition pos, double power, int ticks){
        if (linearSlideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            linearSlideMotor.setTargetPosition(0);
            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        linearSlideMotor.setTargetPosition(ticks);
        linearSlideMotor.setPower(power);

        while (linearSlideMotor.isBusy() && Math.abs(linearSlideMotor.getTargetPosition() - linearSlideMotor.getCurrentPosition()) > 10){
            Thread.yield();
        }
        linearSlideMotor.setPower(0);
    }
}