package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.enums.*;

// https://ftcsim.org/ftcsim/ad
public class MecanumEncoder {
    private RobotParameters rP;
    private LinearOpMode linearOpMode;
    private BNO055IMU imu;
    private DcMotor.RunMode frontLeftMode, frontRightMode, backLeftMode, backRightMode;
    private static final double COS_135 = Math.cos(3 * Math.PI / 4);
    private static final double SIN_135 = -COS_135;
    private static final double DEG_45 = Math.PI / 4;
    public static final int TARGET_REACHED_THRESHOLD = 16;

    public MecanumEncoder(RobotParameters robotParameters, LinearOpMode linearOpMode){
        this.rP = robotParameters;
        this.linearOpMode = linearOpMode;
        rP.frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        rP.backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public void moveInches(Direction direction, double distance, double power){
        storeMotorModes();
        resetEncoders();
        int ticksToTravel = (int) Math.round(inchesToTicks(distance));
        int frontLeftTicks = 0;
        int frontRightTicks = 0;
        int backLeftTicks = 0;
        int backRightTicks = 0;

        switch (direction){
            case FORWARD:
                frontLeftTicks = ticksToTravel;
                frontRightTicks = ticksToTravel;
                backLeftTicks = ticksToTravel;
                backRightTicks = ticksToTravel;
                break;
            case BACKWARD:
                frontLeftTicks = -ticksToTravel;
                frontRightTicks = -ticksToTravel;
                backLeftTicks = -ticksToTravel;
                backRightTicks = -ticksToTravel;
                break;
            case RIGHT:
                frontLeftTicks = ticksToTravel;
                frontRightTicks = -ticksToTravel;
                backLeftTicks = -ticksToTravel;
                backRightTicks = ticksToTravel;
                break;
            case LEFT:
                frontLeftTicks = -ticksToTravel;
                frontRightTicks = ticksToTravel;
                backLeftTicks = ticksToTravel;
                backRightTicks = -ticksToTravel;
                break;
            default:
                throw new IllegalArgumentException("Direction must be FORWARD, BACKWARD, LEFT, or RIGHT");

        }
        setMotorTargets(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks);;

        runToPostions();
        setMotorPowers(power);

        while (areMotorsBusy() || !motorsReachedTarget(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks) && linearOpMode.opModeIsActive()){
            Thread.yield();
        }
        resetMotorPowers();
        restoreMotorModes();

        sleep(100);
    }
    public void rotateDegrees(Direction rotating, double degrees, double speed){
        storeMotorModes();
        resetEncoders();

        int frontLeftTicks = 0;
        int frontRightTicks = 0;
        int backLeftTicks = 0;
        int backRightTicks = 0;

        double inchesToTravel = degreesToInches(degrees);
        int ticksToTravel = (int) Math.round(inchesToTicks(inchesToTravel));

        switch (rotating){
            case CW: // clockwise
                frontLeftTicks = ticksToTravel;
                frontRightTicks = -ticksToTravel;
                backLeftTicks = ticksToTravel;
                backRightTicks = -ticksToTravel;
                break;
            case CCW:  // counter-clockwise
                frontLeftTicks = -ticksToTravel;
                frontRightTicks = ticksToTravel;
                backLeftTicks = -ticksToTravel;
                backRightTicks = ticksToTravel;
                break;
            default:
                throw new IllegalArgumentException("Direction must be CW, CCW");
        }

        setMotorTargets(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks);;

        runToPostions();
        setMotorPowers(speed);

        while (areMotorsBusy() || !motorsReachedTarget(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks) && linearOpMode.opModeIsActive()){
            Thread.yield();
        }
        resetMotorPowers();
        restoreMotorModes();
        sleep(100);
    }
    public void strafe(Direction strafing, double distance, double speed){
        storeMotorModes();
        resetEncoders();

        int frontLeftTicks = 0;
        int frontRightTicks = 0;
        int backLeftTicks = 0;
        int backRightTicks = 0;
        double frontLeftPower, frontRightPower, backLeftPower, backRightPower;
        frontLeftPower = frontRightPower = backLeftPower = backRightPower = 0;
        int ticksToTravel = (int) Math.round(inchesToTicks(distance));

        switch (strafing) {
            case FORWARD:
                break;
            case BACKWARD:
                break;
            case RIGHT:
                break;
            case LEFT:
                break;
            case CCW:
                break;
            case CW:
                break;
            case F_LEFT:
                frontRightTicks = ticksToTravel;
                backLeftTicks = ticksToTravel;
                // frontLeftPower = speed; // activate this line if using line 166 instead of 165
                // backRightPower = speed; // activate this line if using line 166 instead of 165
                break;
            case F_RIGHT:
                frontLeftTicks = ticksToTravel;
                backRightTicks = ticksToTravel;
                // frontRightPower = speed; // activate this line if using line 170 instead of 169
                // backLeftPower = speed; // activate this line if using line 170 instead of 169
                break;
            case B_LEFT:
                frontRightTicks = -ticksToTravel;
                backLeftTicks = -ticksToTravel;
                // frontLeftPower = speed; // activate this line if using line 170 instead of 169
                // backRightPower = speed; // activate this line if using line 170 instead of 169
                break;
            case B_RIGHT:
                frontLeftTicks = -ticksToTravel;
                backRightTicks = -ticksToTravel;
                // frontRightPower = speed; // activate this line if using line 170 instead of 169
                // backLeftPower = speed; // activate this line if using line 170 instead of 169
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strafing);
        }

        setMotorTargets(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks);

        runToPostions();
        setMotorPowers(speed);
        // setMotorPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower); // Test this if the line above does not work

        while (areMotorsBusy() || !motorsReachedTarget(frontLeftTicks, frontRightTicks, backLeftTicks, backRightTicks) && linearOpMode.opModeIsActive()){
            Thread.yield();
        }
        resetMotorPowers();
        restoreMotorModes();
        sleep(100);
    }
    public void restoreMotorModes(){
        rP.frontLeftMotor.setMode(frontLeftMode);
        rP.frontRightMotor.setMode(frontRightMode);
        rP.backLeftMotor.setMode(backLeftMode);
        rP.backRightMotor.setMode(backRightMode);
    }
    public void storeMotorModes(){
        frontLeftMode = rP.frontLeftMotor.getMode();
        frontRightMode = rP.frontRightMotor.getMode();
        backLeftMode = rP.backLeftMotor.getMode();
        backRightMode = rP.backRightMotor.getMode();
    }
    public double degreesToInches(double degrees){
        return (this.rP.wheelBaseCircumference) * (degrees/360);
    }
    public int inchesToTicks(double inches){
        return (int)((inches / this.rP.wheelCircumference) * this.rP.ticks);
    }

