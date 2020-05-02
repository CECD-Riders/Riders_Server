package me.soungho.demo;

import javafx.util.Pair;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

@Component
public class ImageTextDetector {

//    @Autowired
//    NameConverter nameConverter;

    @Autowired
    OCRValidator ocrValidator;

    //프레임 하나를 받아 자막 텍스트를 저장
    public Rect DetectTextAndSave(BufferedImage img, double second ,int cnt){
        Mat src;
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        try{
            src = img2Mat(img);
            if(src.empty()){
                System.out.println("존재하지 않거나 지원하지 않는 소스!");
                return null;
            }
            else
                System.out.println("<================시간: " + second + "================>");

            Mat erosion = ProcessCandidateArea(src); //영상 분석 기법 적용해 자막 후보군 추리기

            //erosion에서 contour를 찾고 원본 이미지에다가 찾은 contour를 그리자
            int i = 0;
            Imgproc.findContours(erosion,contours,hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE,new org.opencv.core.Point(0,0));
            if(contours.size() >0){
                for(int idx = 0;idx <contours.size();idx++){
                    Rect rect = Imgproc.boundingRect(contours.get(idx));
                    if(rect.width > rect.height && rect.area() > 3000) {
                        //여기서 그리는 영역을 tesseract한테 먹여서 문자 리턴값을 확인하기
                        rectangle(src, new Point(rect.br().x - rect.width, rect.br().y - rect.height)
                                , rect.br()
                                , new Scalar(0, 255, 0), 5);

                        //그리는 영역을 잘라서 tesseract의 input으로 먹이자
                        String ocrStr = PreProcessAndDoOCR(img.getSubimage(rect.x,rect.y,rect.width,rect.height));
                        Pair<String,String> ocrResult = ocrValidator.OCRValidate(ocrStr);


                        if(ocrResult!=null){
                            String succesStr = ocrResult.getKey() + "-" + ocrResult.getValue();
                            System.out.println(succesStr);
                            Imgcodecs.imwrite("TargetData\\result" + second + ".jpg", src);
                            return rect;
                        }
                    }
                }
            }
            Imgcodecs.imwrite("TargetData\\result" + second + ".jpg", src);
            return null;

        }catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }


    public Mat ProcessCandidateArea(Mat src){
        Mat src_gaussian = new Mat(src.rows(),src.cols(),src.type());
        Mat src_gray = new Mat(src.rows(),src.cols(),src.type());
        Mat src_laplacian = new Mat(src.rows(),src.cols(),src.type());
        Mat binarization = new Mat(src.rows(),src.cols(),src.type());
        Mat src_grad_x = new Mat(src.rows(),src.cols(),src.type());
        Mat src_grad_y = new Mat(src.rows(),src.cols(),src.type());
        Mat abs_grad_x = new Mat(src.rows(),src.cols(),src.type());
        Mat abs_grad_y = new Mat(src.rows(),src.cols(),src.type());
        Mat src_grad_total = new Mat(src.rows(),src.cols(),src.type());
        Mat abs_src_grad_total = new Mat(src.rows(),src.cols(),src.type());
        Mat erosion = new Mat(src.rows(),src.cols(),src.type());
        Mat dilation = new Mat(src.rows(),src.cols(),src.type());

        Imgproc.GaussianBlur(src, src_gaussian,new Size(5,5), 3);
        Imgproc.cvtColor(src_gaussian,src_gray,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Laplacian(src_gray,src_laplacian, CvType.CV_16S,3,1,0, Core.BORDER_DEFAULT);

        Imgproc.threshold(src_laplacian,binarization,80,255,THRESH_BINARY );
        Imgproc.erode(binarization, erosion, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(3, 1)));
        Imgproc.dilate(erosion, dilation, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(3, 1)));

