package loginproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lottoNumberMaker.LottoMakerUserFrame;
import lottoNumberMaker.LottoNumberMaker;
import oracle.db.DbConnect;

public class LoginSystemMain extends JFrame implements LoginProjectDesign {
	//iv
	JButton loginButton, logoutButton, joinButton, findAccountButton, 
	updateAccountButton, startProgrambButton, managerInfoButton;
	JLabel idLabel, passwardLabel, loginCheck;
	JTextField idTextField;
	JPasswordField passwardfField;
	DbConnect db = new DbConnect();
	LoginSystemDBModel dbModel = new LoginSystemDBModel();

	//로그인유저
	MemberInfo memberInfo = null;

	//cv
	//현재 로그인한 유저 정보
	public static String loginUserId = "";
	//boolean타입으로 만약 해당 프레임 실행시 true로 변경하고 true 일경우 중복실행을 금지시킨다
	public static boolean loginOn = false;
	public static boolean logOut = true;
	public static boolean applicationOn = false;
	public static boolean joinFrameOn = false;
	public static boolean updateFrameOn = false;
	public static boolean findAccountframeOn = false;
	public static boolean managerInfoFrameOn = false;
	public static boolean programOn = false;
	public static boolean confirmTimeExpired = false;


	//constructor
	LoginSystemMain(){
		super("Login");
		this.setBounds(500, 100, 300, 450);
		this.setDesign();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
					dbModel.cleanLoginUserTable(loginUserId);
				System.exit(0);
			}
		});
	}

	//methods
	private void setDesign() {
		this.setLayout(null);
		swingLableDesign();
		swingTextDesign();
		swingLoginButtonDesign();
		swingLogoutButtonDesign();
		swingStartProgramButtonDesign();
		swingJoinButtonDesign();
		swingUpdateButtonDesign();
		swingFindAccountButtonDesign();

	}
	//methods
	@Override
	public void swingLableDesign() {
		// TODO Auto-generated method stub
		loginCheck = new JLabel("LOGOUT");
		loginCheck.setBounds(100, 10, 100, 30);
		this.add(loginCheck);
		idLabel = new JLabel("ID");
		idLabel.setBounds(50, 50, 100, 30);
		this.add(idLabel);
		idLabel = new JLabel("PW");
		idLabel.setBounds(50, 100, 100, 30);
		this.add(idLabel);
	}

	@Override
	public void swingTextDesign() {
		// TODO Auto-generated method stub
		idTextField = new JTextField();
		idTextField.setBounds(85, 50 , 130, 30);
		this.add(idTextField);
		passwardfField = new JPasswordField();
		passwardfField.setBounds(85, 100 , 130, 30);
		this.add(passwardfField);
	}

	private void swingLoginButtonDesign() {
		loginButton = new JButton("LOGIN");
		loginButton.setBounds(40, 150, 100, 30 );
		this.add(loginButton);
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(joinFrameOn||findAccountframeOn||programOn) {
					showMessageToUser("실행중인 양식이 있습니다.");
					return;
				}
				String userId = idTextField.getText().trim();
				String userPw = passwardfField.getText().trim();
				if(idTextField.getText().length() == 0 || 
						passwardfField.getText().length() == 0) 
				{
					showMessageToUser("값을 입력해주세요");
					return;
				}
				if(loginOn) {
					LoginSystemMain.this.showMessageToUser("이미 로그인 되었습니다");
					return;
				}
				int n = dbModel.CheckUserInfoFromDB("id", userId);
				if(n == 1) {
					showMessageToUser("존제하지 않는 아이디 입니다.");
					return;
				} else if(n == 2) {
					MemberInfo loginUser = dbModel.getMemberInfo("id", userId);
					if(!userPw.equals(loginUser.getPassward())) {
						showMessageToUser("비밀번호가 일치하지 않습니다");
						return;
					}
					loginUserId = loginUser.getId();
					int n1 = dbModel.addUserToDB("LOGINUSERINFO", loginUser.getId(), loginUser.getPassward(), 
							loginUser.getName(), loginUser.getEmail(), loginUser.getMobile());
					System.out.println(n1);
					if(n1 == 1) {
						showMessageToUser("로그인 성공");
						loginCheck.setText("LOGIN");
						loginOn = true;
						logOut = false;
					}else if (n1 == 3) {
						showMessageToUser("이미 로그인 되어있는 유저입니다.");
						loginOn = false;
					}
				}



			}
		});
	}

	private void swingLogoutButtonDesign() {
		logoutButton = new JButton("LOGOUT");
		logoutButton.setBounds(160, 150, 100, 30);
		this.add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(logOut) {
					showMessageToUser("이미 로그아웃 되었습니다");
					return;
				}
				if(programOn||updateFrameOn) {
					showMessageToUser("실행중인 창을 종료후 로그아웃하세요");
					return;
				}
				LoginSystemMain.this.idTextField.setText("");
				LoginSystemMain.this.passwardfField.setText("");
				loginCheck.setText("LOGOUT");
				loginOn = false;
				logOut = true;
				dbModel.cleanLoginUserTable(loginUserId);
				showMessageToUser("로그아웃 되었습니다");
			}
		});
	}
	private void swingStartProgramButtonDesign() {
		startProgrambButton = new JButton("START");
		startProgrambButton.setBounds(85, 350, 130, 30);
		this.add(startProgrambButton);

		startProgrambButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(programOn) {
					showMessageToUser("프로그램이 이미실행중입니다.");
					return;
				}
				if(!loginOn) {
					showMessageToUser("로그인 해주세요");
					return;
				} else {
					int n = dbModel.CheckUserInfoFromDB("manager", loginUserId);
					System.out.println(n);
					if(n == 1) {
						new LottoMakerUserFrame();
					}else if(n == 2) {
						System.out.println("관리자 모드실행");
						new LottoNumberMaker();
					}

				}
			}
		});
	}
	private void swingJoinButtonDesign() {
		joinButton = new JButton("JOIN");
		joinButton.setBounds(85, 200, 130, 30 );
		this.add(joinButton);
		joinButton.addActionListener(new ActionListener() {
			//joing button click
			@Override
			public void actionPerformed(ActionEvent e) {
				if(loginOn) {
					showMessageToUser("이미 로그인 되었습니다.");
					return;
				}
				if(joinFrameOn) {
					showMessageToUser("이미 실행중입니다.");
					return;
				}
				joinFrameOn = true;
				new JoinFrame();
			}
		});
	}
	private void swingUpdateButtonDesign() {
		updateAccountButton = new JButton("UPDATE");
		updateAccountButton.setBounds(85, 250, 130, 30);
		this.add(updateAccountButton);
		updateAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!loginOn) {
					showMessageToUser("로그인 해주세요");
					return;
				}
				if(updateFrameOn) {
					showMessageToUser("이미 실행중입니다.");
					return;
				}
				updateFrameOn = true;
				new UpdateFrame();
				//어카운트 클래스 하나 만들고 로그인시 하나 생성시켜주고 그걸바탕으로 정보수정
			}
		});
	}
	private void swingFindAccountButtonDesign() {
		findAccountButton = new JButton("FIND ACCOUNT");
		findAccountButton.setBounds(85, 300, 130, 30);
		this.add(findAccountButton);
		findAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				if(loginOn) {
					showMessageToUser("이미 로그인 되었습니다");
					return;
				}
				if(findAccountframeOn) {
					showMessageToUser("이미 실행중입니다.");
					return;
				}
				if(logOut) {
					findAccountframeOn = true;
					new FindAccountFrame();
				}
			}
		});
	}

	private void showMessageToUser(String s) {
		JOptionPane.showMessageDialog(LoginSystemMain.this, s);
	}


	public static void main(String[] args) {
		new LoginSystemMain();


	}

}
