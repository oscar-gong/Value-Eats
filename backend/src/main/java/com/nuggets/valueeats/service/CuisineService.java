package com.nuggets.valueeats.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nuggets.valueeats.utils.ResponseUtils;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



// TEMPORARY CUISINES LIST
@Service
public class CuisineService {

    public ResponseEntity<JSONObject> listCuisines() {
        List<String> list = new ArrayList<String>();
        String[] cuisines = ("Ainu, Albanian, Argentine, Andhra, American, Anglo-Indian, Arab, Armenian, Assyrian, " +
        "Awadhi, Azerbaijani, Balochi, Bashkir, Belarusian, Bangladeshi, Bengali, Berber, Brazilian, British, Buddhist, " +
        "Bulgarian, Cajun, Cantonese, Caribbean, Chechen, Chinese, Chinese Islamic, Circassian, Crimean Tatar, Cypriot, Czech, " +
        "Danish, Egyptian, English, Ethiopian, Eritrean, Estonian, French, Filipino, Georgian, German, Goan, Goan Catholic, Greek, " +
        "Gujarati, Hyderabad, Indian, Indian Chinese, Indian Singaporean, Indonesian, Inuit, Irish, Italian-American, Italian, " +
        "Jamaican, Japanese, Jewish - Israeli, Karnataka, Kazakh, Keralite, Korean, Kurdish, Laotian, Lebanese, Latvian, Lithuanian, Louisiana " +
        "Creole, Maharashtrian, Mangalorean, Malay, Malaysian Chinese, Malaysian Indian, Mediterranean, Mennonite, Mexican, Mordovian, Mughal, " +
        "Native American, Nepalese, New Mexican, Odia, Parsi, Pashtun, Polish, Pennsylvania Dutch, Pakistani, Peranakan, Persian, Peruvian, Portuguese, " +
        "Punjabi, Québécois, Rajasthani, Romani, Romanian, Russian, Sami, Serbian, Sindhi, Slovak, Slovenian, Somali, South Indian, Soviet, Spanish, " +
        "Sri Lankan, Taiwanese, Tatar, Texan, Thai, Turkish, Tamil, Udupi, Ukrainian, Vietnamese, Yamal, Zambian, Zanzibari").split(", ");

        
        list = Arrays.asList(cuisines);

        
        Map<String, List<String>> dataMedium = new HashMap<>();
        dataMedium.put("cuisines", list);
        JSONObject data = new JSONObject(dataMedium);

        // JSONArray jsArray = new JSONArray();
        // jsArray.addAll(cuisinesArray); 


        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }
}
