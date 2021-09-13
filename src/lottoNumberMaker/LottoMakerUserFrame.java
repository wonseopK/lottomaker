package lottoNumberMaker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import loginproject.LoginSystemDBModel;
import loginproject.LoginSystemMain;
import loginproject.MemberInfo;

public class LottoMakerUserFrame extends JFrame {
	//iv
	JButton lottoNumberApplyButton, lottoNumberCancelButton;
	JLabel subscription, subscriptionCondition, explainLabel, explainLabel2;

	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	MemberInfo memberInfo = dbModel.getMemberInfo("id", LoginSystemMain.loginUserId);

	//update�� ������ �α��� ���� ����
	private String loginUserId = memberInfo.getId();
	private String loginUserSubscribe = memberInfo.getSubscribe();
	private boolean subscribe;

	public LottoMakerUserFrame(){
		super("������ �ζ�");
		this.setBounds(400, 200, 300, 400);
		this.setDesign();
		LoginSystemMain.programOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//���α׷� �����Ӹ� �����ϱ�
				dbModel.cleanLoginUserTable(loginUserId);
				System.exit(0);
				//���α׷� ������ ���� main�� �˸���
			}
		});

		this.setVisible(true);
		System.out.println(subscribe);

	}

	private void setDesign() {

		this.setLayout(null);
		//label
		subscription = new JLabel("�ݰ����ϴ� ȸ������ ����");
		subscription.setBounds(80,50, 200,30);
		this.add(subscription);

		String subCondition = "";
		if(loginUserSubscribe.equalsIgnoreCase("n")) {
			subCondition = "�ζǳѹ��ޱ� �̱����� �Դϴ�";
			subscribe = false;
			System.out.println(subCondition);
		}
		if(loginUserSubscribe.equalsIgnoreCase("y")) {
			subCondition = "�ζǳѹ��ޱ� ������ �Դϴ�";
			subscribe = true;
			System.out.println(subCondition);
		}


		subscriptionCondition = new JLabel(subCondition);
		subscriptionCondition.setBounds(50,100,200,30);
		subscriptionCondition.setBorder(new LineBorder(Color.yellow,2));
		this.add(subscriptionCondition);




		explainLabel = new JLabel("�ζ� ��ȣ�� ���� ����3�ÿ�");
		explainLabel.setBounds(75, 150, 200, 30);
		this.add(explainLabel);
		explainLabel2 = new JLabel("���Խ� ��ϵ� ���Ϸ� �߼۵˴ϴ�.");
		explainLabel2.setBounds(60, 200, 200, 30);
		this.add(explainLabel2);


		//button
		lottoNumberApplyButton = new JButton("��������");
		lottoNumberApplyButton.setBounds(100, 300, 100 , 30);
		this.add(lottoNumberApplyButton);
		lottoNumberApplyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String answer = JOptionPane.showInputDialog("�������θ� �ݵ�� y�ƴϸ�n���θ� �Է����ּ���");
				int n = dbModel.subscirbeLotto(answer, loginUserId);
				//1���Է� 2������౸����û 3������౸����� 5����ȵ� 4�������� �Է�
				switch (n) {
				case 1:
					ShowMessageToUser("�������θ� �Է����ּ���");
					break;
				case 2:
					if(subscribe) {
						ShowMessageToUser("�̹� ������û�Ǿ����ϴ�");
						return;
					}
					ShowMessageToUser("������û�Ǿ����ϴ�");
					subscriptionCondition.setText("�ζǳѹ��ޱ� ������ �Դϴ�");
					subscribe = true;
					break;
				case 3:
					if(!subscribe) {
						ShowMessageToUser("�̹� ������ҵǾ����ϴ�");
						return;
					}
					ShowMessageToUser("������ҵǾ����ϴ�");
					subscriptionCondition.setText("�ζǳѹ��ޱ� �̱����� �Դϴ�");
					subscribe = false;
					break;
				case 4:
					ShowMessageToUser("�������θ� �ݵ�� y�ƴϸ� n���θ� �Է����ּ���");
					break;
				}
			}
		});


	}
	private void ShowMessageToUser(String text) {
		JOptionPane.showMessageDialog(this, text);
	}

}