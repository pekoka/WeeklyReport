package jp.co.netscs.weeklyreport.linesystem.regist;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import jp.co.netscs.weeklyreport.linesystem.common.ChapterManager;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.ResponseScene;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.Chapter;
import jp.co.netscs.weeklyreport.linesystem.common.annotation.Scene;
import jp.co.netscs.weeklyreport.linesystem.common.daos.UserDao;
import jp.co.netscs.weeklyreport.linesystem.common.dtos.ResponseSceneResultDto;
import jp.co.netscs.weeklyreport.linesystem.common.dtos.LinePostInfoDto;
import jp.co.netscs.weeklyreport.linesystem.common.entitis.UserEntity;
import jp.co.netscs.weeklyreport.linesystem.common.util.LineBotConstant;
import jp.co.netscs.weeklyreport.linesystem.common.util.LineMessageUtils;

@Transactional
@Chapter(name = LineBotConstant.CHAPTER_REGIST, startScene = LineBotConstant.REGIST_SCENE_START)
public class RegistServiceImpl extends RegistService {

	@Autowired
	UserDao userDao;
	
	public RegistServiceImpl(@Autowired ChapterManager manager) {
		super(manager);
	}

	@Override
	@Scene(name = LineBotConstant.REGIST_SCENE_START, next = LineBotConstant.REGIST_SCENE_GROUPSELECT)
	public List<Message> start(LinePostInfoDto lineInfo) {
		UserEntity userInfo = UserEntity.builder().admin(false).group(null).lineId(lineInfo.getUserId()).name(null).build();
		userDao.save(userInfo);
		Message welcome = new TextMessage("ようこそエス・シー・エス週報BOTへ");
		Message message = LineMessageUtils.generateConfirm("ユーザ登録", "新規登録を行います。\n管理者権限が必要ですか？", LineBotConstant.YES, LineBotConstant.NO);
		return Arrays.asList(welcome, message);
	}
	
	@Override
	@ResponseScene(target = LineBotConstant.REGIST_SCENE_START)
	public ResponseSceneResultDto startAfter(LinePostInfoDto lineInfo, UserEntity userInfo) {
		userInfo.setAdmin(lineInfo.getText().equals("はい"));
		userDao.save(userInfo);
		return AFTER_RESULT_NEXT;
	}

	@Override
	@Scene(name = LineBotConstant.REGIST_SCENE_GROUPSELECT, next = LineBotConstant.REGIST_SCENE_INPUTNAME)
	public List<Message> groupSelect(LinePostInfoDto lineInfo, UserEntity userInfo) {
		Message message = LineMessageUtils.generateConfirm("グループ選択", "所属グループを選択してください。", LineBotConstant.SCS_GROUP1, LineBotConstant.SCS_GROUP2);
		return Arrays.asList(message);
	}
	
	@Override
	@ResponseScene(target = LineBotConstant.REGIST_SCENE_GROUPSELECT)
	public ResponseSceneResultDto groupSelectAfter(LinePostInfoDto lineInfo, UserEntity userInfo) {
		userInfo.setGroup(lineInfo.getText());
		userDao.save(userInfo);
		return AFTER_RESULT_NEXT;
	}

	@Override
	@Scene(name = LineBotConstant.REGIST_SCENE_INPUTNAME, next = LineBotConstant.REGIST_SCENE_CONFIRMREGIST)
	public List<Message> inputName(LinePostInfoDto lineInfo, UserEntity userInfo) {
		return Arrays.asList(new TextMessage("名前を入力してください。"));
	}
	
	@Override
	@ResponseScene(target = LineBotConstant.REGIST_SCENE_INPUTNAME)
	public ResponseSceneResultDto inputNameAfter(LinePostInfoDto lineInfo, UserEntity userInfo) {
		userInfo.setName(lineInfo.getText());
		userDao.save(userInfo);
		return AFTER_RESULT_NEXT;
	}

	@Override
	@Scene(name = LineBotConstant.REGIST_SCENE_CONFIRMREGIST, next = LineBotConstant.REGIST_SCENE_REGISTCOMP)
	public List<Message> confrimRegist(LinePostInfoDto lineInfo, UserEntity userInfo) {
		Message message = LineMessageUtils.generateConfirm("登録内容確認", "ユーザ名:" + userInfo.getName() + "\nグループ:" + userInfo.getGroup() + "\n管理者権限:" + (userInfo.getAdmin() ? "あり":"なし") , "登録", "キャンセル");
		return Arrays.asList(message);
	}
	
	@Override
	@ResponseScene(target = LineBotConstant.REGIST_SCENE_CONFIRMREGIST)
	public ResponseSceneResultDto confrimRegistAfter(LinePostInfoDto lineInfo, UserEntity userInfo) {
		return AFTER_RESULT_NEXT;
	}

	@Override
	@Scene(name = LineBotConstant.REGIST_SCENE_REGISTCOMP)
	public List<Message> registComplite(LinePostInfoDto lineInfo, UserEntity userInfo) {
		TextMessage text = null;
		if (lineInfo.getText().equals("登録")) {
			text = new TextMessage("登録が完了しました。");
		} else {
			text = new TextMessage("最初からやり直してください。");
			userDao.delete(userInfo);
		}
		return Arrays.asList(text);
	}

}
