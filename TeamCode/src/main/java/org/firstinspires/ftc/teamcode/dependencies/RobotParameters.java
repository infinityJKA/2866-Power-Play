package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RobotParameters {
    private BNO055IMU imu;
    public DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, linearSlide;
    public int ticks;
    public double wheelRadius, driveGearRatio, wheelCircumference, wheelBaseCircumference, wheelBaseRadius;

    public RobotParameters(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, int t, double wheelRadius, double driveGearRatio, DcMotor LS, BNO055IMU imu, double wheelBaseRadius){
        this.frontRightMotor = fr;
        this.backRightMotor = br;
        this.frontLeftMotor = fl;
        this.backLeftMotor = bl;
        this.ticks = t;
        this.wheelRadius = wheelRadius;
        this.wheelCircumference = Math.PI*2*wheelRadius;
        this.imu = imu;
        this.linearSlide = LS;
        this.wheelBaseRadius = wheelBaseRadius;
        this.wheelBaseCircumference = (2*Math.PI*wheelBaseRadius);
    }

    public RobotParameters(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, int t, double wheelRadius, double driveGearRatio, DcMotor LS, double wheelBaseRadius){
        this.frontRightMotor = fr;
        this.backRightMotor = br;
        this.frontLeftMotor = fl;
        this.backLeftMotor = bl;
        this.ticks = t;
        this.wheelRadius = wheelRadius;
        this.wheelCircumference = Math.PI*2*wheelRadius;
        this.linearSlide = LS;
        this.wheelBaseRadius = wheelBaseRadius;
    }



}
