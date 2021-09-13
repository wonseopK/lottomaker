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
		//1?��?�� ?��?��?�� 2?��메일중복발생?�� 3?��?��?���? ?���? ?��른곳?�� 로그?�� 4미수?��
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
			System.out.println("메뉴?�� ?��?�� 값을 ?��?��?��?��?��?��.");
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
				return 1;//?��?��?��?��?��
			}
		} catch (SQLException e1) {
			if(e1 instanceof java.sql.SQLIntegrityConstraintViolationException && errorNum == 1) {
				return 2; //?��?���??���? ?��메일 중복 발생?��
			}else if(e1 instanceof java.sql.SQLIntegrityConstraintViolationException && errorNum == 2) {
				LoginSystemMain.loginOn = false;
				return 3; //로그?��중에 ?��?��?���? ?���? ?��른곳?�� 로그?�� ?��?��?��
			}
			e1.printStackTrace();
		} finally {
			db.dbClose(ps, conn);
		}
		return 4;//미실?��?��
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
				//db?��?�� ?��?��?�� ?��코드�? ?��?��?�� dto?�� ?��?��?��
				MemberInfo member =new MemberInfo();
				member.setId(rs.getString("ID"));
				member.setName(rs.getString("NAME"));
				member.setEmail(rs.getString("EMAIL"));
				member.setSubscribe(rs.getString("SUBSCRIBE"));
				//list ?�� dto 추�?
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
		//memberIb ?��?�� id, pw, name, email, mobile?�� ?��?��?��?��
		//userInfo ?��?�� ?��?��?�� memberIv?��?�� ?��?��?�� ?��???�� ?��보�?? ?��?��?��?��.
		//1반환?�� 존제?���? ?��?�� ?���?
		//2반환?�� 존제?��?�� ?���?
		//3반환?�� ?���? 로그?�� ?��?��?��?�� ?��??
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
			System.out.println("메뉴?�� ?��?�� 값을 ?��?��?��?��?��?��.");
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
		//memberIv ?��?�� id, pw, name, email, mobile?�� ?��?��?��?��
		//memberiv ?�� manager�??��경우 DB manager table?��?�� �??��?��?��.
		//userInfo ?��?�� ?��?��?�� memberIb?�� ?��?��?��?�� ?��???�� ?��보�?? ?��?��?��?��.

		//1반환?�� 존제?���? ?��?�� ?���?
		//2반환?�� 존제?��?�� ?���?
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
			System.out.println("메뉴?�� ?��?�� 값을 ?��?��?��?��?��?��.");
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
		//�?경할 memberIv맴버�??���? 고르�? OldUserInfo�?경전�?, NewUserInfo�?경할값을 ?��?��
		//0?��?�� ?��?�� 1중복?��메일발생 2?��?��?��?��뺐嚥
		//비�?번호 �?경시 olduserinfo?�� �?경할 ???��?�� ?��?��?���? ?��?��
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
			System.out.println("메뉴?�� ?��?�� 값을 ?��?��?��?��?��?��.");
		}
		conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, NewUserInfo);
			ps.setString(2, OldUserInfo);
			ps.execute();
			return 0;//?��?��?��?��
		} catch (SQLException e) {
			if(e instanceof java.sql.SQLIntegrityConstraintViolationException) {
				return 1;//중복 ?��메일?�� 경우
			}
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}
		return 2;//?��류발?��
	}

	public int subscirbeLotto(String answer, String loginUserId) {
		//1미입?�� 2?��?��?��?��구독?���? 3?��?��?��?��구독취소 5?��?��?��?�� 4�??��값외 ?��?��
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
		//로그?��?��??�? 로그?��?��?��?�� ?��로그?�� 종료?�� 로그?��?��???��?��?��?��?�� ?��거합?��?��.
		String sql = "DELETE FROM LOGINUSERINFO WHERE ID = ?";
		Connection conn = db.getLocalOracle();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			System.out.println("로그?�� DB?��?�� ?��?�� 로그?�� 계정 ?��?��?���?");
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
			System.out.println("?��?��계정 ACCOUNTS DB ?��?�� ?��?��?���?");
		} catch (SQLException e) {
			e.printStackTrace();
			cleanLoginUserTable(LoginSystemMain.loginUserId);
		} finally {
			db.dbClose(ps, conn);
		}
	}

}
