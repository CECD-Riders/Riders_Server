package project.ridersserver.ridersserverapp.Controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.ridersserver.ridersserverapp.FTP.FTPHostInfo;
import project.ridersserver.ridersserverapp.FTP.FTPUploader;
import project.ridersserver.ridersserverapp.domain.Video.VideoEntity;
import project.ridersserver.ridersserverapp.dto.VideoDto;
import project.ridersserver.ridersserverapp.service.VideoService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class VideoController {

    private VideoService videoService;

    private FTPHostInfo ftpHostInfo;

    //영상 시청
    @RequestMapping("/watch")
    public String watchVideo(HttpServletRequest request, Model model) {
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

        VideoEntity videoEntity = videoService.loadVideoByVideoname(videoName);
        //영상이 있는지 없는지 데이터 베이스 조회
        if(videoEntity !=null) {
            model.addAttribute("view",videoEntity.getView());
            model.addAttribute("like",videoEntity.getLike());
            model.addAttribute("day",videoEntity.getCreateTimeAt());
            return "/watchVideo";
        }
        else
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
    public String videoUploadAction(HttpServletRequest request ,Model model) {
        String localPath = request.getParameter("path");                    //로컬 경로(전송을 위해 필요한 경로)
        String HostfileName = request.getParameter("HostfileName");        //호스트 서버에 저장될 파일 이름(호스트 서버 밑 디비에 저장=> 이름규칙 필수적으로 따라야함)
        localPath = "C:\\Users\\ksh\\OneDrive - dongguk.edu\\SoungHo\\2020Winter\\Comprehensive_Design\\dataSample\\" + localPath;
        System.out.println(localPath);
        System.out.println(HostfileName);
        Long id;
        VideoDto videoDto = new VideoDto();
        videoDto.setName(HostfileName);
        videoDto.setLike(new Long(0));
        videoDto.setView(new Long(0));
        videoDto.setCreateTimeAt(LocalDateTime.now());
        FTPUploader ftpUploader;
        try {
            //디비 -> FTP
            id = videoService.SaveSingleVideo(videoDto);
            if (id == -1) {
//                model.addAttribute("succesMsg", "영상전송 실패! 중복된 영상이 있습니다.");
                return "redirect:/admin/videoUpload?msg=overlap";
            } else {
                ftpUploader = new FTPUploader(ftpHostInfo.getHostIP(), ftpHostInfo.getPort(), ftpHostInfo.getID(), ftpHostInfo.getPW());
                ftpUploader.uploadFile(localPath, HostfileName, "/ridersTest/");
                ftpUploader.disconnect();
            }
        } catch (Exception e) {
            videoService.DeleteSingleVideo(videoDto);
//            model.addAttribute("succesMsg", "영상전송 실패! 서버와의 연결을 확인해 주세요.");
            return "redirect:/admin/videoUpload?msg=serverError";
        }
        return "redirect:/admin/videoUpload?msg=succes";

//        if (id != -1) {
//            model.addAttribute("succesMsg", "영상전송 성공!");
//            return "redirect:videoUpload?msg=succes";
//        }


    }

}
