package com.bsu.system.tool;

import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.sun.rowset.CachedRowSetImpl;

public class DataHelper {
	private static DataHelper instance = null;
	public static DataHelper getInstance(){
		if(instance==null)
			instance = new DataHelper();
		return instance;
	}
	
	private DataHelper(){}
	/**
	 * 获得用户的排行数据
	 * @param gameid	游戏id
	 * @param zoneid	大区id
	 * @param userid	用户id
	 * @return			返回用户数据的集合
	 * @throws Exception
	 */
	public CachedRowSetImpl getUserPkData(String gameid,String zoneid,String userid) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select id,pkvalue from pk where gameid = ? and zoneid = ? and userid = ?");
		
		CachedRowSetImpl rs = null;
		ArrayList<ColumnData> ldata
			=U.lcd(new String[]{"gameid","zoneid","userid"}, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER}
				, new String[]{gameid,zoneid,userid});
		rs = ODB.query(sb.toString(), ldata);
		
		return rs;
	}
	
	
	/**
	 * 获得用户的排行榜数据
	 * @param gameid		游戏id
	 * @param zoneid		大区id
	 * @param userid		用户id
	 * @param limit			数据显示条数上限
	 * @return				返回查询出的数据集合
	 * @throws Exception 
	 * 
	 *使用的sql语句,在此备注:
	 *		select CAST(rank AS SIGNED) as rank,pkvalue,userid,u.nickname,headicon,equipicon1,equipicon2 from
				(select (@rank:=@rank+1) as rank,dd.pkvalue,dd.userid from 
					(
						select pkvalue,userid from 
						(select pkvalue,userid from pk
						where gameid=1 and zoneid = 1
						order by pkvalue asc) ad,
						(select (@userpkvalue:=pkvalue) from pk where gameid=1 and zoneid=1 and userid=1)c
						where pkvalue>@userpkvalue 
						limit 10 -- 此处limit表示高于自己分数的显示多少
						union all
						select pkvalue,userid from 
							(select pkvalue,userid from pk
							where gameid=1 and zoneid = 1
							order by pkvalue desc) ad,
							(select (@userpkvalue:=pkvalue) from pk where gameid=1 and zoneid=1 and userid=1)c
							where pkvalue<=@userpkvalue 
					) dd
					,(select (@rank:=0)) r
					order by pkvalue desc
					limit 20 -- 此处limit表示一共显示多少行分数
				) od
				inner join `user` u on u.id = od.userid
				order by rank asc
	 * 
	 */
	public CachedRowSetImpl getUserRankData(String gameid,String zoneid,String userid,int limit) throws Exception{
		StringBuffer sb = new StringBuffer();
//		sb.append("select CAST(rank AS SIGNED) as rank,pkvalue,userid,u.nickname,headicon,equipicon1,equipicon2 from ")
//				.append("(select (@rank:=@rank+1) as rank,dd.pkvalue,dd.userid from  ")
//				.append("( ")
//						.append("select pkvalue,userid from ") 
//						.append("(select pkvalue,userid from pk ")
//						.append("where gameid=? and zoneid = ? ")
//						.append("order by pkvalue asc) ad, ")
//						.append("(select (@userpkvalue:=pkvalue) from pk where gameid=? and zoneid=? and userid=?)c ")
//						.append("where pkvalue>@userpkvalue ") 
//						.append("limit ? ")
//						.append("union all ")
//						.append("select pkvalue,userid from ") 
//							.append("(select pkvalue,userid from pk ")
//							.append("where gameid=? and zoneid = ? ")
//							.append("order by pkvalue desc) ad, ")
//							.append("(select (@userpkvalue:=pkvalue) from pk where gameid=? and zoneid=? and userid=?)c ")
//							.append("where pkvalue<=@userpkvalue ") 
//					.append(") dd ")
//					.append(",(select (@rank:=0)) r ")
//					.append("order by pkvalue desc ")
//					.append("limit ? ")
//				.append(") od ")
//				.append("inner join `user` u on u.id = od.userid ")
//				.append("order by rank asc ");

//		int limit1 = limit/2;
//		ArrayList<ColumnData> ldata 
//			= U.lcd(new String[]{"gameid","zoneid","gameid","zoneid","userid","limit","gameid","zoneid","gameid","zoneid","userid","limit"}
//				, new int[]{Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,}
//				, new String[]{gameid,zoneid,gameid,zoneid,userid,String.valueOf(limit1),gameid,zoneid,gameid,zoneid,userid,String.valueOf(limit)});
		
//		sb.append("select CAST((@rank:=@rank+1) as signed) as rank , pkvalue,userid ") 
//			.append("from pk p,(select @rank:=0) r ")
//			.append("where gameid=? and zoneid = ? ")
//			.append("order by pkvalue desc ");
		
		
		sb.append("select pp.rank,pp.pkvalue,pp.userid,u.nickname from ")
			.append("(select CAST((@rank:=@rank+1) as signed) as rank ,pkvalue,userid ")
			.append("from pk p,(select @rank:=0) r  ")
			.append("where p.gameid=? and p.zoneid = ? ") 
			.append("order by p.pkvalue desc) pp ")
			.append("inner join `user` u on pp.userid = u.id ");
		
		CachedRowSetImpl rs = null;
		
		ArrayList<ColumnData> ldata
			=U.lcd(new String[]{"gameid","zoneid"}, new int[]{Types.INTEGER,Types.INTEGER}
				, new String[]{gameid,zoneid});
		rs = ODB.query(sb.toString(), ldata);
		
		int iuserid = U.string2Int(userid, -1);
		int rank = 0;
//		int pkvalue = 0;
		//获得当前用户的数据
		rs.beforeFirst();
		while(rs.next()){
			//如果当前不为用户数据,则继续下一条
			if(rs.getInt("userid")!=iuserid)
				continue;
			rank = rs.getInt("rank");					//获得用户当前的名次
//			pkvalue = rs.getInt("pkvalue");				//获得用户当前的成绩
			break;										//获得以后跳出
		}

		//获得当前用户的前后排名数据
		int[] ranklimit = this.rankLimit(limit, rank, rs.size());
		int uprank = ranklimit[0];				//取的名次的上限
		int downrank = ranklimit[1];			//取的名次的下限
		rs.beforeFirst();
		while(rs.next()){
			int r = rs.getInt("rank");
			//名次范围外的数据全部删除掉
			if(r<uprank || r>downrank)
				rs.deleteRow();
			
		}
		//过滤掉无用的数据,返回该集合
		return rs;
	}
	
	/**
	 * 查询当前的排名数据，如果查询到用户自己的排名数据,将排名数据缓存到session中
	 * @param rs			名次数据集合
	 * @param userid		用户的id
	 * @param request		请求
	 * @throws Exception
	 */
	public void cacheRandValue(CachedRowSetImpl rs,String userid,HttpServletRequest request) throws Exception{
		rs.beforeFirst();
		while(rs.next()){
			if(String.valueOf(rs.getInt("userid")).equals(userid)){
				//将当前数据库中的名次缓存到session中
				HttpSession s = request.getSession(false);
				s.setAttribute("pkvalue", rs.getInt("pkvalue"));
				s.setAttribute("rank",	rs.getString("rank"));
			}
			else
				continue;
		}
	}
	
	/**
	 * 判断当前用户是否有昵称
	 * @param request	用于获得session的request对象
	 * @return			当返回值为true表示有昵称,为false表示没有昵称
	 */
	public boolean hasNickName(HttpServletRequest request){
		String nickname = U.getSS(request, "nickname");
		if(nickname==null || nickname.equals(""))
			return false;
		return true;
	}
	
	/**
	 * 确定名次数据的取值范围
	 * @param limit			//数据的取值条数上限
	 * @param rank			//玩家的排名
	 * @param size			//总共的条数
	 * @return	返回整数数组,第一个元素为名次上限,第二个元素为名次下限
	 */
	private int[] rankLimit(int limit,int rank,int size){
		if(limit<=0) limit = 20;
		int hlimit = limit/2;
		int uprank = 1;
		int downrank = 0;
		//当名次靠近排行榜上部
		if(hlimit>=rank){
			int add = ((hlimit-rank)<0)?0:(hlimit-rank);
			downrank = rank+hlimit+add+((limit)%2);
		}
		//名次在排行榜下部
		else if((size-rank)<hlimit){
			uprank = rank;
			downrank = rank+hlimit;
			int add = (limit-(size-rank)<0)?0:limit-(size-rank);
			uprank-=add;
		}
		//排行榜在中部
		else {
			uprank = rank-hlimit;
			downrank = (limit==1)?rank:(rank+hlimit)-((limit+1)%2);
//			downrank = (hlimit==1)?(rank+hlimit-1):rank+hlimit;
		}
		
//		int uprank = (rank-halflimit<0)?0:(rank-halflimit);		//取的名次的上限
//		int downrank = rank+halflimit;							//取的名次的下限
		
		return new int[]{uprank,downrank};
	}
}
