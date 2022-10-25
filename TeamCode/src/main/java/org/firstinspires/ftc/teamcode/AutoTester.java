package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.dependencies.*;
import org.firstinspires.ftc.teamcode.enums.Direction;

@Autonomous(name = "AutoTester", group = "Autonomous")

public class AutoTester extends LinearOpMode{
    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, linearSlide;
    private RobotParameters rP;
    private BNO055IMU imu;
    private Direction direction, rotation;
    private MecanumEncoder mecanumEncoder;

    public void runOpMode(){
        frontLeftMotor = hardwareMap.get(DcMotor.class, "left");
        frontRightMotor = hardwareMap.get(DcMotor.class, "right");
        backLeftMotor = hardwareMap.get(DcMotor.class, "bLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "bRight");
        linearSlide = hardwareMap.get(DcMotor.class, "ls");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        this.rP = new RobotParameters(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, 1440, 2.6, 60, linearSlide, imu, 12.5);
        MecanumEncoder mecanumEncoder = new MecanumEncoder(this.rP, this);
//hi
        waitForStart();
        if (opModeIsActive()){
            mecanumEncoder.moveInches(direction.FORWARD, 25, 1);
            mecanumEncoder.rotateDegrees(rotation.CW, 90, 1);
            mecanumEncoder.moveInches(direction.FORWARD, 25, 1);
        }

    }public void points5(){
        //kick cone and park
        mecanumEncoder.moveInches(direction.FORWARD, 24, 1);
        mecanumEncoder.rotateDegrees(rotation.CW, 45, 1);
        mecanumEncoder.moveInches(direction.FORWARD, 10, 1);
        mecanumEncoder.moveInches(direction.BACKWARD, 10, 1);
        mecanumEncoder.rotateDegrees(rotation.CCW, 45, 1);
        mecanumEncoder.moveInches(direction.BACKWARD, 1212, 1);
        mecanumEncoder.rotateDegrees(rotation.CCW, 45, 1);
        mecanumEncoder.moveInches(direction.BACKWARD, 20, 1);

        //kick cone, drop cone, and park
        mecanumEncoder.moveInches(direction.FORWARD, 24, 1);
        mecanumEncoder.rotateDegrees(rotation.CW, 45, 1);
        mecanumEncoder.moveInches(direction.FORWARD, 10, 1);
        mecanumEncoder.rotateDegrees(rotation.CW, 45, 1);
        mecanumEncoder.moveInches(direction.FORWARD, 12, 1);
        place();
        mecanumEncoder.rotateDegrees(rotation.CW, 60, 1);
        mecanumEncoder.moveInches(direction.FORWARD, 12, 1);
        mecanumEncoder.rotateDegrees(rotation.CCW, 90, 1);
        mecanumEncoder.moveInches(direction.FORWARD, 30, 1);



    }
    public void place(){
        
    }
}
