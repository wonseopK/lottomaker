package loginproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FindAccountFrame extends JFrame implements LoginProjectDesign {
	//iv 
	JTextField emailTextField, nameTextField;
	JLabel emaiLabel, nameLabel;
	JButton idFindButton, pwFindButton;
	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	SendEmail sendEmailToUser = new SendEmail();

	//const
	public FindAccountFrame() {
		super("FindAccount");
		this.setBounds(800, 100, 300, 300);
		this.setDesign();
		LoginSystemMain.findAccountframeOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//					Login.this.setVisible(true);
				LoginSystemMain.findAccountframeOn = false;
				//메인 프레임은 종료안시키고 현재 프레임만 종료 DISPOSE_ON_CLOSE
				FindAccountFrame.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
		this.setVisible(true);

	}

	private void setDesign() {
		this.setLayout(null);
		swingLableDesign();
		swingTextDesign();
		swingIdFindButton();
		swingPwFindButton();
	}

	@Override
	public void swingLableDesign() {
		nameLabel = new JLabel("NAME");
		nameLabel.setBounds(30, 50, 100, 30);
		this.add(nameLabel);
		emaiLabel = new JLabel("EMAIL");
		emaiLabel.setBounds(30, 100, 100, 30);
		this.add(emaiLabel);		
	}

	@Override
	public void swingTextDesign() {
		nameTextField = new JTextField();
		nameTextField.setBounds(85, 50 , 130, 30);
		this.add(nameTextField);
		emailTextField = new JTextField();
		emailTextField.setBounds(85, 100 , 130, 30);
		this.add(emailTextField);		
	}

	private int userEmailIdCheck() {
		//1을 리턴할경우 if문중에 미입력또는 없는아이디발생 2를 리턴할경우 다음단계 정상실행가능
		String nameForMissAccount = nameTextField.getText().trim();
		String emailForMissAccount = emailTextField.getText().trim();

		if(nameForMissAccount.length() == 0) {
			showMessageToUser("이름을 입력해주세요");
			return 1;
		}
		if(emailForMissAccount.length() == 0) {
			showMessageToUser("이메일을 입력해주세요");
			return 1;
		}

		int nForName = dbModel.CheckUserInfoFromDB("name", nameForMissAccount);
		int nForEmail = dbModel.CheckUserInfoFromDB("email", emailForMissAccount);
		if(nForName == 1) {
			showMessageToUser("등록되지 않은 이름입니다.");
			return 1;
		}
		if(nForEmail == 1) {
			showMessageToUser("등록되지 않은 이메일입니다.");
			return 1;
		}

		return 2;

	}


	private void swingIdFindButton() {
		idFindButton = new JButton("FIND ID");
		idFindButton.setBounds(75, 150, 150, 30);
		this.add(idFindButton);
		idFindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nameForMissAccount = nameTextField.getText().trim();
				String emailForMissAccount = emailTextField.getText().trim();
				int n = userEmailIdCheck();
				if(n == 1) {
					return;
				}
				MemberInfo member = dbModel.getMemberInfo("email", emailForMissAccount);
				MemberInfo member2 = dbModel.getMemberInfo("name", nameForMissAccount);
				if(!member.getId().equals(member2.getId())) {
					showMessageToUser("이름과 아이디가 일치하는 회원이 없습니다.");
					return;
				}

				showMessageToUser(member.getId());
			}


		});
	}

	private void swingPwFindButton() {
		pwFindButton = new JButton("FIND pw");
		pwFindButton.setBounds(75, 200, 150, 30);
		this.add(pwFindButton);
		pwFindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String emailForMissAccount = emailTextField.getText().trim();
				String nameForMissAccount = nameTextField.getText().trim();
				int n = userEmailIdCheck();
				if(n == 1) {
					return;
				}
				MemberInfo member = dbModel.getMemberInfo("email", emailForMissAccount);
				MemberInfo member2 = dbModel.getMemberInfo("name", nameForMissAccount);
				if(!member.getId().equals(member2.getId())) {
					showMessageToUser("이름과 아이디가 일치하는 회원이 없습니다.");
					return;
				}
				String id = JOptionPane.showInputDialog("가입시 등록한 아이디를 입력하세요");
				if(id.equals(member.getId())) {
					String confirmNumber = getUserConfirmNumber();
					String subject = "오늘의 로또 회원님이 요청하신 정보입니다";
					String text = "회원님의 인증번호는 " + confirmNumber + "입니다 인증절차를 확인해주세요";
					sendEmailToUser.sendEmailToUser(emailForMissAccount, subject, text);
					String confirmNumberCheckString = JOptionPane.showInputDialog("인증번호를 발송했습니다."
							+ " 인증번호를 입력해주세요");
					if(confirmNumberCheckString == null) {
						showMessageToUser("인증번호를 입력해주세요");
						return;
					}

					if(confirmNumber.equals(confirmNumberCheckString)) {
						showMessageToUser("회원님의 비밀번호는 " + member.getPassward() + "입니다 가능한빠르게 "
								+ "새로운 비밀번호로 변경해주세요");
					} else {
						showMessageToUser("인증 번호가 다릅니다 다시인증해주세요");
					}

				}else {
					showMessageToUser("가입시 등록되지 않은 아이디입니다.");
					return;
				}

			}


		});

	}

	private String getUserConfirmNumber() {
		String confirmNumber = "";
		for(int i=0; i<4; i++) {
			confirmNumber += (int)(Math.random()*10+1)+"";
		}
		return confirmNumber;
	}

	private void showMessageToUser(String s) {
		JOptionPane.showMessageDialog(this, s);
	}

}
