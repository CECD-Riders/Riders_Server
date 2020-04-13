package project.ridersserver.ridersserverapp.Controller;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.ridersserver.ridersserverapp.FTP.FTPHostInfo;
import project.ridersserver.ridersserverapp.FTP.FTPUploader;
import project.ridersserver.ridersserverapp.VideoConverter.VideoConverter;
import project.ridersserver.ridersserverapp.domain.Video.VideoEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoEventEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoEventRepository;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeEntity;
import project.ridersserver.ridersserverapp.service.VideoViewLikeService;
import project.ridersserver.ridersserverapp.service.VideoService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class VideoController {

    private VideoService videoService;

    private VideoViewLikeService videoViewLikeService;

    private FTPHostInfo ftpHostInfo;

    private VideoConverter videoConverter;

    private VideoEventRepository videoEventRepository;

    //좋아요 처리
    @RequestMapping("/member/vidoLike")
    @ResponseBody
    public int videoLikeHandler(HttpServletRequest request){
        String recommendMsg = request.getParameter("recommendMsg");
        String memberName = request.getParameter("memberName");
        String videoName = request.getParameter("videoName");
        Long videoLike = Long.parseLong(request.getParameter("like"));

        if(recommendMsg.equals("추천")){//-> (추천관계 저장->) videoViewLike에서 isLike를 true로 + videoName기준 video 테이블에 접근해서 like 1증가
            try {
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.setName(videoName);
                videoEntity.setLike(videoLike);
                videoService.UpSingleVideoLike(videoEntity);

                VideoViewLikeEntity videoViewLikeEntity = new VideoViewLikeEntity();
                videoViewLikeEntity.setMemberName(memberName);
                videoViewLikeEntity.setVideoName(videoName);
                videoViewLikeService.updateSingleVideoLike(videoViewLikeEntity,true);
                return 1;
            }catch (Exception e){   //->뭔가 실패한것
                e.printStackTrace();
                return -1;
            }
        }
        else{//->(추천관계 삭제->)videoViewLike에서 isLike를 false로 + videoName기준 video 테이블에 접근해서 like 1 감소
            try {
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.setName(videoName);
                videoEntity.setLike(videoLike);
                videoService.DownSingleVideoLike(videoEntity);

                VideoViewLikeEntity videoViewLikeEntity = new VideoViewLikeEntity();
                videoViewLikeEntity.setMemberName(memberName);
                videoViewLikeEntity.setVideoName(videoName);
                videoViewLikeService.updateSingleVideoLike(videoViewLikeEntity,false);
                return 1;
            }catch (Exception e){
                e.printStackTrace();
                return -1;
            }
        }
    }

    //비디오 마크 처리
    @GetMapping("/member/videoMark")
    @ResponseBody
    public String videoMarkHandler(HttpServletRequest request) {
        String videoName = request.getParameter("videoName");
        String eventName = request.getParameter("eventName");

        VideoEntity videoEntity = videoService.loadVideoByVideoName(videoName);

        List<VideoEventEntity> videoEventEntities = new ArrayList<>();

        if(eventName.equals("어웨이")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(2,videoEntity);
        }
        else if(eventName.equals("홈")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(1,videoEntity);
        }
        else if(eventName.equals("2점슛")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(4,videoEntity);
        }
        else if(eventName.equals("3점슛")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(3,videoEntity);
        }
        else if(eventName.equals("덩크")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(5,videoEntity);
        }
        else if(eventName.equals("블락")){
            videoEventEntities = videoEventRepository.findByWhichEventAndVideoOrderByTimeDesc(6,videoEntity);
        }
        else
            System.out.println("nothing");

        String markStr = "[";
        if(videoEventEntities.size() != 0){
            for(VideoEventEntity videoEvent : videoEventEntities){
                String parsedEventTime = "{time: " + videoEvent.getTime() + "},";
                markStr = markStr + parsedEventTime;
            }
        }else
            markStr = markStr + "{}";

        markStr = markStr + "]";
        return markStr;
    }

    //영상 시청
    @RequestMapping("/member/watch")
    public String watchVideo(HttpServletRequest request, Model model, Principal principal) {
        String memberName;
        memberName = principal.getName();

        String videoName = request.getParameter("videoName");
        if(videoName == null){
            String useDate = request.getParameter("year");
            String date = request.getParameter("date");
            String dateP1[] = date.split("\\s+");
            String dateP2[] = dateP1[0].split("\\.");

            String month = dateP2[0];
            String day = dateP2[1];
            if(month.length()==1)
                month = "0"+month;
            if(day.length()==1)
                day = "0"+day;
            useDate = useDate + month + day;

            String leftTeam = request.getParameter("leftTeam");
            String rightTeam = request.getParameter("rightTeam");
            videoName = useDate + "-" +leftTeam + "-" + rightTeam + ".mp4";
        }
        model.addAttribute("hostIp",ftpHostInfo.getHostIP());
        model.addAttribute("videoName",videoName);

        VideoEntity videoEntity = videoService.loadVideoByVideoName(videoName);
        //영상이 있는지 없는지 데이터 베이스 조회
        if(videoEntity !=null) {
            //영상이 있을 시에 현 접속자와 영상 사이의 조회 관계 판단
            VideoViewLikeEntity videoViewLikeEntity = videoViewLikeService.findByMemberNameAndVideoName(memberName, videoName);
            if(videoViewLikeEntity !=null) { //조회관계가 있음
                model.addAttribute("view",videoEntity.getView());
                if(videoViewLikeEntity.isLike()) //-> 이미 좋아요를 누른 상태
                    model.addAttribute("recommendationMsg","해지");
                else  //좋아요를 누르지 않은 상태
                    model.addAttribute("recommendationMsg","추천");
            }else { //조회관계가 없음
                VideoViewLikeEntity newVideoViewLikeEnity = new VideoViewLikeEntity();
                newVideoViewLikeEnity.setMemberName(memberName);
                newVideoViewLikeEnity.setVideoName(videoName);
                newVideoViewLikeEnity.setLike(false);
                videoViewLikeService.saveVideoViewLike(newVideoViewLikeEnity);
                videoService.UpSingleVideoView(videoEntity);
                model.addAttribute("view",videoEntity.getView() + 1);
                model.addAttribute("recommendationMsg","추천");
            }

            model.addAttribute("like",videoEntity.getLike());
            model.addAttribute("day",videoEntity.getCreateTimeAt());
            model.addAttribute("member",memberName);
            return "/watchVideo";
        }
        else//->없으면
            return "/noVideo";	//없으면 준비중 페이지로 이동
    }

    // 영상전송 페이지
    @GetMapping("/admin/videoUpload")
    public String videoUpload(HttpServletRequest request,Model model) {
        String msg = request.getParameter("msg");

        if(msg != null) {
            if (msg.equals("succes"))
                model.addAttribute("succesMsg", "영상전송 성공!");
            else if (msg.equals("overlap"))
                model.addAttribute("succesMsg", "영상전송 실패! 중복된 영상이 있습니다.");
            else if (msg.equals("serverError"))
                model.addAttribute("succesMsg", "영상전송 실패! 서버와의 연결을 확인해 주세요.");
        }
        return "videoUpload";
    }

    // 영상전송 수행
    @PostMapping("/admin/videoUpload")
    public String videoUploadAction(HttpServletRequest request ,Model model) throws TesseractException, FrameGrabber.Exception {
        String localPath = request.getParameter("path");                    //로컬 경로(전송을 위해 필요한 경로)
        String HostfileName = request.getParameter("HostfileName");         //호스트 서버에 저장될 파일 이름(호스트 서버 밑 디비에 저장=> 이름규칙 필수적으로 따라야함)
        localPath = "C:\\Users\\ksh\\OneDrive - dongguk.edu\\SoungHo\\2020Winter\\Comprehensive_Design\\dataSample\\" + localPath;

        //1. 영상의 이벤트 정보를 디비에 저장할 스트링 생성
        String awayTime = "", homeTime = "", twoTime = "", threeTime = "", dunkTime = "", blockTime = "";
        videoConverter.setTesseractPath("C:\\Users\\ksh\\OneDrive - dongguk.edu\\SoungHo\\2020Winter\\Comprehensive_Design\\RidersWebServer\\tessdata");
        videoConverter.setVideoFilePath(localPath);
        videoConverter.setFrameRate(30);
        videoConverter.setImageDomain(10, 10, 300, 45);
        ArrayList<Pair<Double,String>> convertVideoToString = videoConverter.ConvertVideoToString();
        VideoEntity videoEntity = new VideoEntity();

        for(int i = 0 ; i < convertVideoToString.size();i++) {
            double timeInfo = convertVideoToString.get(i).getKey();
            String[] splitedStr = convertVideoToString.get(i).getValue().split("-");
            String teamInfo = splitedStr[0];
            String actionInfo = splitedStr[1];
            System.out.println(timeInfo + "/" + teamInfo + "/" + actionInfo);

            if(teamInfo.equals("Away")){
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(2);
                videoEntity.addEvent(videoEventEntity);
            }
            else{
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(1);
                videoEntity.addEvent(videoEventEntity);
            }

            if(actionInfo.equals("2Point")){
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(4);
                videoEntity.addEvent(videoEventEntity);
            }
            else if(actionInfo.equals("3Point")){
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(3);
                videoEntity.addEvent(videoEventEntity);
            }
            else if(actionInfo.equals("Dunk")){
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(5);
                videoEntity.addEvent(videoEventEntity);
            }
            else if(actionInfo.equals("Block")){
                VideoEventEntity videoEventEntity = new VideoEventEntity();
                videoEventEntity.setTime(timeInfo);
                videoEventEntity.setWhichEvent(6);
                videoEntity.addEvent(videoEventEntity);
            }
            else
                System.out.println("nothing");
        }

        //2. 영상전송
        String[] parsedStr = HostfileName.split("-");
        String[] parsedStr2 = parsedStr[2].split("\\.");

        videoEntity.setHomeTeam(parsedStr[1]);
        videoEntity.setAwayTeam(parsedStr2[0]);
        videoEntity.setName(HostfileName);
        videoEntity.setLike(new Long(0));
        videoEntity.setView(new Long(0));
        videoEntity.setCreateTimeAt(LocalDateTime.now());
        FTPUploader ftpUploader;
        try {
            //디비 -> FTP
            Long id = videoService.SaveSingleVideo(videoEntity);
            if (id == -1) {
                return "redirect:/admin/videoUpload?msg=overlap";
            } else {
                ftpUploader = new FTPUploader(ftpHostInfo.getHostIP(), ftpHostInfo.getPort(), ftpHostInfo.getID(), ftpHostInfo.getPW());
                ftpUploader.uploadFile(localPath, HostfileName, "/ridersTest/");
                ftpUploader.disconnect();
            }
        } catch (Exception e) {
            videoService.DeleteSingleVideo(videoEntity);
            return "redirect:/admin/videoUpload?msg=serverError";
        }
        return "redirect:/admin/videoUpload?msg=succes";
    }
}
