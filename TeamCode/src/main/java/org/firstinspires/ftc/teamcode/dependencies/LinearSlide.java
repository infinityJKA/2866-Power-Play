package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.enums.Levels;

public class LinearSlide {
    private RobotParameters rP;
    private DcMotor linearSlideMotor;
    private Servo claw;
    private LinearOpMode linearOp;
    public LinearPosition currentLinearPosition = LinearPosition.ZERO;

    // math.pi*2
    // Heights for da thingies: 37 in, 25 in, 17 in

//    private final int t = 1440;
//    private final double MIN_POS = 0, MAX_POS = 1;private final int t = 1440;
    private final double MIN_POS = 0, MAX_POS = 1;

    public enum LinearPosition {
        ZERO(0), ONE(3500), TWO(5650), THREE(8400), CONE1(200), CONE2(400), CONE3(600);
        private final int ticks;
        LinearPosition(int i){this.ticks = i;}
    }

    public LinearSlide(DcMotor linearSlideMotor, Servo claw, LinearOpMode linearOp){
        this.linearSlideMotor = linearSlideMotor;
        this.claw = claw;
        this.linearOp = linearOp;
        if (linearSlideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            linearSlideMotor.setTargetPosition(0);
            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlideMotor.setDirection(DcMotor.Direction.REVERSE);

        }
    }
    public void placeCone(LinearPosition pos, double power){
        moveToPosition(pos, power);
        openClaw();
    }
    public void pickupCone(double power){
        moveToPosition(LinearPosition.ZERO, power);
        closeClaw();
    }
    public void pickupCone(LinearPosition pos, double power){
        moveToPosition(pos, power);
        closeClaw();
    }
    public void moveToPosition(LinearPosition pos, double power){
        if (!currentLinearPosition.equals(pos)){
            currentLinearPosition = pos;
            if (linearSlideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                linearSlideMotor.setTargetPosition(0);
                linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            linearSlideMotor.setTargetPosition(pos.ticks);
            linearSlideMotor.setPower(power);

            while (linearSlideMotor.isBusy() && Math.abs(linearSlideMotor.getTargetPosition() - linearSlideMotor.getCurrentPosition()) > 10){
                Thread.yield();
            }
            linearSlideMotor.setPower(0);
        }
    }
    public void openClaw(){
        changeClawPos(0);
    }
    public void closeClaw(){
        changeClawPos(1);
    }
    public void changeClawPos(double position){
        this.claw.setPosition(Range.clip(position, MIN_POS, MAX_POS));
    }
    public void setPower(double power){
        linearSlideMotor.setPower(power);
        currentLinearPosition = null;
    }
}
