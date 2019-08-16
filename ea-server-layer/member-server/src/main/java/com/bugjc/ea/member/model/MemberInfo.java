package com.bugjc.ea.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员信息表
 * @author aoki
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberInfo implements Serializable{
    /**
     * 外键
     */
    private String memberId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 年龄
     */
    private int age;

}