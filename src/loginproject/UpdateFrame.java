package loginproject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import oracle.db.DbConnect;

public class UpdateFrame extends JFrame {
	//iv 
	//swing디자인
	JTextField idTextField, emailTextField, mobileTextField, nameTextField;
	JLabel idTextFieldLabel, emailTextFieldabel,
	mobileTextFieldLabel, nameTextFieldLabel;
	JLabel idDisplayLable, emailDisplayLable,
	mobileDisplayLable,nameDisplayLable;
	JButton emailUpdateButton, mobileUpdateButton, changePwButton, 
	deleteAccountButton;
	//db
	DbConnect db = new DbConnect();
	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	MemberInfo memberInfo = dbModel.getMemberInfo("id", LoginSystemMain.loginUserId);

	//update를 실행할 로그인 유저 정보
	private String loginUserId = memberInfo.getId();
	private String loginUserPw = memberInfo.getPassward();
	private String loginUserName = memberInfo.getName();
	private String loginUserEmail = memberInfo.getEmail();
	private String loginUserMobile = memberInfo.getMobile();

	//constructor
	public UpdateFrame() {
		super("UPDATE");
		this.setBounds(800, 100, 450, 400);
		this.setDesign();
		LoginSystemMain.updateFrameOn = true;
		showUserDetailOnFrame();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//update 프레임만 종료하기
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				//update 프레임 종료 main에 알리기
				LoginSystemMain.updateFrameOn = false;
			}
		});

		this.setVisible(true);
	}


	//methods area
	private void showUserDetailOnFrame() {
		idDisplayLable.setText(loginUserId);
		nameDisplayLable.setText(loginUserName);
		emailDisplayLable.setText(loginUserEmail);
		mobileDisplayLable.setText(loginUserMobile);
	}

	private void swingLableDesign() {
		// TODO Auto-generated method stub
		//join label
		idTextFieldLabel = new JLabel("ID");
		idTextFieldLabel.setBounds(30, 50, 100, 30);
		this.add(idTextFieldLabel);
		nameTextFieldLabel = new JLabel("NAME");
		nameTextFieldLabel.setBounds(30, 100, 100, 30);
		this.add(nameTextFieldLabel);
		emailTextFieldabel = new JLabel("EMAIL");
		emailTextFieldabel.setBounds(30, 150, 100, 30);
		this.add(emailTextFieldabel);
		mobileTextFieldLabel = new JLabel("MOBILE");
		mobileTextFieldLabel.setBounds(30, 200, 100, 30);
		this.add(mobileTextFieldLabel);
	}


	private void swingDisplayLableDesign() {
		idDisplayLable = new JLabel();
		idDisplayLable.setBounds(110, 50 , 230, 30);
		idDisplayLable.setBorder(new LineBorder(Color.black, 1));
		this.add(idDisplayLable);
		nameDisplayLable = new JLabel();
		nameDisplayLable.setBorder(new LineBorder(Color.black, 1));
		nameDisplayLable.setBounds(110, 100 , 230, 30);
		this.add(nameDisplayLable);
		emailDisplayLable = new JLabel();
		emailDisplayLable.setBorder(new LineBorder(Color.black, 1));
		emailDisplayLable.setBounds(110, 150 , 230, 30);
		this.add(emailDisplayLable);
		mobileDisplayLable = new JLabel();
		mobileDisplayLable.setBorder(new LineBorder(Color.black, 1));
		mobileDisplayLable.setBounds(110, 200 , 230, 30);
		this.add(mobileDisplayLable);
	}

	private void swingDeleteAccountButtonDesign() {
		deleteAccountButton = new JButton("DELETE ACCOUNT");
		deleteAccountButton.setBounds(90, 300, 170, 30);
		this.add(deleteAccountButton);
		deleteAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String pw = "";
				int num = JOptionPane.showConfirmDialog(deleteAccountButton, 
						"정말로 삭제하시 겠습니까?");
				if(num == 0) {
					pw = JOptionPane.showInputDialog(deleteAccountButton, 
							loginUserId + "님의 비밀번호를 입력하세요");
					if(pw == null) {
						showMessageToUser("비밀번호를 입력하지 않았습니다");
						return;
					}
					if(pw.equals(loginUserPw)) {
						dbModel.cleanLoginUserTable(loginUserId);
						dbModel.DeleteLoginUserAccoung(loginUserId);
						showMessageToUser("삭제되었습니다.");
						System.exit(0);

					}else {
						showMessageToUser("비밀번호가 일치하지 않습니다.");
						return;
					}
				}
			}
		});
	}
	private void swingChangePasswardButtonDesign() {
		changePwButton = new JButton("CHANGE PASSWARD");
		changePwButton.setBounds(90, 250, 170, 30);
		this.add(changePwButton);
		changePwButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentPw = JOptionPane.showInputDialog(changePwButton, 
						"현제 비밀번호를 입력해주세요");
				String newPw = "";
				String newPwCheck = "";

				if(currentPw == null) {
					showMessageToUser("비밀번호를 입력해주세요");
					return;
				}

				if(currentPw.equals(loginUserPw)) {
					newPw = JOptionPane.showInputDialog(changePwButton, 
							"새로운 비밀번호를 입력해주세요");
					if(newPw.length() == 0) {
						showMessageToUser("비밀번호를 입력해주세요");
						return;
					}	
					newPwCheck = JOptionPane.showInputDialog(changePwButton, 
							"새로운 비밀번호를 다시한번 입력해주세요");
					if(newPwCheck.length() == 0) {
						showMessageToUser("비밀번호를 입력해주세요");
						return;
					}
				}else {
					showMessageToUser("현제 비밀번호가 일치하지 않습니다.");
					return;
				}
				if(newPw.equals(newPwCheck)) {
					//비밀번호 변경 메소드 실행
					dbModel.changeLoginUserInfo("pw", loginUserId, newPw);
					showMessageToUser("새로운 비밀번호가로 변경되었습니다. 다시로그인해주세요");
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dbModel.cleanLoginUserTable(loginUserId);
					System.exit(0);
				}else {
					showMessageToUser("새로운 비밀번호가 일치하지 않습니다.");
					return;
				}





			}
		});
	}
	private void swingChangeEmailButtonDesign() {
		emailUpdateButton = new JButton("CHANGE");
		emailUpdateButton.setBounds(345, 150, 80, 30);
		this.add(emailUpdateButton);
		//joption inputdialog이용
		emailUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = JOptionPane.showInputDialog("변경할 이메일 주소를 입력해주세요");
				if(email == null || email.length() == 0) {
					showMessageToUser("값을 입력하지 않았습니다.");
					return;
				}

				int n = dbModel.changeLoginUserInfo("email", loginUserEmail, email);
				if(n == 0) {
					UpdateFrame.this.emailDisplayLable.setText(email);
					showMessageToUser("정상적으로 변경 되었습니다.");
				} else if (n == 1) {
					showMessageToUser("이미 존재하는 이메일 입니다.");
				}
			}
		});
	}
	private void swingChangeMobilButtonDesign() {
		mobileUpdateButton = new JButton("CHANGE");
		mobileUpdateButton.setBounds(345, 200, 80, 30);
		this.add(mobileUpdateButton);
		//joption inputdialog이용
		mobileUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String mobile = JOptionPane.showInputDialog("변경할 전화번호를 입력해주세요");
				if(mobile == null || mobile.length() == 0) {
					showMessageToUser("값을 입력하지 않았습니다.");
					return;
				}
				dbModel.changeLoginUserInfo("mobile", loginUserMobile, mobile);
				UpdateFrame.this.mobileDisplayLable.setText(mobile);
				showMessageToUser("정상적으로 변경 되었습니다.");
			}
		});
	}

	private void setDesign() {
		this.setLayout(null);
		swingLableDesign();
		swingChangeMobilButtonDesign();
		swingChangeEmailButtonDesign();
		swingDeleteAccountButtonDesign();
		swingChangePasswardButtonDesign();
		swingDisplayLableDesign();
	}

	private void showMessageToUser(String s) {
		JOptionPane.showMessageDialog(this, s);
	}


}
