package loginproject;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import oracle.db.DbConnect;


public class JoinFrame extends JFrame implements LoginProjectDesign {
	//iv 
	JTextField idTextField, emailTextField, mobileTextField, nameTextField;
	JLabel idLabel, pwLabel, pwCheckLabel, emaiLabel, mobileLabel, nameLabel;
	JPasswordField passwardTextField2, passwardCheckTextField;
	JButton joinButton, sameIdCheckButton;
	DbConnect db = new DbConnect();
	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	boolean idCheckButtonUsed = false;

	//const
	JoinFrame() {
		super("Join");
		this.setBounds(800, 100, 300, 500);
		this.setDesign();
		LoginSystemMain.joinFrameOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//				Login.this.setVisible(true);
				LoginSystemMain.joinFrameOn = false;
				//메인 프레임은 종료안시키고 현재 프레임만 종료 DISPOSE_ON_CLOSE
				JoinFrame.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
		this.setVisible(true);

	}

	//methods 
	@Override
	public void swingLableDesign() {
		idLabel = new JLabel("ID");
		idLabel.setBounds(30, 50, 100, 30);
		this.add(idLabel);
		pwLabel = new JLabel("PW");
		pwLabel.setBounds(30, 150, 100, 30);
		this.add(pwLabel);
		pwCheckLabel = new JLabel("PWCHECK");
		pwCheckLabel.setBounds(30, 200, 100, 30);
		this.add(pwCheckLabel);
		nameLabel = new JLabel("NAME");
		nameLabel.setBounds(30, 250, 250, 30);
		this.add(nameLabel);
		emaiLabel = new JLabel("EMAIL");
		emaiLabel.setBounds(30, 300, 300, 30);
		this.add(emaiLabel);
		mobileLabel = new JLabel("MOBILE");
		mobileLabel.setBounds(30, 350, 350, 30);
		this.add(mobileLabel);
	}

	@Override
	public void swingTextDesign() {
		idTextField = new JTextField();
		idTextField.setBounds(100, 50 , 130, 30);
		this.add(idTextField);
		passwardTextField2 = new JPasswordField(20);
		passwardTextField2.setBounds(100, 150 , 130, 30);
		this.add(passwardTextField2);
		passwardCheckTextField = new JPasswordField(20);
		passwardCheckTextField.setBounds(100, 200 , 130, 30);
		this.add(passwardCheckTextField);
		nameTextField = new JTextField();
		nameTextField.setBounds(100, 250 , 130, 30);
		this.add(nameTextField);
		emailTextField = new JTextField();
		emailTextField.setBounds(100, 300 , 130, 30);
		this.add(emailTextField);
		mobileTextField = new JTextField();
		mobileTextField.setBounds(100, 350 , 130, 30);
		this.add(mobileTextField);
	}

