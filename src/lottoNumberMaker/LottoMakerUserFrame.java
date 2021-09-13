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

	//update를 실행할 로그인 유저 정보
	private String loginUserId = memberInfo.getId();
	private String loginUserSubscribe = memberInfo.getSubscribe();
	private boolean subscribe;

	public LottoMakerUserFrame(){
		super("오늘의 로또");
		this.setBounds(400, 200, 300, 400);
		this.setDesign();
		LoginSystemMain.programOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//프로그램 프레임만 종료하기
				dbModel.cleanLoginUserTable(loginUserId);
				System.exit(0);
				//프로그램 프레임 종료 main에 알리기
			}
		});

		this.setVisible(true);
		System.out.println(subscribe);

	}

	private void setDesign() {

		this.setLayout(null);
		//label
		subscription = new JLabel("반갑습니다 회원님은 현제");
		subscription.setBounds(80,50, 200,30);
		this.add(subscription);

		String subCondition = "";
		if(loginUserSubscribe.equalsIgnoreCase("n")) {
			subCondition = "로또넘버받기 미구독중 입니다";
			subscribe = false;
			System.out.println(subCondition);
		}
		if(loginUserSubscribe.equalsIgnoreCase("y")) {
			subCondition = "로또넘버받기 구독중 입니다";
			subscribe = true;
			System.out.println(subCondition);
		}


		subscriptionCondition = new JLabel(subCondition);
		subscriptionCondition.setBounds(50,100,200,30);
		subscriptionCondition.setBorder(new LineBorder(Color.yellow,2));
		this.add(subscriptionCondition);




		explainLabel = new JLabel("로또 번호는 매일 오후3시에");
		explainLabel.setBounds(75, 150, 200, 30);
		this.add(explainLabel);
		explainLabel2 = new JLabel("가입시 등록된 메일로 발송됩니다.");
		explainLabel2.setBounds(60, 200, 200, 30);
		this.add(explainLabel2);


		//button
		lottoNumberApplyButton = new JButton("구독여부");
		lottoNumberApplyButton.setBounds(100, 300, 100 , 30);
		this.add(lottoNumberApplyButton);
		lottoNumberApplyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String answer = JOptionPane.showInputDialog("구독여부를 반드시 y아니면n으로만 입력해주세요");
				int n = dbModel.subscirbeLotto(answer, loginUserId);
				//1미입력 2정상수행구독신청 3정상수행구독취소 5수행안됨 4지정값외 입력
				switch (n) {
				case 1:
					ShowMessageToUser("구독여부를 입력해주세요");
					break;
				case 2:
					if(subscribe) {
						ShowMessageToUser("이미 구독신청되었습니다");
						return;
					}
					ShowMessageToUser("구독신청되었습니다");
					subscriptionCondition.setText("로또넘버받기 구독중 입니다");
					subscribe = true;
					break;
				case 3:
					if(!subscribe) {
						ShowMessageToUser("이미 구독취소되었습니다");
						return;
					}
					ShowMessageToUser("구독취소되었습니다");
					subscriptionCondition.setText("로또넘버받기 미구독중 입니다");
					subscribe = false;
					break;
				case 4:
					ShowMessageToUser("구독여부를 반드시 y아니면 n으로만 입력해주세요");
					break;
				}
			}
		});


	}
	private void ShowMessageToUser(String text) {
		JOptionPane.showMessageDialog(this, text);
	}

}