    public void resetEncoders(){
        this.rP.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rP.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rP.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rP.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runToPostions(){
        if (this.rP.frontLeftMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            this.rP.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (this.rP.frontRightMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            this.rP.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (this.rP.backLeftMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            this.rP.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (this.rP.backRightMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            this.rP.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetMotorPowers() {this.setMotorPowers(0);}

    public boolean areMotorsBusy(){return this.rP.frontLeftMotor.isBusy() || this.rP.frontRightMotor.isBusy() || this.rP.backLeftMotor.isBusy() || this.rP.backRightMotor.isBusy();}

    protected boolean motorsReachedTarget(int frontLeftTarget, int frontRightTarget, int backLeftTarget, int backRightTarget) {
        return reachedTarget(rP.frontLeftMotor.getCurrentPosition(), frontLeftTarget, rP.frontRightMotor.getCurrentPosition(), frontRightTarget) &&
                reachedTarget(rP.backLeftMotor.getCurrentPosition(), backLeftTarget, rP.backRightMotor.getCurrentPosition(), backRightTarget);
    }

    protected boolean reachedTarget(int currentPositionLeft, int targetPositionLeft, int currentPositionRight, int targetPositionRight) {
        return Math.abs(currentPositionLeft - targetPositionLeft) < TARGET_REACHED_THRESHOLD && Math.abs(currentPositionRight - targetPositionRight) < TARGET_REACHED_THRESHOLD;
    }


    protected void setMotorPowers(double power){
        this.rP.frontLeftMotor.setPower(power);
        this.rP.frontRightMotor.setPower(power);
        this.rP.backLeftMotor.setPower(power);
        this.rP.backRightMotor.setPower(power);
    }
    protected void setMotorPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower){
        this.rP.frontLeftMotor.setPower(frontLeftPower);
        this.rP.frontRightMotor.setPower(frontRightPower);
        this.rP.backLeftMotor.setPower(backLeftPower);
        this.rP.backRightMotor.setPower(backRightPower);
    }

    protected void setMotorTargets(final int targetPostitionLeft, final int targetPostitionRight){setMotorTargets(targetPostitionLeft, targetPostitionRight, targetPostitionLeft, targetPostitionRight);}

    public void sleep(long milli){linearOpMode.sleep(milli);}

    protected void setMotorTargets(int targetPostitionLeft, int targetPostitionRight, int targetPositionBackLeft, int targetPositionBackRight){
        this.rP.frontLeftMotor.setTargetPosition(targetPostitionLeft);
        this.rP.frontRightMotor.setTargetPosition(targetPostitionRight);
        this.rP.backLeftMotor.setTargetPosition(targetPositionBackLeft);
        this.rP.backRightMotor.setTargetPosition(targetPositionBackRight);
    }
}