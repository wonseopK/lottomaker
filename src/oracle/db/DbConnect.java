package oracle.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {
	//static final String ORACLEDRIVER="oracle.jdbc.driver.OracleDriver";
	static final String ORACLEDRIVER="oracle.jdbc.OracleDriver";//�Ѵ� �ȴ�

	//����Ŭ Ŭ���忡 19c �߰��ѻ���� �߰�C:\Users\tagli\Desktop\bitJava\OracleCloud
	static final String ORACLE_CLOUD="jdbc:oracle:thin:@bitcamp_high?TNS_ADMIN=c:/"
			+ "Users/tagli/Desktop/bitJava/OracleCloud";

	//��Į�� ����Ŭ ������ ��� ��� �߰�
	static final String ORACLE_LOCAL="jdbc:oracle:thin:@localhost:1521:xe";

	//������
	public DbConnect() {
		try {
			Class.forName(ORACLEDRIVER);//Class.forName���� ����Ŭ����̹� �ε�
		} catch (ClassNotFoundException e) {
			System.out.println("����Ŭ ����̹� ����:" + e.getMessage());
		}
	}

	public Connection getLocalOracle() {
		Connection conn = null;
		try {
			//�ε������Ǿ����� ����̹� �޴���������ؼ� ����Ŭdb�� Ŀ��Ʈ�Ѵ�
			conn = DriverManager.getConnection(ORACLE_LOCAL, "oracleId", "oraclePw");
		} catch (SQLException e) {
			System.out.println("���� ����Ŭ ���� ����:" + e.getMessage());
		}
		//������ ������ Ŀ�ؼ��� �������ش�
		return conn;

	}

	public Connection getCloudOracle() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(ORACLE_CLOUD, "cloudId", "cloudPw");
		} catch (SQLException e) {
			System.out.println("���� ����Ŭ ���� ����:" + e.getMessage());
		}
		return conn;

	}

	//close �޼���� �� 4��, �����ε� �޼���
	//ResultSet����
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
	//ResultSet����
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