package com.javalab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class sms {

	public static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	public static final String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";

	public static Connection con = null;
	public static PreparedStatement pstmt = null;
	public static ResultSet rs = null;

	public static String oracleId = "sms";
	public static String oraclePwd = "1234";

	public static void main(String[] args) {

		// 1. 디비 접속 메소드호출
		connectDB();

		// 2. 학과 조회
		getDeptList();
		// 3. 학생조회
		getStuList();
		// 4. 교수조회
		getProfessorList();
		// 5. 강좌조회
		getCourseList();
		// 6. 수강조회
		getEnrollmentList();
		// 7. 김광식 학생이 수강한 강좌를 조회(강좌명, 학과명 포함)
		getstukimList();
		// 8. 자료구조 강좌를 수강한 모든 학생을 조회
		getdataclassList();

		// 9. 자원반납
		closeResource();

	}// main e

	// 8. 자료구조 강좌를 수강한 모든 학생을 조회
	private static void getdataclassList() {
		String sql = "";
		try {
			sql += "select c.course_name, s.stu_id, s.stu_name, s.grade, e.score ";
			sql += " from tbl_enrollment e, tbl_course c, tbl_student s ";
			sql += " where e.course_id = c.course_id ";
			sql += " and e.stu_id = s.stu_id ";
			sql += " and c.course_name = '자료구조'";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println();
			System.out.println("8. 자료구조 강좌를 수강한 모든 학생을 조회");
			System.out.println();

			while (rs.next()) {
				System.out.println(rs.getString("course_name") + "\t" + rs.getInt("stu_id") + "\t"
						+ rs.getString("stu_name") + "\t" + rs.getInt("grade") + "\t" + rs.getString("score"));
			}

		} catch (SQLException e) {
			System.out.println("SQL ERR ! : " + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}
	}

	// 7. 김광식 학생이 수강한 강좌를 조회(강좌명, 학과명 포함)
	private static void getstukimList() {
		String sql = "";
		try {
			sql += "select e.ENROLLMENT_ID, e.course_id, c.course_name, s.stu_id, s.stu_name, d.dept_name ";
			sql += " from tbl_enrollment e, tbl_course c, tbl_student s, tbl_dept d ";
			sql += " where e.course_id = c.course_id ";
			sql += " and e.stu_id = s.stu_id ";
			sql += " and s.dept_id = d.dept_id ";
			sql += " and s.stu_name = '김광식'";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println();
			System.out.println("7. 김광식 학생이 수강한 강좌 조회");
			System.out.println();

			while (rs.next()) {
				System.out.println(rs.getInt("enrollment_id") + "\t" + rs.getInt("course_id") + "\t"
						+ rs.getString("course_name") + "\t" + rs.getInt("stu_id") + "\t" + rs.getString("stu_name")
						+ "\t" + rs.getString("dept_name"));
			}

		} catch (SQLException e) {
			System.out.println("SQL ERR ! : " + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}
	}

	// 6. 수강조회
	private static void getEnrollmentList() {
		String sql = "";
		try {
			sql += "select E.ENROLLMENT_ID,E.STU_ID, S.STU_NAME,E.COURSE_ID,";
			sql += " C.COURSE_NAME,E.PROFESSOR_ID,P.PROFESSOR_NAME,E.SCORE,E.ENROLLMENT_DATE,R.ROOM_NAME";
			sql += " from tbl_enrollment e, Tbl_course c, TBL_CLASSROOM r,  tbl_professor p,tbl_student s";
			sql += " where e.course_id = c.course_id";
			sql += " and e.professor_id = p.professor_id";
			sql += " and e.stu_id = s.stu_id";
			sql += " and e.course_id = c.course_id";
			sql += " and c.room_id = r.room_id";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println();
			System.out.println("6. 수강 조회");
			System.out.println();

			while (rs.next()) {
				System.out.println(rs.getInt("enrollment_id") + rs.getInt("STU_ID") + "\t" + rs.getString("STU_NAME")
						+ "\t" + rs.getInt("COURSE_ID") + "\t" + rs.getString("COURSE_NAME") + "\t"
						+ rs.getInt("PROFESSOR_ID") + "\t" + rs.getString("PROFESSOR_NAME") + "\t"
						+ rs.getString("SCORE") + "\t" + rs.getDate("ENROLLMENT_DATE") + "\t"
						+ rs.getString("ROOM_NAME"));
			}

		} catch (SQLException e) {
			System.out.println("SQL ERR ! : " + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}

	}

	// 5. 강좌조회
	private static void getCourseList() {
		String sql = "";
		try {
			sql += "select c.COURSE_ID, c.COURSE_NAME, c.CREDIT, p.professor_name,p.PROFESSOR_ID,";
			sql += " c.C_DATE, r.ROOM_ID, r.ROOM_NAME";
			sql += " from TBL_COURSE c, tbl_classroom r, tbl_professor p";
			sql += " where c.room_id = r.room_id ";
			sql += " and c.professor_id = p.professor_id";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println();
			System.out.println("5. 강좌 조회");
			System.out.println();
			while (rs.next()) {
				System.out.println(rs.getInt("course_id") + rs.getString("course_name") + rs.getInt("credit")
						+ rs.getString("professor_name") + "\t" + rs.getInt("professor_id") + "\t"
						+ rs.getDate("c_date") + "\t" + rs.getInt("room_id") + "\t" + rs.getString("room_name"));
			}

		} catch (SQLException e) {
			System.out.println("SQL ERR ! : " + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}

	}

	// 4. 교수조회
	private static void getProfessorList() {
		String sql = "";
		try {
			sql += "select p.PROFESSOR_ID, p.PROFESSOR_NAME, p.RESIDENT_ID,";
			sql += " D.DEPT_ID, D.DEPT_NAME, p.HIREDATE";
			sql += " from TBL_PROFESSOR p, tbl_dept D";
			sql += " WHERE p.dept_id = d.dept_id";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println();
			System.out.println(" 4. 교수조회");
			System.out.println();

			while (rs.next()) {
				System.out.println(rs.getInt("professor_id") + "\t" + rs.getString("professor_name") + "\t"
						+ rs.getString("resident_id") + "\t" + rs.getInt("dept_id") + "\t" + rs.getString("dept_name")
						+ "\t" + rs.getString("hiredate"));
			}

		} catch (SQLException e) {
			System.out.println("SQL ERR :" + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}

	}

	// 3. 학생조회
	private static void getStuList() {

		String sql = "";
		try {
			sql += "select s.stu_id, s.stu_name, s.resident_id, s.gender, ";
			sql += " s.address, s.grade,";
			sql += " d.dept_id, d.dept_name";
			sql += " from tbl_student s, tbl_dept d";
			sql += " where s.dept_id = d.dept_id";

			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("3. 학생 조회");
			System.out.println();
			while (rs.next()) {
				System.out.println(
						rs.getInt("stu_id") + "\t" + rs.getString("stu_name") + "\t" + rs.getString("resident_id")
								+ "\t" + rs.getString("gender") + "\t" + rs.getString("address") + "\t"
								+ rs.getInt("grade") + "\t" + rs.getInt("dept_id") + "\t" + rs.getString("dept_name"));

			}

		} catch (SQLException e) {
			System.out.println("SQL ERR :" + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}

	}

	// 2. 학과 조회
	private static void getDeptList() {
		String sql = "";
		try {
			sql = "select * from tbl_dept";

			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("2. 학과 조회");
			System.out.println();
			while (rs.next()) {
				System.out.println(rs.getInt("dept_id") + "\t" + rs.getString("dept_name"));

			}
		} catch (SQLException e) {
			System.out.println("SQL ERR !" + e.getMessage());
		} finally {
			closeResource(pstmt, rs);
		}

	}

	// 1. 디비 접속 메소드호출
	private static void connectDB() {
		try {
			Class.forName(DRIVER_NAME);
			System.out.println("1. 드라이버 로드 성공!");

			con = DriverManager.getConnection(DB_URL, oracleId, oraclePwd);
			System.out.println("2. 커넥션 객체 생성 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 ERR ! : " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQL ERR ! : " + e.getMessage());
		}

	}

	private static void closeResource(PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			System.out.println("Result, PreparedStatement 자원 반납 완료");
		} catch (SQLException e) {
			System.out.println("자원 해제 ERR ! : " + e.getMessage());
		}

	}

	private static void closeResource() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println("자원 해제 ERR ! : " + e.getMessage());
		}

	}

}// class e