        /*<그래디언트>*/
        Imgproc.Sobel(dilation,src_grad_x,CvType.CV_16S,1,0,3,3,0);
        Core.convertScaleAbs(src_grad_x, abs_grad_x);
        Imgproc.Sobel(dilation, src_grad_y, CvType.CV_16S, 0, 1,3,3,0);
        Core.convertScaleAbs(src_grad_y, abs_grad_y);

        Core.addWeighted(src_grad_x, 0.5, src_grad_y, 0.5, 0, src_grad_total);
        Core.addWeighted(abs_grad_x, 0, abs_grad_y, 0.5, 0, abs_src_grad_total);


        Imgproc.Sobel(abs_src_grad_total,src_grad_x,CvType.CV_16S,1,0,3,3,0);
        Core.convertScaleAbs(src_grad_x, abs_grad_x);
        Imgproc.Sobel(abs_src_grad_total, src_grad_y, CvType.CV_16S, 0, 1,3,3,0);
        Core.convertScaleAbs(src_grad_y, abs_grad_y);

        Core.addWeighted(src_grad_x, 0.5, src_grad_y, 0.5, 0, src_grad_total);
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, abs_src_grad_total);


        Imgproc.Sobel(abs_src_grad_total,src_grad_x,CvType.CV_16S,1,0,3,3,0);
        Core.convertScaleAbs(src_grad_x, abs_grad_x);
        Imgproc.Sobel(abs_src_grad_total, src_grad_y, CvType.CV_16S, 0, 1,3,3,0);
        Core.convertScaleAbs(src_grad_y, abs_grad_y);

        Core.addWeighted(src_grad_x, 0.5, src_grad_y, 0.5, 0, src_grad_total);
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, abs_src_grad_total);


        /*<Morph Close>*/
        Imgproc.dilate(abs_src_grad_total, dilation, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(65, 3)));
        Imgproc.erode(dilation, erosion, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(5, 5)));
        return erosion;
    }

    public String PreProcessAndDoOCR(BufferedImage subImage) throws Exception {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\ksh\\OneDrive - dongguk.edu\\SoungHo\\2020Winter\\Comprehensive_Design\\(New)VideoSubtitleAndTesseractHardProj\\Project\\tessdata_best\\tessdata");
        tesseract.setLanguage("eng");

        Mat subSrc = img2Mat(subImage);
        Mat src_deonsie = new Mat(subSrc.rows(),subSrc.cols(),subSrc.type());
        Mat src_gray2 = new Mat(subSrc.rows(),subSrc.cols(),subSrc.type());
        Mat src_thresh = new Mat(subSrc.rows(),subSrc.cols(),subSrc.type());
        Mat src_preprocessed = new Mat(subSrc.rows(),subSrc.cols(),subSrc.type());

        Imgproc.cvtColor(subSrc,src_gray2, Imgproc.COLOR_BGR2GRAY);

        Photo.fastNlMeansDenoising(src_gray2,src_deonsie,10,7,21);

        Imgproc.threshold(src_deonsie,src_thresh,180,255,Imgproc.THRESH_BINARY);

        Core.bitwise_not(src_thresh,src_preprocessed);

        BufferedImage preprocessedImg = Mat2BufferedImage(src_preprocessed);
        return tesseract.doOCR(preprocessedImg);
    }

    public static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[]=mob.toArray();

        BufferedImage bi= ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }

    public static Mat img2Mat(BufferedImage bufferedImage) {
        BufferedImage castImage = new BufferedImage(
                bufferedImage.getWidth()
                , bufferedImage.getHeight()
                , BufferedImage.TYPE_3BYTE_BGR
        );

        castImage
                .getGraphics()
                .drawImage(bufferedImage, 0, 0, null);

        Mat img = new Mat(
                bufferedImage.getHeight()
                , castImage.getWidth()
                , CvType.CV_8UC3
        );

        byte[] pixels = (
                (DataBufferByte) castImage
                        .getRaster()
                        .getDataBuffer()
        ).getData();

        img.put(0, 0, pixels);
        return img;
    }


}
