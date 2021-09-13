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
	//swing������
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

	//update�� ������ �α��� ���� ����
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
				//update �����Ӹ� �����ϱ�
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				//update ������ ���� main�� �˸���
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
						"������ �����Ͻ� �ڽ��ϱ�?");
				if(num == 0) {
					pw = JOptionPane.showInputDialog(deleteAccountButton, 
							loginUserId + "���� ��й�ȣ�� �Է��ϼ���");
					if(pw == null) {
						showMessageToUser("��й�ȣ�� �Է����� �ʾҽ��ϴ�");
						return;
					}
					if(pw.equals(loginUserPw)) {
						dbModel.cleanLoginUserTable(loginUserId);
						dbModel.DeleteLoginUserAccoung(loginUserId);
						showMessageToUser("�����Ǿ����ϴ�.");
						System.exit(0);

					}else {
						showMessageToUser("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
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
						"���� ��й�ȣ�� �Է����ּ���");
				String newPw = "";
				String newPwCheck = "";

				if(currentPw == null) {
					showMessageToUser("��й�ȣ�� �Է����ּ���");
					return;
				}

				if(currentPw.equals(loginUserPw)) {
					newPw = JOptionPane.showInputDialog(changePwButton, 
							"���ο� ��й�ȣ�� �Է����ּ���");
					if(newPw.length() == 0) {
						showMessageToUser("��й�ȣ�� �Է����ּ���");
						return;
					}	
					newPwCheck = JOptionPane.showInputDialog(changePwButton, 
							"���ο� ��й�ȣ�� �ٽ��ѹ� �Է����ּ���");
					if(newPwCheck.length() == 0) {
						showMessageToUser("��й�ȣ�� �Է����ּ���");
						return;
					}
				}else {
					showMessageToUser("���� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}
				if(newPw.equals(newPwCheck)) {
					//��й�ȣ ���� �޼ҵ� ����
					dbModel.changeLoginUserInfo("pw", loginUserId, newPw);
					showMessageToUser("���ο� ��й�ȣ���� ����Ǿ����ϴ�. �ٽ÷α������ּ���");
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dbModel.cleanLoginUserTable(loginUserId);
					System.exit(0);
				}else {
					showMessageToUser("���ο� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}





			}
		});
	}
	private void swingChangeEmailButtonDesign() {
		emailUpdateButton = new JButton("CHANGE");
		emailUpdateButton.setBounds(345, 150, 80, 30);
		this.add(emailUpdateButton);
		//joption inputdialog�̿�
		emailUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = JOptionPane.showInputDialog("������ �̸��� �ּҸ� �Է����ּ���");
				if(email == null || email.length() == 0) {
					showMessageToUser("���� �Է����� �ʾҽ��ϴ�.");
					return;
				}

				int n = dbModel.changeLoginUserInfo("email", loginUserEmail, email);
				if(n == 0) {
					UpdateFrame.this.emailDisplayLable.setText(email);
					showMessageToUser("���������� ���� �Ǿ����ϴ�.");
				} else if (n == 1) {
					showMessageToUser("�̹� �����ϴ� �̸��� �Դϴ�.");
				}
			}
		});
	}
	private void swingChangeMobilButtonDesign() {
		mobileUpdateButton = new JButton("CHANGE");
		mobileUpdateButton.setBounds(345, 200, 80, 30);
		this.add(mobileUpdateButton);
		//joption inputdialog�̿�
		mobileUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String mobile = JOptionPane.showInputDialog("������ ��ȭ��ȣ�� �Է����ּ���");
				if(mobile == null || mobile.length() == 0) {
					showMessageToUser("���� �Է����� �ʾҽ��ϴ�.");
					return;
				}
				dbModel.changeLoginUserInfo("mobile", loginUserMobile, mobile);
				UpdateFrame.this.mobileDisplayLable.setText(mobile);
				showMessageToUser("���������� ���� �Ǿ����ϴ�.");
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
