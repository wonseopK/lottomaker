package oracle.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {
	//static final String ORACLEDRIVER="oracle.jdbc.driver.OracleDriver";
	static final String ORACLEDRIVER="oracle.jdbc.OracleDriver";//둘다 된다

	//오라클 클라우드에 19c 추가한사람만 추가C:\Users\tagli\Desktop\bitJava\OracleCloud
	static final String ORACLE_CLOUD="jdbc:oracle:thin:@bitcamp_high?TNS_ADMIN=c:/"
			+ "Users/tagli/Desktop/bitJava/OracleCloud";

	//로칼로 오라클 연결할 모든 사람 추가
	static final String ORACLE_LOCAL="jdbc:oracle:thin:@localhost:1521:xe";

	//생성자
	public DbConnect() {
		try {
			Class.forName(ORACLEDRIVER);//Class.forName으로 오라클드라이버 로딩
		} catch (ClassNotFoundException e) {
			System.out.println("오라클 드라이버 실패:" + e.getMessage());
		}
	}

	public Connection getLocalOracle() {
		Connection conn = null;
		try {
			//로딩성공되었으니 드라이버 메니저를사용해서 오라클db와 커넥트한다
			conn = DriverManager.getConnection(ORACLE_LOCAL, "oracleId", "oraclePw");
		} catch (SQLException e) {
			System.out.println("로컬 오라클 연결 실패:" + e.getMessage());
		}
		//성공시 성공된 커넥션을 리턴해준다
		return conn;

	}

	public Connection getCloudOracle() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(ORACLE_CLOUD, "cloudId", "cloudPw");
		} catch (SQLException e) {
			System.out.println("로컬 오라클 연결 실패:" + e.getMessage());
		}
		return conn;

	}

	//close 메서드는 총 4개, 오버로딩 메서드
	//ResultSet없음
	public void dbClose(Statement stmt, Connection conn) {
		try {
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {}
	}
	public void dbClose(PreparedStatement pstmt, Connection conn) {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {}
	}
	//ResultSet있음
	public void dbClose(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {}
	}
	public void dbClose(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {}
	}

}