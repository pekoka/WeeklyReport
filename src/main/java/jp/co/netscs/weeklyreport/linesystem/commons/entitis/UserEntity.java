package jp.co.netscs.weeklyreport.linesystem.commons.entitis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import jp.co.netscs.weeklyreport.linesystem.commons.util.ScsGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author katumi
 *
 */
@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Table(name="User")
public class UserEntity {
	
	/**
	 * Line Messageing APIのユーザID
	 */
	@Id
	@Column(length=32, nullable = false)
	private String lineId;
	
	/**
	 * 所属グループ
	 */
	@Enumerated(EnumType.STRING)
	private ScsGroup group;
	
	/**
	 * ユーザ登録名
	 */
	private String name;
	
	/**
	 * 管理者権限
	 */
	private Boolean admin;
	
}