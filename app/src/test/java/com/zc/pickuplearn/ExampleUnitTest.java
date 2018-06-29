package com.zc.pickuplearn;

import com.google.gson.JsonElement;
import com.zc.pickuplearn.http.JsonUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    public String string = "{\n" +
            "    \"APPLICATIONINFO\": {\n" +
            "        \"isdebug\": \"TRUE\",\n" +
            "        \"rowsperpage\": \"20\"\n" +
            "    },\n" +
            "    \"CODES\": [],\n" +
            "    \"COMMAND\": {\n" +
            "        \"action\": \"select\",\n" +
            "        \"actionFlag\": \"select\",\n" +
            "        \"modalName\": \"null\",\n" +
            "        \"object\": \"null\",\n" +
            "        \"target\": \"http://117.149.2.229:1624/ecm/mobile/MobileExamCaseModule2.jspx?isApp=1&userID=90000400&teamid=6474&authStr=id&lesson_Id=c692679c-1ea3-4341-81ac-1e41302939df\",\n" +
            "        \"targetpage\": \"null\",\n" +
            "        \"uimodelname\": \"null\"\n" +
            "    },\n" +
            "    \"DATAS\": {\n" +
            "        \"V_KLB_TEAMCIRCLE_INFO\": {\n" +
            "            \"datas\": [\n" +
            "                {\n" +
            "                    \"TEAMCIRCLECODE\": \"6474\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"name\": \"V_KLB_TEAMCIRCLE_INFO\",\n" +
            "            \"totalCount\": 0\n" +
            "        }\n" +
            "    },\n" +
            "    \"EDITEDCOMPONENTS\": [],\n" +
            "    \"EXCEPTIONDATA\": [],\n" +
            "    \"EXCEPTIONS\": [],\n" +
            "    \"ISREFRESH\": \"FALSE\",\n" +
            "    \"PARAMETERS\": {\n" +
            "        \"CLASSNAME\": \"com.nci.klb.app.exam.examCaseTeam\",\n" +
            "        \"DETAILFIELD\": \"\",\n" +
            "        \"DETAILTABLE\": \"\",\n" +
            "        \"MASTERFIELD\": \"SEQKEY\",\n" +
            "        \"MASTERTABLE\": \"V_KLB_TEAMCIRCLE_INFO\",\n" +
            "        \"MENUAPP\": \"EMARK_APP\",\n" +
            "        \"METHOD\": \"DailyPractice\",\n" +
            "        \"ORDERSQL\": \"SYSCREATEDATE desc\",\n" +
            "        \"WHERESQL\": \"\",\n" +
            "        \"limit\": \"20\",\n" +
            "        \"mobileapp\": \"true\",\n" +
            "        \"start\": \"0\"\n" +
            "    },\n" +
            "    \"RETMSG\": \"操作成功！\",\n" +
            "    \"UIMODEL\": {},\n" +
            "    \"VALIDATOR\": []\n" +
            "}";
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void Test ()throws Exception{
        JsonElement datas = JsonUtils.decoElementJSONObject(string, "COMMAND");
        String target = JsonUtils.decoElementASString(datas.toString(), "target");
        System.out.println(target);
    }

    @Test
    public void Test2(){
        System.out.println("test");
    }
}