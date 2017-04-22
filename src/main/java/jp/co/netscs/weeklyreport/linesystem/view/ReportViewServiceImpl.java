package jp.co.netscs.weeklyreport.linesystem.view;

import static jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant.LAST_WEEKS_VIEW;
import static jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant.LAST_WEEK_VIEW;
import static jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant.NEXT_WEEKS_VIEW;
import static jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant.NEXT_WEEK_VIEW;
import static jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant.ONE_WEEK_DAYS;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import jp.co.netscs.weeklyreport.linesystem.common.ChapterManager;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.Chapter;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.ResponseScene;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.Scene;
import jp.co.netscs.weeklyreport.linesystem.common.daos.DayReportDao;
import jp.co.netscs.weeklyreport.linesystem.common.dtos.LinePostInfoDto;
import jp.co.netscs.weeklyreport.linesystem.common.dtos.ResponseSceneResultDto;
import jp.co.netscs.weeklyreport.linesystem.common.entitis.UserEntity;
import jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant;
import jp.co.netscs.weeklyreport.linesystem.common.util.LineMessageUtils;

@Chapter(name = LineBotConstant.CHAPTER_REPORTVIEW, startScene = LineBotConstant.REPORTVIEW_SCENE_SELECT_WEEKS_VIEW)
public class ReportViewServiceImpl extends ReportViewService {
	
	@Autowired
	DayReportDao dayReportDao;
	
	public ReportViewServiceImpl(@Autowired ChapterManager manager) {
		super(manager);
	}
	
	@Scene(sceneName = LineBotConstant.REPORTVIEW_SCENE_SELECT_WEEKS_VIEW, next = LineBotConstant.REPORTVIEW_SCENE_VIEW)
	public List<Message> selectViewWeek(LinePostInfoDto lineInfo, UserEntity userInfo) {
		String text = lineInfo.getText();
		String selectText = text.split(",")[0];
		Message weeks = null;
		switch (selectText) {
			case NEXT_WEEKS_VIEW: {
				Integer offset = Integer.parseInt(text.split(",")[1]);
				weeks = LineMessageUtils.generateWeeksCarousel(LocalDate.now(),  offset - (ONE_WEEK_DAYS * 2));
				break;
			}
			case LAST_WEEKS_VIEW: {
				Integer offset = Integer.parseInt(text.split(",")[1]);
				weeks = LineMessageUtils.generateWeeksCarousel(LocalDate.now(), offset + (ONE_WEEK_DAYS * 2));
				break;
			}
			default: {
				weeks = LineMessageUtils.generateWeeksCarousel(LocalDate.now(), 0);
			}
		}
		return Arrays.asList(weeks);
	}
	
	@ResponseScene(target = LineBotConstant.REPORTVIEW_SCENE_SELECT_WEEKS_VIEW)
	public ResponseSceneResultDto selectViewWeekAfter(LinePostInfoDto lineInfo, UserEntity userInfo) {
		String text = lineInfo.getText();
		String selectText = text.split(",")[0];
		switch (selectText) {
			case NEXT_WEEK_VIEW: 
			case LAST_WEEK_VIEW:
				return ResponseSceneResultDto.builder().dummy(lineInfo).result(ResponseResult.LOOP).build();
			default: {
				return AFTER_RESULT_NEXT;
			}
		}
	}
	
	
	@Scene(sceneName = LineBotConstant.REPORTVIEW_SCENE_VIEW)
	public List<Message> myReportView(LinePostInfoDto lineInfo, UserEntity userInfo) {
		Message message = new TextMessage("日付:2017/04/08\n17:30~18:00 自社にてミーティング\n18:30~20:30 新人歓迎会\n日付:2017/04/XX\nここに入力した内容を表示します。\n最大文字数は１日につき１６０文字です。");
		return Arrays.asList(message);
	}


	
	
	

}
