package com.zc.pickuplearn.http;

/**
 * 接口特殊访问方法
 * 作者： Jonsen
 * 时间: 2016/11/29 16:01
 * 联系方式：chenbin252@163.com
 */

public class UrlMethod {

    public static int PAZE_SIZE =  20;//分页数量

    public static String RETMSG = "RETMSG";
    public static String STRING＿TRUE = "1";
    public static String STRING＿FALSE = "0";


    /*经典案咧查询类型*/
    public static String TYPE_NEW = "TYPE_NEW";
    public static String TYPE_HOT = "TYPE_HOT";
    public static String TYPE_ALL= "TYPE_ALL";
    public static String TYPE_TEXT = "TYPE_TEXT";
    public static String TYPE_IMAGE = "TYPE_IMAGE";
    public static String TYPE_VIDEO = "TYPE_VIDEO";
    public static String TYPE_OTHER = "TYPE_OTHER";
    /*四种方法查询*/
    public static String UPDATASAVE = "updateSave";
    public static String DELETE = "delete";
    public static String SEARCH = "search";
    public static String ADDSAVE = "addSave";
    public static String CLASSNAME = "CLASSNAME";

    public static String APP_BIZ_OPERATION = "com.nci.app.operation.business.AppBizOperation";// 通用方法
    public static String EXPERT_MANAGE = "com.nci.klb.app.procirclestatus.ExpertManage";// 专家查询
    public static String CLICK_COUNT = "com.nci.klb.app.classiccase.ClickCount";// 经典案咧点击量方法
    public static String CASE_SCORE = "com.nci.klb.app.classiccase.CaseScore";// 案例评分方法
    public static String CLASSIC_CASE = "com.nci.klb.app.classiccase.ClassicCase";// 经典案例
    public static String USER_INFO = "com.nci.klb.app.userinfo.UserInfo";//查询用户信息
    public static String QueAns_Detail = "com.nci.klb.app.answer.QueAnsDetail";//追问追答
    public static String COLLECT_CASE = "com.nci.klb.app.classiccase.CollectCase";//我的收藏 金典案咧
    public static String QUESTION_POINTS = "com.nci.klb.app.question.QuestionPoints"; //提问 查询问题
    public static String ANSWER_GOOD = "com.nci.klb.app.answer.AnswerGood";//查询问题答案
    public static String My_Answer = "com.nci.klb.app.answer.MyAnswer";//查询我的回答
    public static String CASE_COMMENT = "com.nci.klb.app.classiccase.CaseComment";//金典案咧评论
    public static String ANSWER_PONITS = "com.nci.klb.app.answer.AnswerPoints";//回答问题
    public static String PROCIRCLE_POSITION = "com.nci.klb.app.procirclestatus.ProcirclePosition";//获取专业圈排名
    public static String PROCIRCLE_USERPOS = "com.nci.klb.app.procirclestatus.ProcircleUserPos";//专业圈人员排名信息
    public static String EPMHR = "com.nci.epm.biz.hr.EpmHr";//修改密码的方法
    public static String URSER_INFO = "com.nci.klb.app.userinfo.UserInfo";//上传头像
    public static String TEAM_CIRCLE_INFO = "com.nci.klb.app.teamcircle.TeamcircleInfo";//班组圈信息
    public static String TEAMUSERNAME = "com.nci.klb.app.teamcircle.TeamUserName";//查询我加入的班组圈
    public static String TEAM_CIRCLE_ANSWERGOOD = "com.nci.klb.app.teamcircle.AnswerGood";//查询班组圈答案列表
    public static String TEAM_CIRCLE_ANSWER = "com.nci.klb.app.teamcircle.TeamcircleAnswer";//回答
    public static String TEAM_CIRCLE_USER_INFO = "com.nci.klb.app.teamcircle.UserInfo";//查询班组圈人员列表
    public static String TEAM_CIRCLE_QUESTION = "com.nci.klb.app.teamcircle.TeamcircleQuestion";//查询班组问题 和 提问的方法
    public static String TEAM_CIRCLE_ANSWER_TAKE = "com.nci.klb.app.teamcircle.AnswerTake";//班组圈采纳
    public static String TEAM_CIRCLE_QUEANS_DETAIL = "com.nci.klb.app.teamcircle.QueAnsDetail";//班组圈追问追答
    public static String TEAM_CIRCLE_TEAMCIRCLE_DYNAMIC = "com.nci.klb.app.teamcircle.TeamcircleDynamic";//首页动态
	public static String TEAM_CIRCLE_TEAMCIRCLE_MANAGER = "com.nci.klb.app.teamcircle.TeamcircleManager";//团队转移权限 解散
	public static String TEXAM_CASE_TEAM = "com.nci.klb.app.exam.examCaseTeam";//练一练
	public static String TEAMCIRCLE_RANK = "com.nci.klb.app.teamcircle.TeamcircleRank";//比一比
	public static String STANDARD_INFO = "com.nci.klb.app.standard.StandardInfo";//行业规范
	public static String STANDARD_CLICKCOUNT = "com.nci.klb.app.standard.ClickCount";//行业规范点击
	public static String TEAMMESSAGE_MANAGE = "com.nci.klb.app.message.TeamMessageManage";//团队消息发送
	public static String PERSON_MESSAGE_MANAGE = "com.nci.klb.app.message.PersonMessageManage";//个人消息
	public static String USERINFOMANAGE = "com.nci.klb.app.userinfo.UserInfoManage";//个人消息
	public static String EXAMCASETEAM = "com.nci.klb.app.exam.examCaseTeam";//拾学推荐学习

	public static String EXAMFUNCTION = "com.nci.klb.app.exam.examFunction";//功能编辑获取链接

	public static String FUNCTION_LISTMANAGE = "com.nci.klb.app.homePage.FunctionList";//我的功能列表
	public static String MESSAGE_LIST = "com.nci.klb.app.homePage.MessageList";//首页消息
	public static String QUESTION_BANK = "com.nci.klb.app.exam.QuestionBank";//能力题库
	public static String ZctHttpClient = "com.nci.klb.app.zct.ZctHttpClient";//能力学堂和员工发展

    //台区经理
	public static String CoursewareManage = "com.nci.klb.app.scene.KlbCoursewareManage";
	public static String KlbAbilityManage = "com.nci.klb.app.scene.KlbAbilityManage";
	public static String QuestionBankQuery = "com.nci.klb.app.exam.QuestionBankQuery";

}
