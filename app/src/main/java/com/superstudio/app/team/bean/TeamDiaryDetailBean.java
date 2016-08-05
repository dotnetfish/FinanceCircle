package com.superstudio.app.team.bean;

import com.superstudio.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("oschina")
public class TeamDiaryDetailBean extends Entity {

    @XStreamAlias("diary")
    private TeamDiary teamDiary;

    public TeamDiary getTeamDiary() {
        return teamDiary;
    }

    public void setTeamDiary(TeamDiary teamDiary) {
        this.teamDiary = teamDiary;
    }

}