	private void swingSameIdCheckButtonDesign() {
		sameIdCheckButton = new JButton("CHECK");
		sameIdCheckButton.setBounds(110, 100, 80, 30 );
		this.add(sameIdCheckButton);
		sameIdCheckButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idCheckButtonUsed = true;
				if(idTextField.getText().length() == 0) {
					showMessageToUser("값을 입력해주세요");
					idCheckButtonUsed = false;
					return;
				}
				if(idTextField.getText().length()>20) {
					showMessageToUser("아이디는 20자리 이하로 만들어주세요");
					idCheckButtonUsed = false;
					return;
				}

				int n = dbModel.CheckUserInfoFromDB("id", idTextField.getText());
				System.out.println(n);
				if(n == 1) {
					showMessageToUser("가입 가능한 아이디입니다.");
					idCheckButtonUsed = true;
					return;
				}else if(n == 2){
					showMessageToUser("중복되는 아이디입니다 다시입력해주세요");
					idCheckButtonUsed = false;
					return;
				}
			}
		});
	}

	private void swingJoinButtonDesign() {
		joinButton = new JButton("Join");
		joinButton.setBounds(110, 400, 80, 30 );
		this.add(joinButton);
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userId = idTextField.getText().trim();
				String userPw = passwardTextField2.getText().trim();
				String userPwCheckString = passwardCheckTextField.getText().trim();
				String userName = nameTextField.getText().trim();
				String userEmail = emailTextField.getText();
				String userMobile = mobileTextField.getText().trim();
				boolean problemOn = false;
				/*
				 * 1 아이디 길이초과
				 * 2 비밀번호 길이초과
				 * 3 이름 길이초과
				 * 4 이메일 길이초과
				 * 5 모바일 길이초과
				 * 6 이메일 형식오류 @와.을사용안함
				 * 7 정상실행
				 * 8 전화번호에 숫자입력
				 */
				int n2 = checkAccountInfo();
				System.out.println(n2);
				switch (n2) {
				case 1:
					showMessageToUser("아이디는 20자리 이하로 만들어주세요");
					problemOn = true;
					break;
				case 2:
					showMessageToUser("비밀번호는 20자리 이하로 만들어주세요");
					problemOn = true;
					break;
				case 3:
					showMessageToUser("이름은 20자리 이하로 만들어주세요");
					problemOn = true;
					break;
				case 4:
					showMessageToUser("이메일은 40자리 이하로 만들어주세요");
					problemOn = true;
					break;
				case 6:
					showMessageToUser("이메일의 형식이 올바르지 않습니다.");
					problemOn = true;
					break;
				case 5:
					showMessageToUser("모바일은 15자리 이하로 만들어주세요");
					problemOn = true;
					break;
				case 8:
					showMessageToUser("전화번호는 반드시 숫자로 입력하세요");
					problemOn = true;
					break;
				}
				if(problemOn) return;


				if(userId.length() == 0 || userPw.length()==0 || 
						userName.length()==0|| userEmail.length()==0|| 
						userMobile.length()==0) 
				{
					JoinFrame.this.showMessageToUser("공백없이 모두 입력해주세요");
					return;
				}
				if(!userPwCheckString.equals(userPw)) {
					JoinFrame.this.showMessageToUser("비밀번호확인을 다시해주세요");
					return;
				}
				if(!idCheckButtonUsed) {
					JoinFrame.this.showMessageToUser("아이디 중복 체크를 해주세요");
					return;
				}

				int n = dbModel.addUserToDB("ACCOUNTS", userId, userPw, userName, userEmail, userMobile);
				if(n == 1) {
					cleanTextField();
					idCheckButtonUsed = false;
					showMessageToUser("축하합니다 " + userName +"회원가입이 완료되었습니다.");
				} else if (n == 2) {
					showMessageToUser("이미 존제하는 이메일 입니다.");
					return;
				}
			}
		});
	}
	private void setDesign() {
		this.setLayout(null);
		swingLableDesign();
		swingTextDesign();
		swingJoinButtonDesign();
		swingSameIdCheckButtonDesign();
	}

	private void showMessageToUser(String s) {
		JOptionPane.showMessageDialog(JoinFrame.this, s);
	}

	private void cleanTextField() {
		idTextField.setText("");
		passwardTextField2.setText("");
		passwardCheckTextField.setText("");
		nameTextField.setText("");
		emailTextField.setText("");
		mobileTextField.setText("");
	}

	private int checkAccountInfo() {
		String id = idTextField.getText();
		String pw = passwardTextField2.getText();
		String name = nameTextField.getText();
		String email = emailTextField.getText();
		String mobile = mobileTextField.getText();
		/*
		 * 1 아이디 길이초과
		 * 2 비밀번호 길이초과
		 * 3 이름 길이초과
		 * 4 이메일 길이초과
		 * 5 모바일 길이초과
		 * 6 이메일 형식오류 @와.을사용안함
		 * 7 정상실행
		 * 8 전화번호에 숫자입력
		 */
		if(id.length()>20) {
			return 1;
		} else if (pw.length()>20 || pw.length()>4) {
			return 2;
		} else if (name.length()>20) {
			return 3;
		} else if (email.length()>50) {
			return 4;
		} else if (mobile.length()>15  ) {
			return 5;

		} else if (!email.contains("@") || !email.contains(".")) {
			return 6;
		}
		try {
			int n = Integer.valueOf(mobile); 
			int n2 = n + 1;
		} catch (NumberFormatException e) {
			return 8;
		}

		return 7;



	}
}
