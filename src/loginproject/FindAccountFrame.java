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
				//���� �������� ����Ƚ�Ű�� ���� �����Ӹ� ���� DISPOSE_ON_CLOSE
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
		//1�� �����Ұ�� if���߿� ���Է¶Ǵ� ���¾��̵�߻� 2�� �����Ұ�� �����ܰ� ������డ��
		String nameForMissAccount = nameTextField.getText().trim();
		String emailForMissAccount = emailTextField.getText().trim();

		if(nameForMissAccount.length() == 0) {
			showMessageToUser("�̸��� �Է����ּ���");
			return 1;
		}
		if(emailForMissAccount.length() == 0) {
			showMessageToUser("�̸����� �Է����ּ���");
			return 1;
		}

		int nForName = dbModel.CheckUserInfoFromDB("name", nameForMissAccount);
		int nForEmail = dbModel.CheckUserInfoFromDB("email", emailForMissAccount);
		if(nForName == 1) {
			showMessageToUser("��ϵ��� ���� �̸��Դϴ�.");
			return 1;
		}
		if(nForEmail == 1) {
			showMessageToUser("��ϵ��� ���� �̸����Դϴ�.");
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
					showMessageToUser("�̸��� ���̵� ��ġ�ϴ� ȸ���� �����ϴ�.");
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
					showMessageToUser("�̸��� ���̵� ��ġ�ϴ� ȸ���� �����ϴ�.");
					return;
				}
				String id = JOptionPane.showInputDialog("���Խ� ����� ���̵� �Է��ϼ���");
				if(id.equals(member.getId())) {
					String confirmNumber = getUserConfirmNumber();
					String subject = "������ �ζ� ȸ������ ��û�Ͻ� �����Դϴ�";
					String text = "ȸ������ ������ȣ�� " + confirmNumber + "�Դϴ� ���������� Ȯ�����ּ���";
					sendEmailToUser.sendEmailToUser(emailForMissAccount, subject, text);
					String confirmNumberCheckString = JOptionPane.showInputDialog("������ȣ�� �߼��߽��ϴ�."
							+ " ������ȣ�� �Է����ּ���");
					if(confirmNumberCheckString == null) {
						showMessageToUser("������ȣ�� �Է����ּ���");
						return;
					}

					if(confirmNumber.equals(confirmNumberCheckString)) {
						showMessageToUser("ȸ������ ��й�ȣ�� " + member.getPassward() + "�Դϴ� �����Ѻ����� "
								+ "���ο� ��й�ȣ�� �������ּ���");
					} else {
						showMessageToUser("���� ��ȣ�� �ٸ��ϴ� �ٽ��������ּ���");
					}

				}else {
					showMessageToUser("���Խ� ��ϵ��� ���� ���̵��Դϴ�.");
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
