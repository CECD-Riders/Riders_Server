package me.soungho.demo;

import javafx.util.Pair;
import org.springframework.stereotype.Component;


//강화된 올바른 문장 검증 방법
@Component
public class OCRValidator {

    public boolean IsRightString(String ocrStr)
    {
        int idx = ocrStr.indexOf('/');
        if(idx >=0)
            return true;
        else
            return false;
    }


    //ocrStr을 검증해서 올바른 키워드면 키워드 리턴 / 아니면 null리턴
    public Pair<String,String> OCRValidate(String ocrStr){

        if(!IsRightString(ocrStr))
            return null;
        String[] compontents = ocrStr.split("/");
        String ocrTeamStr = compontents[0];
        String ocrActionStr = compontents[1];
        int home = 0, away = 0;
        int two = 0, three = 0, dunk = 0 , block = 0;

        if(ocrTeamStr.indexOf('H') >=0) home++;
        if(ocrTeamStr.indexOf('o') >=0) home++;
        if(ocrTeamStr.indexOf('m') >=0) home++;
        if(ocrTeamStr.indexOf('e') >=0) home++;

        if(ocrTeamStr.indexOf('A') >=0) away++;
        if(ocrTeamStr.indexOf('w') >=0) away++;
        if(ocrTeamStr.indexOf('a') >=0) away++;
        if(ocrTeamStr.indexOf('y') >=0) away++;

        if(ocrActionStr.indexOf('T') >=0) two++;
        if(ocrActionStr.indexOf('w') >=0) two++;
        if(ocrActionStr.indexOf('o') >=0) two++;
        if(ocrActionStr.indexOf('p') >=0) two++;
        if(ocrActionStr.indexOf('o') >=0) two++;
        if(ocrActionStr.indexOf('i') >=0) two++;
        if(ocrActionStr.indexOf('n') >=0) two++;
        if(ocrActionStr.indexOf('t') >=0) two++;

        if(ocrActionStr.indexOf('T') >=0) three++;
        if(ocrActionStr.indexOf('h') >=0) three++;
        if(ocrActionStr.indexOf('r') >=0) three++;
        if(ocrActionStr.indexOf('e') >=0) three++;
        if(ocrActionStr.indexOf('e') >=0) three++;
        if(ocrActionStr.indexOf('p') >=0) three++;
        if(ocrActionStr.indexOf('o') >=0) three++;
        if(ocrActionStr.indexOf('i') >=0) three++;
        if(ocrActionStr.indexOf('n') >=0) three++;
        if(ocrActionStr.indexOf('t') >=0) three++;

        if(ocrActionStr.indexOf('D') >=0) dunk++;
        if(ocrActionStr.indexOf('u') >=0) dunk++;
        if(ocrActionStr.indexOf('n') >=0) dunk++;
        if(ocrActionStr.indexOf('k') >=0) dunk++;

        if(ocrActionStr.indexOf('B') >=0) block++;
        if(ocrActionStr.indexOf('l') >=0) block++;
        if(ocrActionStr.indexOf('o') >=0) block++;
        if(ocrActionStr.indexOf('k') >=0) block++;

        if((home <3 && away < 3) || (two < 4 && three < 4 && block <3 && dunk < 3) ){
            return null;
        }

        String teamStr = (home > away)? "Home" : "Away";
        String actionStr;
        int maxCount;
        if(two > three)
        {
            maxCount = two;
            actionStr = "2Point";
        }
        else
        {
            maxCount = three;
            actionStr = "3Point";
        }

        if(maxCount < dunk)
        {
            maxCount = dunk;
            actionStr = "Dunk";
        }

        if(maxCount < block)
        {
            maxCount = block;
            actionStr = "Block";
        }
        return new Pair<String,String >(teamStr,actionStr);
    }
}
