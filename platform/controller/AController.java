package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import platform.businessLayer.CodeService;
import platform.logic.MCode;
import platform.logic.MCodeDTO;
import platform.logic.MCodePost;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
// avec @RestController pas besoin de @RequestBody mais marche pas avec FreeMarker
public class AController {

    public static final String code = "public static void main(String[] args) {\\n    SpringApplication.run(CodeSharingPlatform.class, args);\\n}";
    public static final LocalDate codeDate = LocalDate.of(2021, 01 ,01);
    public static final LocalTime codeTime = LocalTime.of(01, 01 ,01);

    public static MCode mCode = new  MCode(code, codeDate, codeTime );

    public static List<MCode> synList = Collections.synchronizedList(new ArrayList<MCode>());

    @Autowired
    CodeService codeService;




    @RequestMapping(value = "/code/{uuid}" , method=RequestMethod.GET)
    public @ResponseBody String  HtmlCode(@PathVariable String uuid, HttpServletResponse response) {

        String result = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

        if(codeService.possibleToViewCode(uuid)){

            response.addHeader("Content-Type", "text/html");

            MCode mCode = codeService.findCodeByUuid(uuid).get();

            String formattedDate = mCode.getCodeDate().format(formatter);

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = mCode.getCodeTime().format(formatter2);

            String viewsAndTimeStr = "";

            if(mCode.isSecretCodeViews()){

                viewsAndTimeStr = "<p>" + "<span id=\"views_restriction\">" +
                        mCode.getViews() + "</span>" + " more views allowed</p>";

            }

            if(mCode.isSecretCodeTime()){

                viewsAndTimeStr = viewsAndTimeStr + "<p> " +
                        "The code will be available for " + "<span id=\"time_restriction\">"
                        + mCode.getTime() + "/span>" + " seconds</p>";

            }


            String SriptCode =
                    "<link rel=\"stylesheet\"\n" +
                            "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                            "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                            "<script>hljs.initHighlightingOnLoad();</script>";

            StringBuilder sb = new StringBuilder();

            sb.append("<html>\n" +
                    "<head>\n" +
                    "    <title>Code</title>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    SriptCode +
                    "</head>\n" +
                    "<body>\n" +
                    "<span id=\"load_date\">\n" +

                    formattedDate + " " + formattedTime +

                    "</span>\n" +

                    viewsAndTimeStr +

                    "<pre id=\"code_snippet\"><code>");

            sb.append(mCode.getCode());

            sb.append("</code></pre>\n" +
                    "</body>\n" +
                    "</html>");

            result = sb.toString();


        }else{

            throw new ResourceNotFoundException();

        }


        return result;

    }

    @RequestMapping(value = "/api/code/{uuid}", method=RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody MCodeDTO apiCode(@PathVariable String uuid,HttpServletResponse response) {

        MCodeDTO mCodeDTO = null;

        response.addHeader("Content-Type", "application/json");

        if(codeService.possibleToViewCode(uuid)) {

            MCode mCode = codeService.findCodeByUuid(uuid).get();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String formattedDate = mCode.getCodeDate().format(formatter);

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = mCode.getCodeTime().format(formatter2);


            mCodeDTO = new MCodeDTO(mCode.getCode(), formattedDate + " " + formattedTime, mCode.getTime(), mCode.getViews());


        }else{
            throw new ResourceNotFoundException();
        }

        return mCodeDTO;
    }

    @RequestMapping(value = "/api/code/new",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String apiPostNewCode(@RequestBody MCodePost code) {

      //  MCodeVide mcodeVide = new MCodeVide();

        MCode mCode = new MCode(code.getCode(), LocalDate.now(), LocalTime.now(), code.getTime(), code.getViews());

     /*   this.mCode.setCode(code.getCode());

       this.mCode.setCodeDate(LocalDate.now());

       this.mCode.setCodeTime(LocalTime.now()); */

        codeService.saveCode(mCode);

        String jsonRetou = "{ " + "\"id\":" + "\"" + mCode.getUuidCode() + "\"" + " }";

       return jsonRetou;
    }

    @RequestMapping(value = "/code/new", method=RequestMethod.GET)
    public @ResponseBody String getNewCode() {

        String fileName = "C:\\Users\\psir1\\IdeaProjects\\Code Sharing Platform\\Code Sharing Platform\\task\\src\\resources\\postFile";

        String strhtmlFile = "";

        try {
            strhtmlFile = new String(Files.readAllBytes(Paths.get(fileName)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strhtmlFile;
    }

    @RequestMapping(value = "/api/code/latest", method=RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<MCodeDTO> getApiLatestCode() {

        return tenthCode(codeService.findAllCode());
    }

    @RequestMapping(value = "/code/latest", method=RequestMethod.GET)
    public String getLatestCode(Model model) {

        model.addAttribute("codeList", tenthCode(codeService.findAllCode()));

        return "codeTemplate";
    }

    public static List<MCodeDTO> tenthCode(List<MCode> mCodeList){

        ArrayList<MCodeDTO> listDTO = new ArrayList<>();

        // Comprarer par date puis par temps en inverse

       /* List<MCode> outList = mCodeList.stream()
                               .sorted(Comparator.comparing(MCode::getCodeDate).reversed()
                                       .thenComparing(MCode::getCodeTime).reversed())
                                           .collect(Collectors.toList());*/

        // Trier par Id et avec timerest = 0 et Viewrest = 0
        List<MCode> outList = mCodeList.stream()
                .sorted(Comparator.comparing(MCode::getCodeId).reversed())
                .filter(c -> c.getTime() == 0 && c.getViews() == 0)
                .collect(Collectors.toList());

        int count = 1;

        for(MCode mCode : outList){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String formattedDate = mCode.getCodeDate().format(formatter);

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = mCode.getCodeTime().format(formatter2);

            MCodeDTO mCodeDTO = new MCodeDTO(mCode.getCode(), formattedDate + " " + formattedTime, mCode.getTime(), mCode.getViews());

            if(count <= 10) {

                listDTO.add(mCodeDTO);

                count += 1;
            }
            else{

                break;
            }

        }

        return listDTO;
    }

}
