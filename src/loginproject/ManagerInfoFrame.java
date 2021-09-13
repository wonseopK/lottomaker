package loginproject;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class ManagerInfoFrame extends JFrame {
	//iv
	JLabel idTextFieldLabel, emailTextFieldabel,
	mobileTextFieldLabel, nameTextFieldLabel;
	JLabel idDisplayLable, emailDisplayLable,
	mobileDisplayLable,nameDisplayLable;

	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	MemberInfo memberInfo = dbModel.getMemberInfo("id", LoginSystemMain.loginUserId);

	private String loginUserId = memberInfo.getId();
	private String loginUserName = memberInfo.getName();
	private String loginUserEmail = memberInfo.getEmail();
	private String loginUserMobile = memberInfo.getMobile();

	//constructor
	public ManagerInfoFrame() {
		super("UPDATE");
		this.setBounds(800, 100, 450, 400);
		this.setDesign();
		LoginSystemMain.managerInfoFrameOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//프레임만 종료하기
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				//update 프레임 종료 main에 알리기
				LoginSystemMain.managerInfoFrameOn = false;
			}
		});

		this.setVisible(true);
	}

	private void setDesign() {
		this.setLayout(null);
		swingLableDesign();
		swingDisplayLableDesign();
		showUserDetailOnFrame();
	}

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

	public static void main(String[] args) {
		new ManagerInfoFrame();
	}

}
