package org.firstinspires.ftc.teamcode.autonomi;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class runGoofyJustinColorTest extends OpMode {

    OpenCvWebcam webcam1 = null;


    public void init(){
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "LiveLeak");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvWebcam webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new justinsGoofyPipeline());

        webcam1.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    public void onOpened() {
                        webcam1.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
                    }

                    public void onError(int errorCode) {
                    }
                }
        );
    }

    @Override   // No idea why @Override is giving an error. (as of Nov. 3, 2023) - Justin   EDIT NOV 7, LMAO I JUST COMMENTED IT OUT RIP BOZO
    public void loop(){
        // Hi, this is an empty loop.
        // Hi this is an empty loop, I'm Justin.
    }

    private class justinsGoofyPipeline extends OpenCvPipeline {

        Mat YCbCr = new Mat(); // Note: I have no idea what a "YCbCr" means.
        Mat leftCrop; // Left side of the screen thingy
        Mat rightCrop; // The right part of the camera screen thing
        double leftavgfin; // idk man
        double rightavgfin; // ^same! so true!
        Mat outPut = new Mat(); // The word "Put" reminds me of golf, got the P capitalized for some reason, probably some deep allegory from something idk.
        Scalar rectColor = new Scalar(225.0, 0.0, 0.0); // "Scalar" is such a funny word.

        public Mat processFrame(Mat input){

            Imgproc.cvtColor(input,YCbCr,Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("The wrath of \"JustinsWackyColorTest.java\" dawns upon us.");

            Rect leftRect = new Rect(1,1,319,359);
            Rect rightRect = new Rect(320,1,319,359);

            input.copyTo(outPut);
            Imgproc.rectangle(outPut, leftRect, rectColor, 2);
            Imgproc.rectangle(outPut, rightRect, rectColor, 2);

            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rightavg = Core.mean(rightCrop);

            leftavgfin = leftavg.val[0];
            rightavgfin = rightavg.val[0];

            if (leftavgfin > rightavgfin){
                telemetry.addLine("LEFT!!!!!! GOOOOO LEEEEEEFT!!!!!");
            }
            else{
                telemetry.addLine("I said right foot creep hit the griddy yuh");
            }

            return(outPut);
        }
    }
}
