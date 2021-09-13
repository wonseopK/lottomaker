package loginproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import oracle.db.DbConnect;

public class LoginSystemDBModel {
	//iv 
	DbConnect db = new DbConnect();
	Connection conn = null;

	//CREATE
	int addUserToDB (String tableName, String ID, String PW, 
			String NAME, String EMAIL, String MOBILE){
		//1? •?ƒ ?‚½?…?‹œ 2?´ë©”ì¼ì¤‘ë³µë°œìƒ?‹œ 3?•„?´?””ê°? ?´ë¯? ?‹¤ë¥¸ê³³?— ë¡œê·¸?¸ 4ë¯¸ìˆ˜?–‰
		int errorNum = 0;
		String sql = "";
		boolean errorCheck = false;
		switch (tableName) {
		case "ACCOUNTS":
			sql = "INSERT INTO ACCOUNTS VALUES (SEQ_ACC.NEXTVAL,?, ?, ?, ?, ?, 'n')";
			errorNum = 1;
			break;
		case "LOGINUSERINFO":
			sql = "INSERT INTO LOGINUSERINFO VALUES (?, ?, ?, ?, ?)";
			errorNum = 2;
			break;
		default:
			errorCheck = true;
			break;
		}
		if(errorCheck) {
			System.out.println("ë©”ë‰´?— ?—†?Š” ê°’ì„ ?…? ¥?–ˆ?Šµ?‹ˆ?‹¤.");
		}

		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ID);
			ps.setString(2, PW);
			ps.setString(3, NAME);
			ps.setString(4, EMAIL);
			ps.setString(5, MOBILE);
			int n = ps.executeUpdate();
			if(n != 0) {
				return 1;//? •?ƒ?‚½?…?‹œ
			}
		} catch (SQLException e1) {
			if(e1 instanceof java.sql.SQLIntegrityConstraintViolationException && errorNum == 1) {
				return 2; //?šŒ?›ê°??…ì¤? ?´ë©”ì¼ ì¤‘ë³µ ë°œìƒ?‹œ
			}else if(e1 instanceof java.sql.SQLIntegrityConstraintViolationException && errorNum == 2) {
				LoginSystemMain.loginOn = false;
				return 3; //ë¡œê·¸?¸ì¤‘ì— ?•„?´?””ê°? ?´ë¯? ?‹¤ë¥¸ê³³?— ë¡œê·¸?¸ ?˜?—ˆ?„Â‹Âš
			}
			e1.printStackTrace();
		} finally {
			db.dbClose(ps, conn);
		}
		return 4;//ë¯¸ì‹¤?–‰?‹œ
	}

	//READ
	public ArrayList<String> getSubscriberEmail() {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT EMAIL FROM ACCOUNTS WHERE SUBSCRIBE = 'y' ";
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while(rs.next()) {
				String email = rs.getString("EMAIL");
				list.add(email);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(rs, ps, conn);
		}
		return list;
	}


	public Vector<MemberInfo> getMemberInfoForTable()
	{
		Vector<MemberInfo> list = new Vector<MemberInfo>();
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT ID, NAME, EMAIL, SUBSCRIBE FROM ACCOUNTS ORDER BY SUBSCRIBE";

		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next())
			{
				//db?—?„œ ?•˜?‚˜?˜ ? ˆì½”ë“œë¥? ?½?–´?„œ dto?— ?„£?Š”?‹¤
				MemberInfo member =new MemberInfo();
				member.setId(rs.getString("ID"));
				member.setName(rs.getString("NAME"));
				member.setEmail(rs.getString("EMAIL"));
				member.setSubscribe(rs.getString("SUBSCRIBE"));
				//list ?— dto ì¶”ê?
				list.add(member);				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.dbClose(rs, ps, conn);
		}
		return list;
	}



	public MemberInfo getMemberInfo(String memberIv, String userInfo) {
		//memberIb ?—?Š” id, pw, name, email, mobile?´ ?˜¬?ˆ˜?ˆ?‹¤
		//userInfo ?—?Š” ?„ ?ƒ?•œ memberIv?—?„œ ?›?•˜?Š” ?œ ???˜ ? •ë³´ë?? ?…? ¥?•œ?‹¤.
		//1ë°˜í™˜?‹œ ì¡´ì œ?•˜ì§? ?•Š?Š” ? •ë³?
		//2ë°˜í™˜?‹œ ì¡´ì œ?•˜?Š” ? •ë³?
		//3ë°˜í™˜?‹œ ?´ë¯? ë¡œê·¸?¸ ?˜?–´?ˆ?Š” ?œ ??
		String sql = "";
		boolean errorCheck = false;
		switch (memberIv) {
		case "id":
			sql = "SELECT ID, PW, NAME, EMAIL, MOBILE, SUBSCRIBE FROM ACCOUNTS WHERE ID = ?";

			break;
		case "pw":
			sql = "SELECT ID, PW, NAME, EMAIL, MOBILE, SUBSCRIBE  FROM ACCOUNTS WHERE PW = ?";

			break;
		case "name":
			sql = "SELECT ID, PW, NAME, EMAIL, MOBILE, SUBSCRIBE  FROM ACCOUNTS WHERE NAME = ?";

			break;
		case "email":
			sql = "SELECT ID, PW, NAME, EMAIL, MOBILE, SUBSCRIBE  FROM ACCOUNTS WHERE EMAIL = ?";

			break;
		case "mobile":
			sql = "SELECT ID, PW, NAME, EMAIL, MOBILE, SUBSCRIBE  FROM ACCOUNTS WHERE MOBILE = ?";

			break;

		default:
			errorCheck = true;
			break;
		}

		if(errorCheck) {
			System.out.println("ë©”ë‰´?— ?—†?Š” ê°’ì„ ?…? ¥?–ˆ?Šµ?‹ˆ?‹¤.");
		}
		MemberInfo member = null;
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userInfo);
			rs = ps.executeQuery();

			if(rs.next()) {
				String id = rs.getString("ID");
				String pw = rs.getString("PW");
				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				String mobile = rs.getString("MOBILE");
				member = new MemberInfo(id, pw, name, email, mobile);
				member.setSubscribe(rs.getString("SUBSCRIBE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.dbClose(rs, ps, conn);
		}
		return member;
	}

	int CheckUserInfoFromDB(String memberIv, String userInfo) {
		//memberIv ?—?Š” id, pw, name, email, mobile?´ ?˜¬?ˆ˜?ˆ?‹¤
		//memberiv ?— managerê°??˜¬ê²½ìš° DB manager table?—?„œ ê²??ƒ‰?•œ?‹¤.
		//userInfo ?—?Š” ?„ ?ƒ?•œ memberIb?— ?•´?‹¹?•˜?Š” ?œ ???˜ ? •ë³´ë?? ?…? ¥?•œ?‹¤.

		//1ë°˜í™˜?‹œ ì¡´ì œ?•˜ì§? ?•Š?Š” ? •ë³?
		//2ë°˜í™˜?‹œ ì¡´ì œ?•˜?Š” ? •ë³?
		String sql = "";
		boolean errorCheck = false;
		switch (memberIv) {
		case "id":
			sql = "SELECT ID FROM ACCOUNTS WHERE ID = ?";

			break;
		case "pw":
			sql = "SELECT PW FROM ACCOUNTS WHERE PW = ?";

			break;
		case "name":
			sql = "SELECT NAME FROM ACCOUNTS WHERE NAME = ?";

			break;
		case "email":
			sql = "SELECT EMAIL FROM ACCOUNTS WHERE EMAIL = ?";

			break;
		case "mobile":
			sql = "SELECT MOBILE FROM ACCOUNTS WHERE MOBILE = ?";

			break;
		case "manager":
			sql = "SELECT ID FROM MANAGER WHERE ID = ?";

			break;

		default:
			errorCheck = true;
			break;
		}

		if(errorCheck) {
			System.out.println("ë©”ë‰´?— ?—†?Š” ê°’ì„ ?…? ¥?–ˆ?Šµ?‹ˆ?‹¤.");
		}
		Connection conn = db.getLocalOracle();
		PreparedStatement ps= null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userInfo);
			int n = ps.executeUpdate();
			System.out.println(n);
			if(n == 0) {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}
		return 2;
	}

	//UPDATE
	int changeLoginUserInfo(String memberIv, String OldUserInfo, String NewUserInfo) {
		//ë³?ê²½í•  memberIvë§´ë²„ë³??ˆ˜ë¥? ê³ ë¥´ê³? OldUserInfoë³?ê²½ì „ê°?, NewUserInfoë³?ê²½í• ê°’ì„ ?…? ¥
		//0? •?ƒ ?‹¤?–‰ 1ì¤‘ë³µ?´ë©”ì¼ë°œìƒ 2?ˆ˜?–‰?•ˆÂ‰?‘›ëºåš¥
		//ë¹„ë?ë²ˆí˜¸ ë³?ê²½ì‹œ olduserinfo?— ë³?ê²½í•  ???ƒ?˜ ?•„?´?””ë¥? ?…? ¥
		String sql = "";
		boolean errorCheck = false;
		switch (memberIv) {
		case "email":
			sql = " UPDATE ACCOUNTS SET EMAIL = ? WHERE EMAIL = ? ";
			break;
		case "mobile":
			sql = " UPDATE ACCOUNTS SET MOBILE = ? WHERE MOBILE = ? ";
			break;
		case "pw":
			sql = " UPDATE ACCOUNTS SET PW = ? WHERE ID = ?";
			break;
		default:
			errorCheck = true;
			break;
		}
		if(errorCheck) {
			System.out.println("ë©”ë‰´?— ?—†?Š” ê°’ì„ ?…? ¥?–ˆ?Šµ?‹ˆ?‹¤.");
		}
		conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, NewUserInfo);
			ps.setString(2, OldUserInfo);
			ps.execute();
			return 0;//? •?ƒ?‹¤?–‰
		} catch (SQLException e) {
			if(e instanceof java.sql.SQLIntegrityConstraintViolationException) {
				return 1;//ì¤‘ë³µ ?´ë©”ì¼?¼ ê²½ìš°
			}
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}
		return 2;//?˜¤ë¥˜ë°œ?ƒ
	}

	public int subscirbeLotto(String answer, String loginUserId) {
		//1ë¯¸ì…? ¥ 2? •?ƒ?ˆ˜?–‰êµ¬ë…?‹ ì²? 3? •?ƒ?ˆ˜?–‰êµ¬ë…ì·¨ì†Œ 5?ˆ˜?–‰?•ˆ?¨ 4ì§?? •ê°’ì™¸ ?…? ¥
		int answer2 = 0;
		if(answer.length() == 0 || answer == null ) {
			return 1;
		}
		if(!answer.equalsIgnoreCase("y")&&!answer.equalsIgnoreCase("n")) {
			return 4;
		}
		if(answer.equalsIgnoreCase("y")) answer2 = 1;
		if(answer.equalsIgnoreCase("n")) answer2 = 2;

		String sql = "UPDATE ACCOUNTS SET SUBSCRIBE = ? WHERE ID = ?";
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, answer);
			ps.setString(2, loginUserId);
			int n = ps.executeUpdate();
			if(n != 0) {
				if(answer2 == 1) return 2;
				if(answer2 == 2) return 3;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}

		return 5;
	}
	//DELETE
	public void cleanLoginUserTable(String userId) {
		//ë¡œê·¸?¸?œ ??ê°? ë¡œê·¸?•„?›ƒ?´?‚˜ ?”„ë¡œê·¸?¨ ì¢…ë£Œ?‹œ ë¡œê·¸?¸?œ ???°?´?„°?—?„œ ? œê±°í•©?‹ˆ?‹¤.
		String sql = "DELETE FROM LOGINUSERINFO WHERE ID = ?";
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			System.out.println("ë¡œê·¸?¸ DB?—?„œ ?˜„? œ ë¡œê·¸?¸ ê³„ì • ?‚­? œ?™„ë£?");
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			db.dbClose(ps, conn);
		}

	}


	public void DeleteLoginUserAccoung(String userId) {
		String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			System.out.println("?•´?‹¹ê³„ì • ACCOUNTS DB ?—?„œ ?‚­? œ?™„ë£?");
		} catch (SQLException e) {
			e.printStackTrace();
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}
	}

}
