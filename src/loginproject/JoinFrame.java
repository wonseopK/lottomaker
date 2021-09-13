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
				//���� �������� ����Ƚ�Ű�� ���� �����Ӹ� ���� DISPOSE_ON_CLOSE
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
					showMessageToUser("���� �Է����ּ���");
					idCheckButtonUsed = false;
					return;
				}
				if(idTextField.getText().length()>20) {
					showMessageToUser("���̵�� 20�ڸ� ���Ϸ� ������ּ���");
					idCheckButtonUsed = false;
					return;
				}

				int n = dbModel.CheckUserInfoFromDB("id", idTextField.getText());
				System.out.println(n);
				if(n == 1) {
					showMessageToUser("���� ������ ���̵��Դϴ�.");
					idCheckButtonUsed = true;
					return;
				}else if(n == 2){
					showMessageToUser("�ߺ��Ǵ� ���̵��Դϴ� �ٽ��Է����ּ���");
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
				 * 1 ���̵� �����ʰ�
				 * 2 ��й�ȣ �����ʰ�
				 * 3 �̸� �����ʰ�
				 * 4 �̸��� �����ʰ�
				 * 5 ����� �����ʰ�
				 * 6 �̸��� ���Ŀ��� @��.��������
				 * 7 �������
				 * 8 ��ȭ��ȣ�� �����Է�
				 */
				int n2 = checkAccountInfo();
				System.out.println(n2);
				switch (n2) {
				case 1:
					showMessageToUser("���̵�� 20�ڸ� ���Ϸ� ������ּ���");
					problemOn = true;
					break;
				case 2:
					showMessageToUser("��й�ȣ�� 20�ڸ� ���Ϸ� ������ּ���");
					problemOn = true;
					break;
				case 3:
					showMessageToUser("�̸��� 20�ڸ� ���Ϸ� ������ּ���");
					problemOn = true;
					break;
				case 4:
					showMessageToUser("�̸����� 40�ڸ� ���Ϸ� ������ּ���");
					problemOn = true;
					break;
				case 6:
					showMessageToUser("�̸����� ������ �ùٸ��� �ʽ��ϴ�.");
					problemOn = true;
					break;
				case 5:
					showMessageToUser("������� 15�ڸ� ���Ϸ� ������ּ���");
					problemOn = true;
					break;
				case 8:
					showMessageToUser("��ȭ��ȣ�� �ݵ�� ���ڷ� �Է��ϼ���");
					problemOn = true;
					break;
				}
				if(problemOn) return;


				if(userId.length() == 0 || userPw.length()==0 || 
						userName.length()==0|| userEmail.length()==0|| 
						userMobile.length()==0) 
				{
					JoinFrame.this.showMessageToUser("������� ��� �Է����ּ���");
					return;
				}
				if(!userPwCheckString.equals(userPw)) {
					JoinFrame.this.showMessageToUser("��й�ȣȮ���� �ٽ����ּ���");
					return;
				}
				if(!idCheckButtonUsed) {
					JoinFrame.this.showMessageToUser("���̵� �ߺ� üũ�� ���ּ���");
					return;
				}

				int n = dbModel.addUserToDB("ACCOUNTS", userId, userPw, userName, userEmail, userMobile);
				if(n == 1) {
					cleanTextField();
					idCheckButtonUsed = false;
					showMessageToUser("�����մϴ� " + userName +"ȸ�������� �Ϸ�Ǿ����ϴ�.");
				} else if (n == 2) {
					showMessageToUser("�̹� �����ϴ� �̸��� �Դϴ�.");
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
		 * 1 ���̵� �����ʰ�
		 * 2 ��й�ȣ �����ʰ�
		 * 3 �̸� �����ʰ�
		 * 4 �̸��� �����ʰ�
		 * 5 ����� �����ʰ�
		 * 6 �̸��� ���Ŀ��� @��.��������
		 * 7 �������
		 * 8 ��ȭ��ȣ�� �����Է�
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
