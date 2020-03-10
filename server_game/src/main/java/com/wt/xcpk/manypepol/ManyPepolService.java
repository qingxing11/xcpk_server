package com.wt.xcpk.manypepol;

import java.util.ArrayList;

import com.wt.naval.vo.user.Player;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.classicgame.ConfigClassicGame;
import com.wt.xcpk.vo.JackpotData;

import model.GameWanren;

public interface ManyPepolService
{
	ArrayList<PlayerSimpleData> getPlayers();

	int getState();

	void sitdown(Player player);

	void checkPoker(Player player);

	void playerFollowBet(Player player);

	void raiseBet(Player player, int betNum);

	void payBet(Player player, int pos, int betNum);

	void enter(Player player);

	ArrayList<PlayerSimpleData> getTableList();

	void comparerPoker(Player player, int pos,boolean isAllIn);

	void fold(Player player);

	void allIn(Player player);

	void mGameStandUp(Player player);

	void leaveRoom(Player player, boolean isTV);
	
	ConfigClassicGame getConfig();

	int getAllBet();

	boolean reconnect(Player player,boolean isLogin);

	JackpotData getJackpotData();

	long getJackpotNum();

	int getRoundNum();

	void refreshKillroomConfig();

	GameWanren getWanrenConfig();
}
